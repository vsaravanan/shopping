package com.saravan.mastercard.repo;

import com.saravan.mastercard.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {
}
