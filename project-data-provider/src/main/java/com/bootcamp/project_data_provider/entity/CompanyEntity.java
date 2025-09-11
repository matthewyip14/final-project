package com.bootcamp.project_data_provider.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyEntity {
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
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String ticker;
  @Column(nullable = false)
  private String weburl;

  @ManyToOne
  @JoinColumn(name = "symbol", nullable = false)
  private StockEntity stockEntity;
  
}
