package br.com.cams7.orders.adapter.webclient;

import static br.com.cams7.orders.adapter.commons.ApiConstants.COUNTRY_HEADER;
import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_HEADER;
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
public class PaymentService extends BaseWebclient implements VerifyPaymentServicePort {

  private final WebClient.Builder builder;
  private final ModelMapper modelMapper;

  @Value("${api.paymentUrl}")
  private String paymentUrl;

  @Override
  public Mono<Payment> verify(String customerId, Float amount) {
    return getWebClient(builder, APPLICATION_JSON_VALUE, paymentUrl)
        .post()
        .header(COUNTRY_HEADER, getCountry())
        .header(REQUEST_TRACE_ID_HEADER, getRequestTraceId())
        .body(Mono.just(new PaymentRequest(customerId, amount)), PaymentRequest.class)
        .retrieve()
        .bodyToMono(PaymentResponse.class)
        .map(this::getPayment);
  }

  private Payment getPayment(PaymentResponse response) {
    return modelMapper.map(response, Payment.class);
  }
}
