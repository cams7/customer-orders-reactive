package br.com.cams7.orders.adapter.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Customer {
  @Schema(example = "703c327b-8b61-4f32-bf1a-fb3108a6f7e1", description = "Customer id")
  private String customerId;

  @Schema(example = "Gustavo Severino Tomás Ramos", description = "Customer full name")
  private String fullName;

  @Schema(example = "gustavo", description = "Customer username")
  private String username;
}
