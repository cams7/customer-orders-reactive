package br.com.cams7.orders.adapter.webclient;

import br.com.cams7.orders.adapter.webclient.response.CartItemResponse;
import br.com.cams7.orders.core.domain.CartItem;
import br.com.cams7.orders.core.port.out.GetCartItemsServicePort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CartService implements GetCartItemsServicePort {

  private final WebClient.Builder builder;
  private final ModelMapper modelMapper;

  @Override
  public Flux<CartItem> getCartItems(String itemsUrl) {
    return getWebClient(builder, itemsUrl)
        .get()
        .retrieve()
        .bodyToFlux(CartItemResponse.class)
        .map(this::getCartItem);
  }

  private static WebClient getWebClient(WebClient.Builder builder, String url) {
    return builder.baseUrl(url).build();
  }

  private CartItem getCartItem(CartItemResponse response) {
    return modelMapper.map(response, CartItem.class);
  }
}
