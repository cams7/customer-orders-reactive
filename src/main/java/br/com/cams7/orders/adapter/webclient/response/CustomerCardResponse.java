package br.com.cams7.orders.adapter.webclient.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCardResponse {
  private String id;

  private String longNum;

  private String expires;

  private String ccv;
}
