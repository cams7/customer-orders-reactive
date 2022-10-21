package br.com.cams7.orders.adapter.webclient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

public abstract class BaseWebclient {

  @Value("${connectionProvider.maxConnections}")
  private Integer maxConnections;

  @Value("${connectionProvider.maxIdleTimeInSeconds}")
  private Integer maxIdleTimeInSeconds;

  @Value("${connectionProvider.maxLifeTimeInSeconds}")
  private Integer maxLifeTimeInSeconds;

  @Value("${httpClient.connectTimeoutInMillis}")
  private Integer connectTimeoutInMillis;

  @Value("${httpClient.responseTimeoutInSeconds}")
  private Integer responseTimeoutInSeconds;

  @Value("${httpClient.readTimeoutInSeconds}")
  private Integer readTimeoutInSeconds;

  @Value("${builder.addClientConnector}")
  private Boolean addClientConnector;

  protected WebClient getWebClient(final Builder builder, final String url) {
    return getBuilder(builder, url).build();
  }

  private Builder getBuilder(Builder builder, final String url) {
    final var provider =
        ConnectionProvider.builder("customer-orders")
            .maxConnections(maxConnections)
            .maxIdleTime(Duration.ofSeconds(maxIdleTimeInSeconds))
            .maxLifeTime(Duration.ofSeconds(maxLifeTimeInSeconds))
            .build();

    final var httpClient =
        HttpClient.create(provider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutInMillis)
            .wiretap(true)
            .responseTimeout(Duration.ofSeconds(responseTimeoutInSeconds))
            .doOnConnected(
                connection ->
                    connection.addHandlerFirst(
                        new ReadTimeoutHandler(readTimeoutInSeconds, TimeUnit.SECONDS)));

    final var connector = new ReactorClientHttpConnector(httpClient);

    if (addClientConnector) builder = builder.clientConnector(connector);

    return builder.baseUrl(url);
  }
}
