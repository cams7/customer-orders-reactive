package br.com.cams7.orders.adapter.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
  private String customerId;

  private String fullName;

  private String username;
}
