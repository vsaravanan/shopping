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

    private void calculateNormal(Shopping shopping) {

        Order order = shopping.getOrder();

        if (shopping.getCarts().size() == 0) {
            return;
        }

        List<Cart> carts = shopping.getCarts().stream()
                .filter(r -> ! r.getItem().getPromotion().getPromo1()
                        && ! r.getItem().getPromotion().getPromo2()).collect(Collectors.toList());

        for (Cart cart : carts) {

            Bill bill = new Bill(cart);

            bill.setTotalPrice(bill.getItemPrice().multiply(new BigDecimal(cart.getCounts())));
            bill.setDiscountedPrice(bill.getItemPrice());
            bill.setOrder (order);

            shopping.addBill(bill);

        }

    }

    private void calculatePromo1(Shopping shopping) {

        Order order = shopping.getOrder();

        if (shopping.getCarts().size() == 0) {
            return;
        }

        List<Cart> cartsPromo1 = shopping.getCarts().stream()
                .filter(r -> r.getItem().getPromotion().getPromo1()).collect(Collectors.toList());

//        Collections.sort(cartsPromo1, new CartPriceSorter());

        for (Cart cart : cartsPromo1) {

            BigDecimal newPrice = new BigDecimal(0);

            Bill bill = new Bill(cart);

            BigDecimal itemPrice = bill.getItemPrice();

            int promo1Count = 0;
            while (promo1Count < bill.getCounts()) {

                BigDecimal promo1priceAfterDiscount = itemPrice.multiply(promo1discount);

                promo1Count++;
                if ( promo1Count % promo1freeAt == 0) {
                    newPrice = promo1priceAfterDiscount;
                }
                else {
                    newPrice = itemPrice;
                }
                bill.setTotalPrice(bill.getTotalPrice().add(newPrice));
            }

            bill.setDiscountedPrice(newPrice);
            bill.setOrder (order);

            shopping.addBill(bill);


        }

    }

    private void calculatePromo2(Shopping shopping) {

        Order order = shopping.getOrder();

        if (shopping.getCarts().size() == 0) {
            return;
        }

        List<Cart> cartsPromo2 = shopping.getCarts().stream()
                .filter(r -> r.getItem().getPromotion().getPromo2()).collect(Collectors.toList());

        Collections.sort(cartsPromo2, new CartPriceSorter());

        // loop promo2 items
        int promo2count = 0;
        for (Cart cart : cartsPromo2) {

            BigDecimal newPrice = new BigDecimal(0);

            Bill bill = new Bill(cart);

            BigDecimal itemPrice = bill.getItemPrice();
            int cartIndex = 0;

            while (cartIndex < bill.getCounts()) {

                BigDecimal promo2priceAfterDiscount = itemPrice.multiply(promo2discount);

                promo2count++;
                cartIndex++;
                if ( promo2count % promo2freeAt == 0) {
                    newPrice = promo2priceAfterDiscount;
                }
                else {
                    newPrice = itemPrice;
                }
                bill.setTotalPrice(bill.getTotalPrice().add(newPrice));
            }

            bill.setDiscountedPrice(newPrice);
            bill.setOrder (order);

            shopping.addBill(bill);


        }

    }


    public Order checkout(Shopping shopping) {
        Order order = orderRepo.saveAndFlush(shopping.getOrder());

        calculatePromo1(shopping);
        calculatePromo2(shopping);
        calculateNormal(shopping);

        // filter other than promo2 items
        List<Bill> bills = shopping.getBills();

        order.setTotalSum(bigzero);
        for (Bill bill : bills) {

            order.setTotalSum( order.getTotalSum().add(bill.getTotalPrice()));
            log.info("{} : Discounted rate : {},   Total Price : {}, Total Sum : {}", bill.getItemName(), bill.getDiscountedPrice(), bill.getTotalPrice(), order.getTotalSum());

        }


        billRepo.saveAll(shopping.getBills());
        billRepo.flush();
        order.setBill(shopping.getBills());
        orderRepo.saveAndFlush(order);

        return order;

    }



}
