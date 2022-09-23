package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.OrderEntity;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface GetOrderByIdRepositoryPort {
  Mono<OrderEntity> getOrder(String country, String orderId);
}
