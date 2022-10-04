package br.com.cams7.orders.core.port.out;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface AddShippingOrderServicePort {
  Mono<String> add(String country, String requestTraceId, String orderId);
}
