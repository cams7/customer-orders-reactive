package br.com.cams7.orders.core.port.in;

import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.in.params.CreateOrderCommand;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface CreateOrderUseCasePort {
  Mono<OrderEntity> execute(String country, CreateOrderCommand order);
}
