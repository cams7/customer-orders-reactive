package br.com.cams7.orders.core.domain;

import lombok.Data;

@Data
public class CustomerAddress {
  private String addressId;

  private String number;

  private String street;

  private String postcode;

  private String city;

  private String federativeUnit;

  private String country;
}
