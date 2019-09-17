package com.saravan.mastercard.repo;

import com.saravan.mastercard.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
}
