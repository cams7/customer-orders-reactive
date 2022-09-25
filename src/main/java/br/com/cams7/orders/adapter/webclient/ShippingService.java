package br.com.cams7.orders.adapter.webclient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
public class ShippingService implements AddShippingOrderServicePort {

  private final WebClient.Builder builder;

  @Value("${api.shippingUrl}")
  private String shippingUrl;

  @Override
  public Mono<String> add(String orderId) {
    return getWebClient(builder, shippingUrl)
        .post()
        .body(Mono.just(new ShippingRequest(orderId)), ShippingRequest.class)
        .retrieve()
        .bodyToMono(ShippingResponse.class)
        .map(shipping -> shipping.getId());
  }

  private static WebClient getWebClient(WebClient.Builder builder, String url) {
    return builder.baseUrl(url).defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE).build();
  }
}
