package com.saravan.mastercard.repo;

import com.saravan.mastercard.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepo extends JpaRepository<Item, String> {


}
