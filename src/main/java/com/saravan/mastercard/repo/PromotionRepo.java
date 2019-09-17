package com.saravan.mastercard.repo;

import com.saravan.mastercard.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepo extends JpaRepository<Promotion, String> {
}
