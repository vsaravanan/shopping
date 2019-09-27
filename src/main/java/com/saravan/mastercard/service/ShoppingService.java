package com.saravan.mastercard.service;

import com.saravan.mastercard.entity.*;
import com.saravan.mastercard.repo.BillRepo;
import com.saravan.mastercard.repo.ItemRepo;
import com.saravan.mastercard.repo.OrderRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ShoppingService {


    @Autowired
    OrderRepo orderRepo;

    @Autowired
    ItemRepo itemRepo;

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

    private Shopping newOrder() {
        Order order = new Order();
        Shopping shopping = new Shopping(order);
        return shopping;
    }

    public Shopping createCheckout(List<BuyItem> buyItems) {
        Shopping shopping = newOrder();

        for (BuyItem buyItem : buyItems) {

            Item item = findById (buyItem.getItemName());
            if (item != null) {
                shopping.addCart(item, buyItem.getCounts());
            }

        }
        return shopping;
    }

    public Item findById(String itemName) {
        return itemRepo.findById(itemName).orElse(null);
    }

    private void calculatePromo1(Shopping shopping) {


        if (shopping.getBills().size() == 0) {
            return;
        }


        List<Bill> bills = shopping.getBills().stream()
                .filter(Bill::getPromo1).collect(Collectors.toList());

        for (Bill bill : bills) {

            int findpromo1count = bill.getNormalcounts() / promo1freeAt;

            bill.setNormalcounts(bill.getNormalcounts() - findpromo1count);
            bill.setPromo1counts(bill.getPromo1counts() + findpromo1count);

        }

    }

    private void calculatePromo2(Shopping shopping) {

        if (shopping.getBills().size() == 0) {
            return;
        }

        List<Bill> bills = shopping.getBills().stream()
                .filter(Bill::getPromo2).collect(Collectors.toList());

        Collections.sort(bills, new BillPriceSorter());

        // loop promo2 items
        int promo2count = 0;
        for (Bill bill : bills) {
            Integer currentIndex = bills.indexOf(bill);

            int cartIndex = 0;

            while (cartIndex++ <= bill.getNormalcounts()) {
                promo2count++;
                if (promo2count == promo2freeAt) {

                    Bill tmpBill = null;
                    Iterator<Bill> iter = bills.listIterator(currentIndex);
                    int iterNav = 0;
                    while (iterNav++ < promo2freeAt && iter.hasNext()) {
                        tmpBill = iter.next();
                    }

                    if (!bill.equals(tmpBill)) {
                        tmpBill.setNormalcounts(tmpBill.getNormalcounts() - 1);
                        tmpBill.setPromo2counts(tmpBill.getPromo2counts() + 1);
                        promo2count = 0;
                    } else {
                        promo2count--;
                    }

                }

            }


        }
    }


    public Order checkout(Shopping shopping) {
        Order order = orderRepo.saveAndFlush(shopping.getOrder());


        shopping.getCarts().forEach(f -> shopping.addBill (new Bill(f)));


        calculatePromo1(shopping);
        calculatePromo2(shopping);

        // filter other than promo2 items
        List<Bill> bills = shopping.getBills();

        order.setTotalSum(bigzero);
        for (Bill bill : bills) {

            if ( bill.getPromo1()) {
                BigDecimal promo1priceAfterDiscount = bill.getItemPrice().multiply(promo1discount);
                bill.setDiscountedPrice(promo1priceAfterDiscount);
                bill.setPromo1Total(bill.getDiscountedPrice().multiply(new BigDecimal(bill.getPromo1counts())));

                if (bill.getPromo1counts() > 0)  {
                    log.info("Item {} :  promo1 qty : {} * ${} = {}",
                            bill.getItemName(), bill.getPromo1counts(),  bill.getDiscountedPrice(), bill.getPromo1Total() ) ;
                }

            }
            if ( bill.getPromo2()) {
                BigDecimal promo2priceAfterDiscount = bill.getItemPrice().multiply(promo2discount);
                bill.setDiscountedPrice(promo2priceAfterDiscount);
                bill.setPromo2Total(bill.getDiscountedPrice().multiply(new BigDecimal(bill.getPromo2counts())));

                if (bill.getPromo2counts() > 0)  {
                    log.info("Item {} :  promo2 qty : {} * ${} = {}",
                            bill.getItemName(), bill.getPromo2counts(),  bill.getDiscountedPrice(), bill.getPromo2Total() ) ;
                }
            }
            bill.setNormalTotal(bill.getItemPrice().multiply(new BigDecimal(bill.getNormalcounts())));

            if (bill.getNormalcounts() > 0)  {
                log.info("Item {} :  normal qty : {} * ${} = {}",
                        bill.getItemName(), bill.getNormalcounts(),  bill.getItemPrice(), bill.getNormalTotal() ) ;
            }


            bill.setTotalPrice( bill.getNormalTotal().add(bill.getPromo1Total()).add(bill.getPromo2Total()));
            order.setTotalSum( order.getTotalSum().add(bill.getTotalPrice()));
            bill.setOrder (order);

            log.info("Item {} :                                       => Total Price {} => Total Order Sum accumulated : {} ",
                    bill.getItemName(), bill.getTotalPrice(), order.getTotalSum());

        }


        billRepo.saveAll(shopping.getBills());
        billRepo.flush();
        order.setBill(shopping.getBills());
        orderRepo.saveAndFlush(order);


        return order;

    }



}
