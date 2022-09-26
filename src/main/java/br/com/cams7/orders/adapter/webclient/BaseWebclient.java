package br.com.cams7.orders.adapter.webclient;

import static br.com.cams7.orders.adapter.commons.ApiConstants.COUNTRY_HEADER;
import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_HEADER;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import org.slf4j.MDC;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class BaseWebclient {

  protected static String getCountry() {
    return MDC.get(COUNTRY_HEADER);
  }

  protected static String getRequestTraceId() {
    return MDC.get(REQUEST_TRACE_ID_HEADER);
  }

  protected static WebClient getWebClient(WebClient.Builder builder, String url) {
    return builder.baseUrl(url).build();
  }

  protected static WebClient getWebClient(
      WebClient.Builder builder, String contentType, String url) {
    return builder.baseUrl(url).defaultHeader(CONTENT_TYPE, contentType).build();
  }
}
