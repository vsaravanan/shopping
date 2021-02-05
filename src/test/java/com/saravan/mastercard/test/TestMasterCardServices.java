package com.saravan.mastercard.test;

import com.saravan.mastercard.entity.Bill;
import com.saravan.mastercard.entity.BuyItem;
import com.saravan.mastercard.entity.Catalog;
import com.saravan.mastercard.entity.Order;
import com.saravan.mastercard.repo.BillRepo;
import com.saravan.mastercard.repo.BuyItemRepo;
import com.saravan.mastercard.repo.ItemRepo;
import com.saravan.mastercard.repo.OrderRepo;
import com.saravan.mastercard.service.CatalogService;
import com.saravan.mastercard.service.ShoppingService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = ShoppingServiceApplication.class) // optional in junit 5
//@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Transactional // important in junit 5
public class TestMasterCardServices {

    @Autowired
    CatalogService catalogService;

//    @Autowired
//    final JdbcTemplate jdbcTemplate = null;

    @Autowired
    ShoppingService shoppingService;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    BuyItemRepo buyItemRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    BillRepo billRepo;



        @BeforeEach
//    @Before
        void setUp() {
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

        private void clean() {

//            buyItemRepo.deleteAll();
//            billRepo.deleteAll();
//            orderRepo.deleteAll();

        }

//        @Nested
//        class TestShopping {

            @Test
            public void testShopping1() {

                clean();

                List<BuyItem> buyItems = new ArrayList<>();
                buyItemRepo.deleteAll();

                buyItems.add(new BuyItem("I"));
                buyItems.add(new BuyItem("G"));
                buyItems.add(new BuyItem("H", 2));

                Order shopping = shoppingService.createCheckout(buyItems);
                shoppingService.checkout(shopping);

                List< Order > listOrders = orderRepo.findAll();
                List< Bill > listBills = billRepo.findAll();

                Order order = listOrders.stream().findFirst().orElse(null);
                assertThat(order.getTotalSum().doubleValue()).isEqualTo(30.50);
                Bill billH = listBills.stream().filter(b -> b.getItemName().equals("H")).findFirst().orElse(null);
                assertThat(billH.getSellingPrice().doubleValue()).isEqualTo(4.50);
    //            log.info(billH);
                log.info("testShopping1 completed");
            }



            @Test
            public void testShopping2() {

                clean();

                List<BuyItem> buyItems = new ArrayList<>();


                buyItems.add(new BuyItem("C", 2));
                buyItems.add(new BuyItem("A", 2));
                buyItems.add(new BuyItem("B", 2));
                buyItems.add(new BuyItem("E", 1));
                buyItems.add(new BuyItem("F", 2));

                Order shopping = shoppingService.createCheckout(buyItems);
                shoppingService.checkout(shopping);

                List< Order > listOrders = orderRepo.findAll();
                List< Bill > listBills = billRepo.findAll();

                Order order = listOrders.stream().findFirst().orElse(null) ;
                assertThat(order.getTotalSum().doubleValue()).isEqualTo(42.0);
                Bill billC = listBills.stream().filter(b -> b.getItemName().equals("C")).findFirst().orElse(null);
                assert billC != null;
                assertThat(billC.getSellingPrice().doubleValue()).isEqualTo(0.00);
                assertThat(billC.getTotal().doubleValue()).isEqualTo(0.00);
    //            log.info(billC);

                log.info("testShopping2 completed");
            }


            @Test
            public void testShopping3() {

                clean();

                List<BuyItem> buyItems = new ArrayList<>();


                buyItems.add(new BuyItem("A", 3));
                buyItems.add(new BuyItem("B", 2));

                Order shopping = shoppingService.createCheckout(buyItems);
                shoppingService.checkout(shopping);

                List< Order > listOrders = orderRepo.findAll();
                List< Bill > listBills = billRepo.findAll();
                Order order = listOrders.stream().findFirst().orElse(null) ;
                assert order != null;
                assertThat(order.getTotalSum().doubleValue()).isEqualTo(35);
                Bill billB = listBills.stream().filter(b -> b.getItemName().equals("B")).findFirst().orElse(null);
                assert billB != null;
                assertThat(billB.getSellingPrice().doubleValue()).isEqualTo(0.00);
                BigDecimal sum = listBills.stream().filter(b -> b.getItemName().equals("B"))
                        .map(x -> x.getTotal())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                assertThat( sum.doubleValue()).isEqualTo(5.00);

                log.info("testShopping3 completed");

            }

            @Test
            public void testShopping4() {

                clean();

                List<BuyItem> buyItems = new ArrayList<>();


                buyItems.add(new BuyItem("A", 3));

                Order shopping = shoppingService.createCheckout(buyItems);
                shoppingService.checkout(shopping);


                List< Order > listOrders = orderRepo.findAll();
                List< Bill > listBills = billRepo.findAll();
                Order order = listOrders.stream().findFirst().orElse(null) ;
                assert order != null;

                assertThat(order.getTotalSum().doubleValue()).isEqualTo(20);
                Bill billA = listBills.stream().filter(b -> b.getItemName().equals("A"))
                        .filter(f -> f.getSellingPrice().doubleValue() == (0.00))
                        .findFirst().orElse(null);
                assert billA != null;

    //            jdbcTemplate.execute("insert into item values ('AA',10.00)");
    //            List<Item> items = itemRepo.findAll();

                assertThat(billA.getSellingPrice().doubleValue()).isEqualTo(0.00);

                log.info("testShopping4 completed");

            }

//        }

}
