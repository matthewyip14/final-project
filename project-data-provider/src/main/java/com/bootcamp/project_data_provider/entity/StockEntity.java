package com.bootcamp.project_data_provider.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;


@Entity
@Table(name = "stocks") 
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String symbol;
  @Column(nullable = false)
  private Double currentPrice;
  @Column(nullable = false)
  private Double change;
  @Column(nullable = false)
  private Double percentChange;
  @Column(nullable = false)
  private Long volume;
}
