package com.saravan.mastercard.test;

import com.saravan.mastercard.ShoppingServiceApplication;
import com.saravan.mastercard.entity.*;
import com.saravan.mastercard.repo.BillRepo;
import com.saravan.mastercard.repo.OrderRepo;
import com.saravan.mastercard.service.CatalogService;
import com.saravan.mastercard.service.ShoppingService;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;


@Log4j2
@SpringBootTest
//@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ShoppingServiceApplication.class)
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


//    @Nested
//    class TestShopping {
//        @BeforeEach
    @Before
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
            assertThat(billH.getDiscountedPrice().doubleValue()).isEqualTo(4.50);
            log.info(billH);
            log.info("testShopping1 completed");
        }

        @Test
        public void testShopping2() {

            List<BuyItem> buyItems = new ArrayList<>();


            buyItems.add(new BuyItem("A", 2));
            buyItems.add(new BuyItem("B", 2));
            buyItems.add(new BuyItem("C", 2));
            buyItems.add(new BuyItem("F", 2));

            Shopping shopping = shoppingService.createCheckout(buyItems);
            shoppingService.checkout(shopping);

            List<Order> listOrders = orderRepo.findAll();
            List<Bill> listBills = billRepo.findAll();

            Order order = listOrders.stream().findFirst().orElse(null) ;
            assertThat(order.getTotalSum().doubleValue()).isEqualTo(34.0);
            Bill billC = listBills.stream().filter(b -> b.getItemName().equals("C")).findFirst().orElse(null);
            assert billC != null;
            assertThat(billC.getDiscountedPrice().doubleValue()).isEqualTo(0.00);
            assertThat( billC.getTotalPrice().doubleValue()).isEqualTo(0.00);
            log.info(billC);



            log.info("testShopping2 completed");
        }


        @Test
        public void testShopping3() {

            List<BuyItem> buyItems = new ArrayList<>();


            buyItems.add(new BuyItem("A", 3));
            buyItems.add(new BuyItem("B", 2));

            Shopping shopping = shoppingService.createCheckout(buyItems);
            shoppingService.checkout(shopping);

            List<Order> listOrders = orderRepo.findAll();
            List<Bill> listBills = billRepo.findAll();
            Order order = listOrders.stream().findFirst().orElse(null) ;
            assert order != null;
            assertThat(order.getTotalSum().doubleValue()).isEqualTo(35);
            Bill billB = listBills.stream().filter(b -> b.getItemName().equals("B")).findFirst().orElse(null);
            assert billB != null;
            assertThat(billB.getDiscountedPrice().doubleValue()).isEqualTo(0.00);
            assertThat( billB.getTotalPrice().doubleValue()).isEqualTo(5.00);
            log.info(billB);


        }

        @Test
        public void testShopping4() {

            List<BuyItem> buyItems = new ArrayList<>();


            buyItems.add(new BuyItem("A", 3));

            Shopping shopping = shoppingService.createCheckout(buyItems);
            shoppingService.checkout(shopping);


            List<Order> listOrders = orderRepo.findAll();
            List<Bill> listBills = billRepo.findAll();
            Order order = listOrders.stream().findFirst().orElse(null) ;
            assert order != null;
            assertThat(order.getTotalSum().doubleValue()).isEqualTo(30);
            Bill billA = listBills.stream().filter(b -> b.getItemName().equals("A")).findFirst().orElse(null);
            assert billA != null;
            assertThat(billA.getDiscountedPrice().doubleValue()).isEqualTo(0.00);
            assertThat( billA.getTotalPrice().doubleValue()).isEqualTo(30.00);
            log.info(billA);


        }
//    }


}
