package com.bootcamp.project_stock_data.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "stock_profiles")
@Data
public class StockProfileEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "stock_id")
  private StockEntity stock;

  @Column(name = "company_name")
  private String companyName;

  private String industry;

  @Column(name = "market_cap")
  private Long marketCap;

  @Column(name = "logo_url")
  private String logoUrl;

  @Column(name = "shares_outstanding")
  private Long sharesOutstanding;

  @Column(name = "last_updated")
  private LocalDateTime lastUpdated;
}
