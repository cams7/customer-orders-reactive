package br.com.cams7.orders.core.domain;

import lombok.Data;

@Data
public class Customer {
  private String customerId;

  private String fullName;

  private String username;
}
