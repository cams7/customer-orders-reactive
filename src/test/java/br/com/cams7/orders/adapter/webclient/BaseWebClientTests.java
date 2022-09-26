package br.com.cams7.orders.adapter.webclient;

import static br.com.cams7.orders.adapter.commons.ApiConstants.COUNTRY_HEADER;
import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_HEADER;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import br.com.cams7.orders.BaseTests;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.slf4j.MDC;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class BaseWebClientTests extends BaseTests {
  protected static final String REQUEST_TRACE_ID = "123";

  @Mock private Builder builder;

  @BeforeEach
  void addRequestTraceAndCountry() {
    MDC.put(REQUEST_TRACE_ID_HEADER, REQUEST_TRACE_ID);
    MDC.put(COUNTRY_HEADER, CUSTOMER_ADDRESS_COUNTRY);
  }

  protected <R> void mockWebClientForGet(Mono<R> response, Class<R> responseType) {
    var responseMock = getWebClientMockForGet();
    given(responseMock.bodyToMono(responseType)).willReturn(response);
  }

  protected <R> void mockWebClientForGet(Flux<R> response, Class<R> responseType) {
    var responseMock = getWebClientMockForGet();
    given(responseMock.bodyToFlux(responseType)).willReturn(response);
  }

  @SuppressWarnings("unchecked")
  private ResponseSpec getWebClientMockForGet() {
    var webClientMock = getWebClientMock(false);
    var requestHeadersUriMock = mock(WebClient.RequestHeadersUriSpec.class);
    var requestHeadersMock = mock(WebClient.RequestHeadersSpec.class);
    var responseMock = mock(WebClient.ResponseSpec.class);

    given(webClientMock.get()).willReturn(requestHeadersUriMock);
    given(requestHeadersUriMock.header(anyString(), anyString())).willReturn(requestHeadersMock);
    given(requestHeadersMock.header(anyString(), anyString())).willReturn(requestHeadersMock);
    given(requestHeadersMock.retrieve()).willReturn(responseMock);
    return responseMock;
  }

  @SuppressWarnings("unchecked")
  protected void mockWebClientForDelete() {
    var webClientMock = getWebClientMock(false);
    var requestHeadersUriMock = mock(WebClient.RequestHeadersUriSpec.class);
    var requestHeadersMock = mock(WebClient.RequestHeadersSpec.class);
    var responseMock = mock(WebClient.ResponseSpec.class);

    given(webClientMock.delete()).willReturn(requestHeadersUriMock);
    given(requestHeadersUriMock.header(anyString(), anyString())).willReturn(requestHeadersMock);
    given(requestHeadersMock.header(anyString(), anyString())).willReturn(requestHeadersMock);
    given(requestHeadersMock.retrieve()).willReturn(responseMock);
    given(responseMock.bodyToMono(String.class)).willReturn(Mono.just("OK"));
  }

  @SuppressWarnings("unchecked")
  protected <R> void mockWebClientForPost(Mono<R> response, Class<R> responseType) {
    var webClientMock = getWebClientMock(true);
    var requestBodyUriMock = mock(WebClient.RequestBodyUriSpec.class);
    var requestHeadersMock = mock(WebClient.RequestHeadersSpec.class);
    var requestBodyMock = mock(WebClient.RequestBodySpec.class);
    var responseMock = mock(WebClient.ResponseSpec.class);

    given(webClientMock.post()).willReturn(requestBodyUriMock);
    given(requestBodyUriMock.header(anyString(), anyString())).willReturn(requestBodyMock);
    given(requestBodyMock.header(anyString(), anyString())).willReturn(requestBodyMock);
    given(requestBodyMock.body(any(Mono.class), any(Class.class))).willReturn(requestHeadersMock);
    given(requestHeadersMock.retrieve()).willReturn(responseMock);
    given(responseMock.bodyToMono(responseType)).willReturn(response);
  }

  private WebClient getWebClientMock(boolean hasDefaultHeader) {
    var builderMock = mock(Builder.class);
    var webClientMock = mock(WebClient.class);

    given(builder.baseUrl(anyString())).willReturn(builderMock);
    if (hasDefaultHeader)
      given(builderMock.defaultHeader(anyString(), anyString())).willReturn(builderMock);
    given(builderMock.build()).willReturn(webClientMock);

    return webClientMock;
  }
}
