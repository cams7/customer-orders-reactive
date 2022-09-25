package br.com.cams7.orders.adapter.configuration;

import static br.com.cams7.orders.adapter.commons.ApiConstants.COUNTRY_HEADER;
import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_HEADER;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.time.Instant;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Log4j2
@Component
public class TraceFilter implements WebFilter {
  private static final String EMPTY = "";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    var start = Instant.now();
    var headers = exchange.getRequest().getHeaders().toSingleValueMap();
    var requestTraceId = getRequestTraceId(headers);
    var country = getCountry(headers);

    return chain
        .filter(exchange)
        .contextWrite(
            context -> {
              createTrace(requestTraceId, country);

              // simple hack to provide the context with the exchange, so the whole chain can get
              // the same trace id
              Context contextTmp =
                  context.put(REQUEST_TRACE_ID_HEADER, requestTraceId).put(COUNTRY_HEADER, country);
              var attributes = exchange.getAttributes();
              attributes.put(REQUEST_TRACE_ID_HEADER, requestTraceId);
              attributes.put(COUNTRY_HEADER, country);

              return contextTmp;
            })
        .doFinally(
            signalType -> {
              var totalTime = MILLIS.between(start, Instant.now());
              exchange.getAttributes().put("totalTime", totalTime);
              createTrace(requestTraceId, country);
              log.info("The request spent {} millis to be completed", totalTime);
            });
  }

  private static void createTrace(String requestTraceId, String country) {
    if (!isEmpty(requestTraceId)) MDC.put(REQUEST_TRACE_ID_HEADER, requestTraceId);
    if (!isEmpty(country)) MDC.put(COUNTRY_HEADER, country);
  }

  private static String getRequestTraceId(Map<String, String> headers) {
    var requestTraceId = EMPTY;
    if (headers.containsKey(REQUEST_TRACE_ID_HEADER))
      requestTraceId = headers.get(REQUEST_TRACE_ID_HEADER);
    return requestTraceId;
  }

  private static String getCountry(Map<String, String> headers) {
    var country = EMPTY;
    if (headers.containsKey(COUNTRY_HEADER)) country = headers.get(COUNTRY_HEADER);
    return country;
  }
}
