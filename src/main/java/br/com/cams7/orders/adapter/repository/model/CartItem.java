package br.com.cams7.orders.adapter.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
  private String productId;

  private Integer quantity;

  private Float unitPrice;
}
