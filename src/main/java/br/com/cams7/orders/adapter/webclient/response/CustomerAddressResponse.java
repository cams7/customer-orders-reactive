package br.com.cams7.orders.adapter.webclient.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressResponse {
  private String id;

  private String number;

  private String street;

  private String postcode;

  private String city;

  private String federativeUnit;

  private String country;
}
