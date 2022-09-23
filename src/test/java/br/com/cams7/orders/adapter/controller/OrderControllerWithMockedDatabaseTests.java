package br.com.cams7.orders.adapter.controller;

import static br.com.cams7.orders.template.OrderEntityTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.cams7.orders.template.OrderEntityTemplate.ORDER_ID;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import br.com.cams7.orders.core.port.out.GetOrderByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.GetOrdersByCountryRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "300000")
public class OrderControllerWithMockedDatabaseTests extends BaseIntegrationTests {

  @MockBean private GetOrdersByCountryRepositoryPort getOrdersRepository;
  @MockBean private GetOrderByIdRepositoryPort getOrderByIdRepository;

  @Test
  @DisplayName(
      "Should return InternalServerError status when accessing 'get orders' API and some error happen trying to get orders in database")
  void
      shouldReturnInternalServerErrorStatusWhenAccessingGetOrdersAPIAndSomeErrorHappenTryingToGetOrdersInDatabase() {
    given(getOrdersRepository.getOrders(anyString()))
        .willReturn(Flux.error(new RuntimeException(ERROR_MESSAGE)));

    testClient
        .get()
        .uri("/orders")
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody()
        .jsonPath(TIMESTAMP_ATTRIBUTE)
        .isNotEmpty()
        .jsonPath(PATH_ATTRIBUTE)
        .isNotEmpty()
        .jsonPath(ERROR_ATTRIBUTE)
        .isEqualTo(INTERNAL_SERVER_ERROR)
        .jsonPath(REQUESTID_ATTRIBUTE)
        .isNotEmpty();

    then(getOrdersRepository).should(times(1)).getOrders(eq(CUSTOMER_ADDRESS_COUNTRY));
  }

  @Test
  @DisplayName(
      "Should return InternalServerError status when accessing 'get order' API and some error happen trying to get order in database")
  void
      shouldReturnInternalServerErrorStatusWhenAccessingGetOrderAPIAndSomeErrorHappenTryingToGetOrderInDatabase() {
    given(getOrderByIdRepository.getOrder(anyString(), anyString()))
        .willReturn(Mono.error(new RuntimeException(ERROR_MESSAGE)));

    testClient
        .get()
        .uri("/orders/{orderId}", ORDER_ID)
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody()
        .jsonPath(TIMESTAMP_ATTRIBUTE)
        .isNotEmpty()
        .jsonPath(PATH_ATTRIBUTE)
        .isNotEmpty()
        .jsonPath(ERROR_ATTRIBUTE)
        .isEqualTo(INTERNAL_SERVER_ERROR)
        .jsonPath(REQUESTID_ATTRIBUTE)
        .isNotEmpty();

    then(getOrderByIdRepository)
        .should(times(1))
        .getOrder(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID));
  }
}
