package br.com.cams7.orders.core.port.in;

import br.com.cams7.orders.core.domain.OrderEntity;
import reactor.core.publisher.Flux;

@FunctionalInterface
public interface GetOrdersByCountryUseCasePort {
  Flux<OrderEntity> execute(String country);
}
