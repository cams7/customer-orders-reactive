package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.CustomerCard;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface GetCustomerCardServicePort {
  Mono<CustomerCard> getCustomerCard(
      String country, String requestTraceId, String customerId, String longNum);
}
