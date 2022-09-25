package br.com.cams7.orders.adapter.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

  @Schema(
      example = "http://localhost:3000/customers/703c327b-8b61-4f32-bf1a-fb3108a6f7e1",
      description = "URL to access customer data")
  private String customerUrl;

  @Schema(
      example = "http://localhost:3000/addresses/a679084a-03e7-49c7-9516-5a7d6757e1c2",
      description = "URL to access customer address data")
  private String addressUrl;

  @Schema(
      example = "http://localhost:3000/cards/818c8544-dfb1-49b2-8212-eb9dcdbd57c9",
      description = "URL to access customer card data")
  private String cardUrl;

  @Schema(
      example = "http://localhost:3001/carts/bad2ba11-19b2-4af8-b41e-dccc47f69929/items",
      description = "URL to access cart items")
  private String itemsUrl;
}
