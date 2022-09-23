package br.com.cams7.orders.adapter.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CustomerAddress {
  @Schema(example = "a679084a-03e7-49c7-9516-5a7d6757e1c2", description = "Customer address id")
  private String addressId;

  @Schema(example = "956", description = "Customer address number")
  private String number;

  @Schema(example = "Rua Manoel Gregório Mattos", description = "Customer address street")
  private String street;

  @Schema(example = "89816-170", description = "Customer address postcode")
  private String postcode;

  @Schema(example = "Chapecó", description = "Customer address city")
  private String city;

  @Schema(example = "SC", description = "Customer address federative unit")
  private String federativeUnit;

  @Schema(example = "BR", description = "Customer address country")
  private String country;
}
