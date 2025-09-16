package com.bootcamp.project_data_provider.model.dto;

import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CandlesDTO {
  public List<Double> c;
  public List<Double> h;
  public List<Double> l;
  public List<Double> o;
  public List<Long> t;
  public List<Long> v;
  public String s;
  public String symbol; // 添加 symbol 屬性
  // getters & setters omitted
}
