package br.com.cams7.orders.core.port.out;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface UpdateShippingByIdRepositoryPort {
  Mono<Long> updateShipping(String country, String orderId, Boolean registeredShipping);
}
