package br.com.cams7.orders.adapter.webclient;

import static br.com.cams7.orders.template.DomainTemplateLoader.SHIPPING_RESPONSE;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.ORDER_ID;
import static br.com.six2six.fixturefactory.Fixture.from;
import static reactor.test.StepVerifier.create;

import br.com.cams7.orders.adapter.webclient.response.ShippingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class ShippingServiceTests extends BaseWebClientTests {

  @InjectMocks private ShippingService shippingService;

  @Spy private ModelMapper modelMapper = new ModelMapper();

  @Test
  @DisplayName("Should save shipping when pass valid params")
  void shouldSaveShippingWhenPassValidParams() {
    ReflectionTestUtils.setField(shippingService, "shippingUrl", "http://test/shipping");

    ShippingResponse response = from(ShippingResponse.class).gimme(SHIPPING_RESPONSE);

    mockWebClientForPost(Mono.just(response), ShippingResponse.class);

    create(shippingService.add(ORDER_ID))
        .expectSubscription()
        .expectNext(response.getId())
        .verifyComplete();
  }
}
