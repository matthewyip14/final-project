package com.bootcamp.project_stock_data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class CompanyEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String ticker;
  @Column(nullable = false)
  private String country;
  @Column(nullable = false)
  private String currency;
  @Column(nullable = false)
  private String exchange;
  @Column(nullable = false)
  private String finnhubIndustry;
  @Column(nullable = false)
  private String ipo;
  @Column(nullable = false)
  private String logo;
  @Column(nullable = false)
  private Double marketCapitalization;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String phone;
  @Column(nullable = false)
  private Double shareOutstanding;
  @Column(nullable = false)
  private String weburl;

  @OneToOne
  @JoinColumn(name = "ticker", referencedColumnName = "symbol", insertable = false, updatable = false)  
  private StockEntity stockEntity;
}
