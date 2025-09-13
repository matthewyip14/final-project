package com.bootcamp.project_stock_data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bootcamp.project_stock_data.entity.StockProfileEntity;

@Repository
public interface StockProfileRepository extends JpaRepository<StockProfileEntity, Long> {
  StockProfileEntity findByStockId(Long stockId);
}
