package com.bootcamp.project_stock_data.repository;

import com.bootcamp.project_stock_data.entity.StockOhlcEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StockOhlcRepository extends JpaRepository<StockOhlcEntity, Long> {
  @Query("SELECT s FROM StockOhlcEntity s WHERE s.symbol = ?1")
  List<StockOhlcEntity> findBySymbol(String symbol);
}
