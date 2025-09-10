package com.bootcamp.project_data_provider.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter

public class QuoteResponse {
  private String symbol;
  private double currentPrice;
  private double change;
  private double percentChange;
  private long volume;
}
