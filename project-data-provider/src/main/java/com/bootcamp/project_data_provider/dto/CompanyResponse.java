package com.bootcamp.project_data_provider.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class CompanyResponse {
  private String country;
  private String currency;
  private String exchange;
  private String ipo;
  private double marketCapitalization;
  private String name;
  private String phone;
  private double shareOutstanding;
  private String ticker;
  private String weburl;
  private String logo;
  private String finnhubIndustry;
}
