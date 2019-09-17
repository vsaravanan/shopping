package com.saravan.mastercard.repo;

import com.saravan.mastercard.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepo extends JpaRepository<Bill, Long> {
}
