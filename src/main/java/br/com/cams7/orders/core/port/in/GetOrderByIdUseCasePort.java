package br.com.cams7.orders.core.port.in;

import br.com.cams7.orders.core.domain.OrderEntity;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface GetOrderByIdUseCasePort {
  Mono<OrderEntity> execute(String country, String orderId);
}
