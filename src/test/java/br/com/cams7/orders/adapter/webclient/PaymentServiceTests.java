package br.com.cams7.orders.adapter.webclient;

import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_PAYMENT;
import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_PAYMENT_RESPONSE;
import static br.com.cams7.orders.template.domain.CustomerTemplate.CUSTOMER_ID;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.AUTHORISED_TOTAL_AMOUNT;
import static br.com.six2six.fixturefactory.Fixture.from;
import static reactor.test.StepVerifier.create;

import br.com.cams7.orders.adapter.webclient.response.PaymentResponse;
import br.com.cams7.orders.core.domain.Payment;
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
public class PaymentServiceTests extends BaseWebClientTests {

  @InjectMocks private PaymentService paymentService;

  @Spy private ModelMapper modelMapper = new ModelMapper();

  @Test
  @DisplayName("Should return authorized payment when pass valid params")
  void shouldReturnAuthorizedPaymentWhenPassValidParams() {
    ReflectionTestUtils.setField(paymentService, "paymentUrl", "http://test/payment");

    PaymentResponse response = from(PaymentResponse.class).gimme(AUTHORISED_PAYMENT_RESPONSE);
    Payment payment = from(Payment.class).gimme(AUTHORISED_PAYMENT);

    mockWebClientForPost(Mono.just(response), PaymentResponse.class);

    create(paymentService.verify(CUSTOMER_ID, AUTHORISED_TOTAL_AMOUNT))
        .expectSubscription()
        .expectNext(payment)
        .verifyComplete();
  }
}
