package com.bootcamp.project_stock_data.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "stock_ohlc_data")
@Data
public class StockOhlcEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "stock_id")
  private StockEntity stock;

  private LocalDateTime date;

  private Double open;

  private Double high;

  private Double low;

  private Double close;

  private Long volume;
}
