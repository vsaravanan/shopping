package com.saravan.mastercard.test;

import com.saravan.mastercard.entity.Order;
import com.saravan.mastercard.entity.*;
import com.saravan.mastercard.repo.*;
import com.saravan.mastercard.service.CatalogService;
import com.saravan.mastercard.service.ShoppingService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Log4j2
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Transactional

public class TestMasterCardServices {

    @Autowired
    CatalogService catalogService;


    @Autowired
    ShoppingService shoppingService;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    BillRepo billRepo;


    @Nested
    class TestShopping {
        @BeforeEach
        public void setUp() {
            Catalog catalog = new Catalog("A", new BigDecimal(10), false, true);
            catalogService.priceItem(catalog);

            catalog = new Catalog("B", new BigDecimal(5), false, true);
            catalogService.priceItem(catalog);
            catalog = new Catalog("C", new BigDecimal(4), false, true);
            catalogService.priceItem(catalog);
            catalog = new Catalog("D", new BigDecimal(3), false, true);
            catalogService.priceItem(catalog);
            catalog = new Catalog("E", new BigDecimal(8), false, true);
            catalogService.priceItem(catalog);
            catalog = new Catalog("F", new BigDecimal(2), false, false);
            catalogService.priceItem(catalog);

            catalog = new Catalog("G", new BigDecimal(10), true, false);
            catalogService.priceItem(catalog);
            catalog = new Catalog("H", new BigDecimal(9), true, false);
            catalogService.priceItem(catalog);

            catalog = new Catalog("I", new BigDecimal(7));
            catalogService.priceItem(catalog);




            log.info("TestShopping setUp completed");

        }

        @Test
        public void testShopping1() {

            List<BuyItem> buyItems = new ArrayList<>();

            buyItems.add(new BuyItem("I"));
            buyItems.add(new BuyItem("G"));
            buyItems.add(new BuyItem("H", 2));

            Shopping shopping = shoppingService.createCheckout(buyItems);
            shoppingService.checkout(shopping);

            List<Order> listOrders = orderRepo.findAll();
            List<Bill> listBills = billRepo.findAll();

            Order order = listOrders.stream().findFirst().orElse(null);
            assertThat(order.getTotalSum().doubleValue()).isEqualTo(30.50);
            Bill billH = listBills.stream().filter(b -> b.getItemName().equals("H")).findFirst().orElse(null);
            assertThat(billH.getDiscountedPrice().toString()).isEqualTo("4.50");

            log.info("testShopping1 completed");
        }

        @Test
        public void testShopping2() {

            List<BuyItem> buyItems = new ArrayList<>();

            buyItems.add(new BuyItem("A"));
            buyItems.add(new BuyItem("B"));
            buyItems.add(new BuyItem("C"));
            buyItems.add(new BuyItem("F"));

            Shopping shopping = shoppingService.createCheckout(buyItems);
            shoppingService.checkout(shopping);

            List<Order> listOrders = orderRepo.findAll();
            List<Bill> listBills = billRepo.findAll();

            Order order = listOrders.get(1) ;
            assertThat(order.getTotalSum().doubleValue()).isEqualTo(17.0);
            Bill billC = listBills.stream().filter(b -> b.getItemName().equals("C")).findFirst().orElse(null);
            assertThat(billC.getDiscountedPrice().toString()).isEqualTo("0.00");
            assertThat(billC.getTotalPrice().toString()).isEqualTo("0.00");



            log.info("testShopping2 completed");
        }
    }


}
