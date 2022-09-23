package br.com.cams7.orders.core.domain;

import lombok.Data;

@Data
public class CartItem {
  private String productId;

  private Integer quantity;

  private Float unitPrice;
}
