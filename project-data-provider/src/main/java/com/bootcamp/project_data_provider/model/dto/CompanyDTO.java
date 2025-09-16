package com.bootcamp.project_data_provider.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CompanyDTO {
  public String country;
  public String currency;
  public String exchange;
  public String finnhubIndustry;
  public String ipo;
  public String logo;
  public Long marketCapitalization;
  public String name;
  public Long shareOutstanding;
  public String ticker;
  public String weburl;
  public String symbol; // 添加 symbol 屬性
  // getters & setters omitted
}
