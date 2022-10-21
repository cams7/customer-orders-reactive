package br.com.cams7.orders.adapter.webclient;

import static br.com.cams7.orders.adapter.commons.ApiConstants.COUNTRY_HEADER;
import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_HEADER;

import br.com.cams7.orders.adapter.webclient.response.CartItemResponse;
import br.com.cams7.orders.core.domain.CartItem;
import br.com.cams7.orders.core.port.out.GetCartItemsServicePort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CartService extends BaseWebclient implements GetCartItemsServicePort {

  private final WebClient.Builder builder;
  private final ModelMapper modelMapper;

  @Value("${api.cartUrl}")
  private String cartUrl;

  @Override
  public Flux<CartItem> getCartItems(
      final String country,
      final String requestTraceId,
      final String customerId,
      final String cartId) {
    return getWebClient(builder, cartUrl)
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/items")
                    .queryParam("customerId", customerId)
                    .queryParam("cartId", cartId)
                    .build())
        .header(COUNTRY_HEADER, country)
        .header(REQUEST_TRACE_ID_HEADER, requestTraceId)
        .retrieve()
        .bodyToFlux(CartItemResponse.class)
        .map(this::getCartItem);
  }

  private CartItem getCartItem(final CartItemResponse response) {
    return modelMapper.map(response, CartItem.class);
  }
}
