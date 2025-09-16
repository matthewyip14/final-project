package com.bootcamp.project_data_provider.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class QuoteDTO {
  public Double c; // current price
  public Double d; // change
  public Double dp; // percent change
  public Double h; // high
  public Double l; // low
  public Double o; // open
  public Double pc; // previous close
  public String symbol; // 添加 symbol 屬性
  // getters & setters omitted for brevity
}
