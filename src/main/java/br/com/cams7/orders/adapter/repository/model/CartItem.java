package br.com.cams7.orders.adapter.repository.model;

import lombok.Data;

@Data
public class CartItem {
  private String productId;

  private Integer quantity;

  private Float unitPrice;
}
