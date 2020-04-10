package com.saravan.mastercard.service;

import com.saravan.mastercard.entity.*;
import com.saravan.mastercard.repo.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

@Service
@Log4j2
public class ShoppingService {



    @Autowired
    ItemRepo itemRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    BillRepo billRepo;

    @Value("${promo1discount}")
    BigDecimal promo1discount;

    @Value("${promo2discount}")
    BigDecimal promo2discount;

    @Value("${promo1freeAt}")
    Integer promo1freeAt;

    @Value("${promo2freeAt}")
    Integer promo2freeAt;

    BigDecimal bigzero = new BigDecimal(0);


    public Order createCheckout(List<BuyItem> buyItems) {

        Order shopping = new Order();

        buyItems.forEach(f -> {
            Item item = findById (f.getItemName());
            if (item != null) {
                shopping.addBill(item, f.getCounts());
            }
        });

        return shopping;


    }

    public Item findById(String itemName) {
        return itemRepo.findById(itemName).orElse(null);
    }


    private static Predicate< Bill > predicatePromo1() {
        return p -> p.getItem().getPromotion().getPromo1();
    }
    protected static Predicate< Bill > predicatePromo2() {
        return p -> p.getItem().getPromotion().getPromo2();
    }
    protected static Predicate< Bill > predicateNormal() {
        return p -> ! (
                p.getItem().getPromotion().getPromo1() ||
                p.getItem().getPromotion().getPromo2() ) ;
    }

    private Order genericPromo(Order shopping,
                               Predicate< Bill > predicatePromo,
                               Integer promofreeAt,
                               BigDecimal promoDiscount) {

        Order order = new Order();

        if (shopping.getListBills().size() == 0) {
            return order;
        }


        List< Bill > bills = shopping.getListBills().stream()
                .filter(predicatePromo).collect(Collectors.toList());

        if (bills.size() == 0) {
            return order;
        }

        Collections.sort(bills, new BillPriceSorter());

        List< Bill > items = new LinkedList<>();


        for (Bill bill : bills) {
            IntStream.range(0,bill.getQty())
                    .forEach(i -> {
                        try {
                            Bill billCloned =  (Bill) bill.clone();
                            billCloned.setQty(1);
                            items.add(billCloned);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    });
        }


        for (int i = 0;  i < items.size(); i++) {
            Bill bill = null;
            if (promofreeAt > 0 && (i+1) % promofreeAt == 0) {
                bill = items.get(items.size() - 1);
                items.remove(items.size() - 1);
                BigDecimal promoPriceAfterDiscount = bill.getItemPrice().multiply(promoDiscount);
                bill.setSellingPrice(promoPriceAfterDiscount);
                items.add(i,bill);
            }
            else {
                bill = items.get(i);
            }
            order.addBill(bill);
        }

        return order;

    }



    public Order checkout(Order shopping) {

        Order orderPromo1 = genericPromo(shopping, predicatePromo1(), promo1freeAt, promo1discount);
        Order orderPromo2 = genericPromo(shopping, predicatePromo2(), promo2freeAt, promo2discount);
        Order orderNormal = genericPromo(shopping, predicateNormal(), -1, new BigDecimal(100) );


        List< Bill > finalBill = new ArrayList<>();
        finalBill.addAll(orderPromo1.getListBills());
        finalBill.addAll(orderPromo2.getListBills());
        finalBill.addAll(orderNormal.getListBills());

        Order finalOrder = new Order();

        finalBill.forEach(f -> {
            f.setOrder(finalOrder);
            log.info(f.toString());
        });

        finalOrder.setListBills(finalBill);
        finalOrder.recalc();

        orderRepo.saveAndFlush(finalOrder);

        billRepo.saveAll(finalOrder.getListBills());
        billRepo.flush();


        return finalOrder;

    }



}
