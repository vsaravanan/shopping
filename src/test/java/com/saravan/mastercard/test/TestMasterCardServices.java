package com.saravan.mastercard.test;

//import com.saravan.mastercard.entity.Order;

import com.saravan.mastercard.entity.Order;
import com.saravan.mastercard.entity.*;
import com.saravan.mastercard.repo.CartRepo;
import com.saravan.mastercard.repo.ItemRepo;
import com.saravan.mastercard.repo.OrderRepo;
import com.saravan.mastercard.repo.PromotionRepo;
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
import java.util.List;

//import org.springframework.core.annotation.Order;

@Log4j2
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Transactional
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestMasterCardServices {

    @Autowired
    CatalogService catalogService;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    PromotionRepo promotionRepo;

    @Autowired
    ShoppingService shoppingService;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    CartRepo cartRepo;



//    @Nested
//    @Disabled
//    class TestItemAndPromotion {
//        @BeforeEach
//        public void setUp() {
//
//            Catalog catalog = new Catalog("A", new BigDecimal(10), true, false);
//            catalogService.priceItem(catalog);
//            catalog = new Catalog("B", new BigDecimal(9), true, false);
//            catalogService.priceItem(catalog);
//            log.info("TestItemAndPromotion setUp completed");
//
//
//
//        }
//
//        @Test
////        @org.junit.jupiter.api.Order(1)
//        public void testItemAndPromotion() {
//            List<Item> listItems = itemRepo.findAll();
//            List<Promotion> listPromotions = promotionRepo.findAll();
//            log.info("testItemAndPromotion completed");
//        }
//    }

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





            log.info("TestShopping setUp completed");

        }

        @Test
//        @org.junit.jupiter.api.Order(2)
        public void testShopping1() {

            Shopping shopping = shoppingService.newOrder();
            Item item = shoppingService.findById ("G");
            shopping.addCart(item,1);
            item = shoppingService.findById ("H");
            shopping.addCart(item,2);

            shoppingService.checkout(shopping);
            List<Order> listOrders = orderRepo.findAll();
            List<Cart> listCarts = cartRepo.findAll();
            log.info("testShopping1 completed");
        }

        @Test
        public void testShopping2() {

            Shopping shopping = shoppingService.newOrder();
            Item item = shoppingService.findById ("A");
            shopping.addCart(item,1);
            item = shoppingService.findById ("B");
            shopping.addCart(item,1);
            item = shoppingService.findById ("C");
            shopping.addCart(item,1);
            item = shoppingService.findById ("F");
            shopping.addCart(item,1);

            shoppingService.checkout(shopping);

            List<Order> listOrders = orderRepo.findAll();
            List<Cart> listCarts = cartRepo.findAll();
            log.info("testShopping completed");
        }
    }


}
