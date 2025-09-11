package com.bootcamp.project_stock_data.entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class StockOhlcEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String symbol;
  @Column(nullable = false)
  private LocalDate date;
  @Column(nullable = false) 
  private Double open;
  @Column(nullable = false)
  private Double high;
  @Column(nullable = false)
  private Double low;
  @Column(nullable = false)
  private Double close;
  @Column(nullable = false)
  private Long volume;

  @ManyToOne
  @JoinColumn(name = "symbol", referencedColumnName = "symbol", insertable = false, updatable = false)
  private StockEntity stockEntity;
}
