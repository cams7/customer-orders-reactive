package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.Payment;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface VerifyPaymentServicePort {
  Mono<Payment> verify(String country, String requestTraceId, String customerId, Float amount);
}
