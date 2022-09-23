package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.OrderEntity;
import reactor.core.publisher.Flux;

@FunctionalInterface
public interface GetOrdersRepositoryPort {
  Flux<OrderEntity> findAll(String country);
}
