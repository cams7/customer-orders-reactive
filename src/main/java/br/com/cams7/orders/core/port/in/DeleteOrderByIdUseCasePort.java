package br.com.cams7.orders.core.port.in;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface DeleteOrderByIdUseCasePort {
  Mono<Void> execute(String country, String orderId);
}
