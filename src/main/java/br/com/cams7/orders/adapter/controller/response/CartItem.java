package br.com.cams7.orders.adapter.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
  @Schema(example = "9aff4cc5-f921-4157-8976-41ceae93ae54", description = "Product id")
  private String productId;

  @Schema(example = "3", description = "Quantity specified in cart")
  private Integer quantity;

  @Schema(example = "18.0", description = "Product unit price")
  private Float unitPrice;
}
