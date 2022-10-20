package br.com.cams7.orders.adapter.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import br.com.cams7.orders.BaseTests;
import java.util.function.Function;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class BaseWebClientTests extends BaseTests {

  @MockBean private Builder builder;

  protected <R> void mockGet(
      final String url, final Mono<R> response, final Class<R> responseType) {
    final var responseMock = mockGet(url);
    given(responseMock.bodyToMono(eq(responseType))).willReturn(response);
  }

  protected <R> void mockGet(
      final String url, final Flux<R> response, final Class<R> responseType) {
    final var responseMock = mockGet(url);
    given(responseMock.bodyToFlux(eq(responseType))).willReturn(response);
  }

  @SuppressWarnings("unchecked")
  private ResponseSpec mockGet(final String url) {
    final var webClientMock = mockWebClient(url);
    final var requestHeadersUriMock = mock(WebClient.RequestHeadersUriSpec.class);
    final var requestHeadersMock = mock(WebClient.RequestHeadersSpec.class);
    final var responseMock = mock(WebClient.ResponseSpec.class);

    given(webClientMock.get()).willReturn(requestHeadersUriMock);
    given(requestHeadersUriMock.uri(any(Function.class))).willReturn(requestHeadersUriMock);
    given(requestHeadersUriMock.header(anyString(), anyString())).willReturn(requestHeadersMock);
    given(requestHeadersMock.header(anyString(), anyString())).willReturn(requestHeadersMock);
    given(requestHeadersMock.retrieve()).willReturn(responseMock);
    return responseMock;
  }

  @SuppressWarnings("unchecked")
  protected <R> void mockPost(
      final String url, final Mono<R> response, final Class<R> responseType) {
    final var webClientMock = mockWebClient(url);
    final var requestBodyUriMock = mock(WebClient.RequestBodyUriSpec.class);
    final var requestHeadersMock = mock(WebClient.RequestHeadersSpec.class);
    final var requestBodyMock = mock(WebClient.RequestBodySpec.class);
    final var responseMock = mock(WebClient.ResponseSpec.class);

    given(webClientMock.post()).willReturn(requestBodyUriMock);
    given(requestBodyUriMock.uri(anyString())).willReturn(requestBodyUriMock);
    given(requestBodyUriMock.header(anyString(), anyString())).willReturn(requestBodyMock);
    given(requestBodyMock.header(anyString(), anyString())).willReturn(requestBodyMock);
    given(requestBodyMock.accept(eq(APPLICATION_JSON))).willReturn(requestBodyMock);
    given(requestBodyMock.contentType(eq(APPLICATION_JSON))).willReturn(requestBodyMock);
    given(requestBodyMock.body(any(Mono.class), any(Class.class))).willReturn(requestHeadersMock);
    given(requestHeadersMock.retrieve()).willReturn(responseMock);
    given(responseMock.bodyToMono(responseType)).willReturn(response);
  }

  private WebClient mockWebClient(final String url) {
    final var baseUrlMock = mock(Builder.class);
    final var webClientMock = mock(WebClient.class);

    given(builder.baseUrl(eq(url))).willReturn(baseUrlMock);
    given(baseUrlMock.build()).willReturn(webClientMock);

    return webClientMock;
  }
}
