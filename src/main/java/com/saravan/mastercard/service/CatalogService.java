package com.saravan.mastercard.service;

import com.saravan.mastercard.entity.Catalog;
import com.saravan.mastercard.repo.ItemRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CatalogService {

    @Autowired
    ItemRepo itemRepo;

    public void priceItem(Catalog catalog) {

        itemRepo.saveAndFlush(catalog.getItem() );


    }

}
