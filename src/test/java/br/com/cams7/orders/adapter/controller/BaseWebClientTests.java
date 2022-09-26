package br.com.cams7.orders.adapter.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import br.com.cams7.orders.BaseTests;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class BaseWebClientTests extends BaseTests {

  @MockBean private Builder builder;

  protected <R> void mockWebClientForGet(String url, Mono<R> response, Class<R> responseType) {
    var responseMock = getWebClientMockForGet(url);
    given(responseMock.bodyToMono(responseType)).willReturn(response);
  }

  protected <R> void mockWebClientForGet(String url, Flux<R> response, Class<R> responseType) {
    var responseMock = getWebClientMockForGet(url);
    given(responseMock.bodyToFlux(responseType)).willReturn(response);
  }

  @SuppressWarnings("unchecked")
  private ResponseSpec getWebClientMockForGet(String url) {
    var webClientMock = getWebClientMock(url, false);
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
  protected void mockWebClientForDelete(String url) {
    var webClientMock = getWebClientMock(url, false);
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
  protected <R> void mockWebClientForPost(String url, Mono<R> response, Class<R> responseType) {
    var webClientMock = getWebClientMock(url, true);
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

  private WebClient getWebClientMock(String url, boolean hasDefaultHeader) {
    var builderMock = mock(Builder.class);
    var webClientMock = mock(WebClient.class);

    given(builder.baseUrl(url)).willReturn(builderMock);
    if (hasDefaultHeader)
      given(builderMock.defaultHeader(anyString(), anyString())).willReturn(builderMock);
    given(builderMock.build()).willReturn(webClientMock);

    return webClientMock;
  }
}
