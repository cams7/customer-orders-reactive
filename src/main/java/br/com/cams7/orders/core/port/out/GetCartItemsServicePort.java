package br.com.cams7.orders.core.port.out;

import br.com.cams7.orders.core.domain.CartItem;
import reactor.core.publisher.Flux;

@FunctionalInterface
public interface GetCartItemsServicePort {
  Flux<CartItem> getCartItems(
      String country, String requestTraceId, String customerId, String cartId);
}
