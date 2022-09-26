package br.com.cams7.orders.adapter.webclient;

import static br.com.cams7.orders.template.CreateOrderRequestTemplate.ITEMS_URL;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM1;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM2;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM3;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE1;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE2;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE3;
import static br.com.six2six.fixturefactory.Fixture.from;
import static reactor.test.StepVerifier.create;

import br.com.cams7.orders.adapter.webclient.response.CartItemResponse;
import br.com.cams7.orders.core.domain.CartItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
public class CartServiceTests extends BaseWebClientTests {

  @InjectMocks private CartService cartService;

  @Spy private ModelMapper modelMapper = new ModelMapper();

  @Test
  @DisplayName("Should return cart items when pass valid URL")
  void shouldReturnCartItemsWhenPassValidURL() {
    List<CartItemResponse> response =
        List.of(
            from(CartItemResponse.class).gimme(CART_ITEM_RESPONSE1),
            from(CartItemResponse.class).gimme(CART_ITEM_RESPONSE2),
            from(CartItemResponse.class).gimme(CART_ITEM_RESPONSE3));
    List<CartItem> cartItems =
        List.of(
            from(CartItem.class).gimme(CART_ITEM1),
            from(CartItem.class).gimme(CART_ITEM2),
            from(CartItem.class).gimme(CART_ITEM3));

    mockWebClientForGet(Flux.fromIterable(response), CartItemResponse.class);

    create(cartService.getCartItems(ITEMS_URL))
        .expectSubscription()
        .expectNextSequence(cartItems)
        .verifyComplete();
  }
}
