package com.bootcamp.project_stock_data.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bootcamp.project_stock_data.entity.StockOhlcEntity;

@Repository
public interface StockOhlcRepository extends JpaRepository<StockOhlcEntity, Long> {
  List<StockOhlcEntity> findByStockIdOrderByDateAsc(Long stockId);
}
