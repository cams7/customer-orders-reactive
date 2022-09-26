package br.com.cams7.orders.adapter.controller;

import static br.com.cams7.orders.adapter.repository.model.OrderModel.COLLECTION_NAME;
import static br.com.cams7.orders.template.DomainTemplateLoader.ORDER_MODEL;
import static br.com.cams7.orders.template.DomainTemplateLoader.ORDER_RESPONSE;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.ORDER_ID;
import static br.com.six2six.fixturefactory.Fixture.from;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.test.StepVerifier.create;

import br.com.cams7.orders.adapter.controller.response.OrderResponse;
import br.com.cams7.orders.adapter.repository.model.OrderModel;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Query;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "300000")
public class OrderControllerTests extends BaseIntegrationTests {

  private static final String INVALID_COUNTRY = "DO";

  @AfterEach
  void dropCollection() {
    dropCollection(CUSTOMER_ADDRESS_COUNTRY, COLLECTION_NAME);
  }

  @Test
  @DisplayName("Should return orders when accessing 'get orders' API and pass a valid country")
  void shouldReturnOrdersWhenAccessingGetOrdersAPIAndPassAValidCountry() {
    OrderModel model = from(OrderModel.class).gimme(ORDER_MODEL);
    OrderResponse response = from(OrderResponse.class).gimme(ORDER_RESPONSE);

    createOrderCollection(CUSTOMER_ADDRESS_COUNTRY, model);

    testClient
        .get()
        .uri("/orders")
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(OrderResponse.class)
        .isEqualTo(List.of(response));
  }

  @Test
  @DisplayName(
      "Should return empty list when accessing 'get orders' API and doesn't have any orders")
  void shouldReturnEmptyListWhenAccessingGetOrdersAPIAndDoesNotHaveAnyOrders() {
    testClient
        .get()
        .uri("/orders")
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(OrderResponse.class)
        .hasSize(0);
  }

  @Test
  @DisplayName(
      "Should return empty list when accessing 'get orders' API and when pass a invalid country")
  void shouldReturnEmptyListWhenAccessingGetOrdersAPIAndPassAInvalidCountry() {
    OrderModel model = from(OrderModel.class).gimme(ORDER_MODEL);

    createOrderCollection(CUSTOMER_ADDRESS_COUNTRY, model);

    testClient
        .get()
        .uri("/orders")
        .header("country", INVALID_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(OrderResponse.class)
        .hasSize(0);
  }

  @Test
  @DisplayName("Should return order when accessing 'get order' API and pass a valid order id")
  void shouldReturnOrderWhenAccessingGetOrderAPIAndPassAValidOrderId() {
    OrderModel model = from(OrderModel.class).gimme(ORDER_MODEL);
    OrderResponse response = from(OrderResponse.class).gimme(ORDER_RESPONSE);

    createOrderCollection(CUSTOMER_ADDRESS_COUNTRY, model);

    testClient
        .get()
        .uri("/orders/{orderId}", model.getId())
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(OrderResponse.class)
        .isEqualTo(response);
  }

  @Test
  @DisplayName("Should return empty when accessing 'get order' API and doesn't have any order")
  void shouldReturnEmptyWhenAccessingGetOrderAPIAndDoesNotHaveOrder() {
    testClient
        .get()
        .uri("/orders/{orderId}", ORDER_ID)
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .isEmpty();
  }

  @Test
  @DisplayName("Should return empty when accessing 'get order' API and when pass a invalid country")
  void shouldReturnEmptyWhenAccessingGetOrderAPIAndPassAInvalidCountry() {
    OrderModel model = from(OrderModel.class).gimme(ORDER_MODEL);

    createOrderCollection(CUSTOMER_ADDRESS_COUNTRY, model);

    testClient
        .get()
        .uri("/orders/{orderId}", model.getId())
        .header("country", INVALID_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .isEmpty();
  }

  @Test
  @DisplayName("Should delete order when accessing 'delete order' API and pass a valid order id")
  void shouldDeleteOrderWhenAccessingDeleteOrderAPIAndPassAValidOrderId() {
    OrderModel model = from(OrderModel.class).gimme(ORDER_MODEL);

    createOrderCollection(CUSTOMER_ADDRESS_COUNTRY, model);

    testClient
        .delete()
        .uri("/orders/{orderId}", model.getId())
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .isEmpty();

    create(
            mongoOperations.find(
                new Query().addCriteria(where("id").is(model.getId())),
                OrderModel.class,
                getCollectionName(CUSTOMER_ADDRESS_COUNTRY, COLLECTION_NAME)))
        .expectSubscription()
        .expectNextCount(0)
        .verifyComplete();
  }

  @Test
  @DisplayName("Should do nothing when accessing 'delete order' API and doesn't have any order")
  void shouldDoNothingWhenAccessingDeleteOrderAPIAndDoesNotHaveOrder() {
    testClient
        .delete()
        .uri("/orders/{orderId}", ORDER_ID)
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .isEmpty();
  }

  @Test
  @DisplayName(
      "Should do nothing when accessing 'delete order' API and when pass a invalid country")
  void shouldDoNothingWhenAccessingDeleteOrderAPIAndPassAInvalidCountry() {
    OrderModel model = from(OrderModel.class).gimme(ORDER_MODEL);

    createOrderCollection(CUSTOMER_ADDRESS_COUNTRY, model);

    testClient
        .delete()
        .uri("/orders/{orderId}", model.getId())
        .header("country", INVALID_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .isEmpty();

    create(
            mongoOperations.find(
                new Query().addCriteria(where("id").is(model.getId())),
                OrderModel.class,
                getCollectionName(CUSTOMER_ADDRESS_COUNTRY, COLLECTION_NAME)))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }

  private void createOrderCollection(String country, OrderModel order) {
    var collectionName = getCollectionName(country, COLLECTION_NAME);
    create(mongoOperations.insert(order, collectionName))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }
}
