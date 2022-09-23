package br.com.cams7.orders.adapter.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddress {
  private String addressId;

  private String number;

  private String street;

  private String postcode;

  private String city;

  private String federativeUnit;

  private String country;
}
