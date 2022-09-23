package br.com.cams7.orders.adapter.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCard {
  @Schema(example = "818c8544-dfb1-49b2-8212-eb9dcdbd57c9", description = "Customer card id")
  private String cardId;
}
