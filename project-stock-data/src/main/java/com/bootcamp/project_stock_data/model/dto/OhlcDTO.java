package com.bootcamp.project_stock_data.model.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class OhlcDTO {
  private LocalDate date;
  private Double open;
  private Double high;
  private Double low;
  private Double close;
  private Long volume;
}
