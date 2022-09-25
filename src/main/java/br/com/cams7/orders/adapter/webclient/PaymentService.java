package br.com.cams7.orders.adapter.webclient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.com.cams7.orders.adapter.webclient.request.PaymentRequest;
import br.com.cams7.orders.adapter.webclient.response.PaymentResponse;
import br.com.cams7.orders.core.domain.Payment;
import br.com.cams7.orders.core.port.out.VerifyPaymentServicePort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentService implements VerifyPaymentServicePort {

  private final WebClient.Builder builder;
  private final ModelMapper modelMapper;

  @Value("${api.paymentUrl}")
  private String paymentUrl;

  @Override
  public Mono<Payment> verify(String customerId, Float amount) {
    return getWebClient(builder, paymentUrl)
        .post()
        .body(Mono.just(new PaymentRequest(customerId, amount)), PaymentRequest.class)
        .retrieve()
        .bodyToMono(PaymentResponse.class)
        .map(this::getPayment);
  }

  private static WebClient getWebClient(WebClient.Builder builder, String url) {
    return builder.baseUrl(url).defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE).build();
  }

  private Payment getPayment(PaymentResponse response) {
    return modelMapper.map(response, Payment.class);
  }
}
