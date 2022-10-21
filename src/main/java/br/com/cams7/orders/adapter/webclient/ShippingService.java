package br.com.cams7.orders.adapter.webclient;

import static br.com.cams7.orders.adapter.commons.ApiConstants.COUNTRY_HEADER;
import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import br.com.cams7.orders.adapter.webclient.request.ShippingRequest;
import br.com.cams7.orders.adapter.webclient.response.ShippingResponse;
import br.com.cams7.orders.core.port.out.AddShippingOrderServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ShippingService extends BaseWebclient implements AddShippingOrderServicePort {

  private final WebClient.Builder builder;

  @Value("${api.shippingUrl}")
  private String shippingUrl;

  @Override
  public Mono<String> add(final String country, final String requestTraceId, final String orderId) {
    return getWebClient(builder, shippingUrl)
        .post()
        .uri("/shippings")
        .header(COUNTRY_HEADER, country)
        .header(REQUEST_TRACE_ID_HEADER, requestTraceId)
        .accept(APPLICATION_JSON)
        .contentType(APPLICATION_JSON)
        .body(Mono.just(new ShippingRequest(orderId)), ShippingRequest.class)
        .retrieve()
        .bodyToMono(ShippingResponse.class)
        .map(shipping -> shipping.getId());
  }
}
