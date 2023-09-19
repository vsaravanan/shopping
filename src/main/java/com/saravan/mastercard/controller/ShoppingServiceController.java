package com.saravan.mastercard.controller;

import com.saravan.mastercard.entity.*;
import com.saravan.mastercard.repo.ItemRepo;
import com.saravan.mastercard.service.CatalogService;
import com.saravan.mastercard.service.ShoppingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ShoppingService", description = "Shopping Service Api")
@RestController
@RequestMapping("api")
public class ShoppingServiceController {

    @Autowired
    CatalogService catalogService;

    @Autowired
    ShoppingService shoppingService;

    @Autowired
    ItemRepo itemRepo;

    @Operation(
            summary = "add catalog",
            description = "add stock into shopping mall")
    @ApiResponses( {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping("/catalogs")
    public ApiResp<List<Item>> addCatalogs(@RequestBody List<Catalog> catalogs) {

        for (Catalog catalog : catalogs) {

            Catalog newcatalog = new Catalog(catalog.getItemName(),
                    catalog.getItemPrice(), catalog.getPromo1(), catalog.getPromo2());
            catalogService.priceItem(newcatalog);

        }

        List<Item> listItems = itemRepo.findAll();
        return new ApiResp<>(HttpStatus.OK, "new Catalogs was Successfully created ", listItems);

    }

    @Operation(
            summary = "shopping cart",
            description = "customer is buying items")
    @ApiResponses( {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping("/shopping" )
    public ApiResp< Order > shopping(@RequestBody List<BuyItem> buyItems) {


        Order shopping = shoppingService.createCheckout(buyItems);

        if (shopping.getListBills().isEmpty()) {
            return new ApiResp<>(HttpStatus.OK, "No matching items found in shop ", null);
        }


        Order order = shoppingService.checkout(shopping);


        return new ApiResp<>(HttpStatus.OK, "shopping checkout was successfully finished ", order);

    }


}
