package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.Customer;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface GetCustomerServicePort {
  Mono<Customer> getCustomer(String customerUrl);
}
