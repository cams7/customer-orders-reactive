package br.com.cams7.orders.adapter.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import br.com.cams7.orders.BaseTests;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class BaseWebClientTests extends BaseTests {

  @MockBean private Builder builder;

  protected <R> void mockGet(String url, Mono<R> response, Class<R> responseType) {
    var responseMock = mockGet(url);
    given(responseMock.bodyToMono(eq(responseType))).willReturn(response);
  }

  protected <R> void mockGet(String url, Flux<R> response, Class<R> responseType) {
    var responseMock = mockGet(url);
    given(responseMock.bodyToFlux(eq(responseType))).willReturn(response);
  }

  @SuppressWarnings("unchecked")
  private ResponseSpec mockGet(String url) {
    var webClientMock = mockWebClient(url);
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
  protected <R> void mockPost(String url, String uri, Mono<R> response, Class<R> responseType) {
    var webClientMock = mockWebClient(url);
    var requestBodyUriMock = mock(WebClient.RequestBodyUriSpec.class);
    var requestHeadersMock = mock(WebClient.RequestHeadersSpec.class);
    var requestBodyMock = mock(WebClient.RequestBodySpec.class);
    var responseMock = mock(WebClient.ResponseSpec.class);

    given(webClientMock.post()).willReturn(requestBodyUriMock);
    given(requestBodyUriMock.uri(eq(uri))).willReturn(requestBodyUriMock);
    given(requestBodyUriMock.header(anyString(), anyString())).willReturn(requestBodyMock);
    given(requestBodyMock.header(anyString(), anyString())).willReturn(requestBodyMock);
    given(requestBodyMock.accept(eq(APPLICATION_JSON))).willReturn(requestBodyMock);
    given(requestBodyMock.contentType(eq(APPLICATION_JSON))).willReturn(requestBodyMock);
    given(requestBodyMock.body(any(Mono.class), any(Class.class))).willReturn(requestHeadersMock);
    given(requestHeadersMock.retrieve()).willReturn(responseMock);
    given(responseMock.bodyToMono(responseType)).willReturn(response);
  }

  private WebClient mockWebClient(String url) {
    var baseUrlMock = mock(Builder.class);
    var webClientMock = mock(WebClient.class);

    given(builder.baseUrl(eq(url))).willReturn(baseUrlMock);
    given(baseUrlMock.build()).willReturn(webClientMock);

    return webClientMock;
  }
}
