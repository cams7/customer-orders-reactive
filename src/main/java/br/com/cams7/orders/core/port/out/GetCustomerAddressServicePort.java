package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.CustomerAddress;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface GetCustomerAddressServicePort {
  Mono<CustomerAddress> getCustomerAddress(
      String country, String requestTraceId, String addressUrl);
}
