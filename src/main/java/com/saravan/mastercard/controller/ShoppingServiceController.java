package com.saravan.mastercard.controller;

import com.saravan.mastercard.entity.*;
import com.saravan.mastercard.repo.ItemRepo;
import com.saravan.mastercard.service.CatalogService;
import com.saravan.mastercard.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class ShoppingServiceController {

    @Autowired
    CatalogService catalogService;

    @Autowired
    ShoppingService shoppingService;

    @Autowired
    ItemRepo itemRepo;

    @PostMapping("/catalogs")
    public ApiResponse<List<Item>> addCatalogs(@RequestBody List<Catalog> catalogs) {

        for (Catalog catalog : catalogs) {

            Catalog newcatalog = new Catalog(catalog.getItemName(),
                    catalog.getItemPrice(), catalog.getPromo1(), catalog.getPromo2());
            catalogService.priceItem(newcatalog);

        }

        List<Item> listItems = itemRepo.findAll();
        return new ApiResponse<>(HttpStatus.OK, "new Catalogs was Successfully created ", listItems);

    }

    @PostMapping("/shopping")
    public ApiResponse<Order> shopping(@RequestBody List<BuyItem> buyItems) {


        Shopping shopping = shoppingService.createCheckout(buyItems);

        if ( shopping.getCarts().size() == 0 ) {
            return new ApiResponse<>(HttpStatus.OK, "No mating items found in shop ", null);
        }


        Order order = shoppingService.checkout(shopping);


        return new ApiResponse<>(HttpStatus.OK, "shopping checkout was successfully finished ", order);

    }


}
