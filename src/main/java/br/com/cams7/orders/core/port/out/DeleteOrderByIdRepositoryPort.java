package br.com.cams7.orders.core.port.out;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface DeleteOrderByIdRepositoryPort {
  Mono<Long> delete(String country, String orderId);
}
