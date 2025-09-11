package com.bootcamp.project_stock_data.repository;

import com.bootcamp.project_stock_data.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockEntity, String> {
  
}
