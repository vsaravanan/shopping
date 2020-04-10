package com.saravan.mastercard.repo;

import com.saravan.mastercard.entity.Bill;
import com.saravan.mastercard.entity.BuyItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyItemRepo extends JpaRepository< BuyItem, Long> {
}
