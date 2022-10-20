package br.com.cams7.orders.adapter.controller;

import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_ORDER_ENTITY;
import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_PAYMENT_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE1;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE3;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_ADDRESS_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_CARD_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.VALID_CREATE_ORDER_REQUEST;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.ORDER_ID;
import static br.com.six2six.fixturefactory.Fixture.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import br.com.cams7.orders.adapter.controller.request.CreateOrderRequest;
import br.com.cams7.orders.adapter.webclient.response.CartItemResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerAddressResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerCardResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerResponse;
import br.com.cams7.orders.adapter.webclient.response.PaymentResponse;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.out.CreateOrderRepositoryPort;
import br.com.cams7.orders.core.port.out.DeleteOrderByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.GetOrderByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.GetOrdersByCountryRepositoryPort;
import br.com.cams7.orders.core.port.out.UpdateShippingByIdRepositoryPort;
import java.util.List;
// import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// @Disabled("Disabled until bug has been fixed!")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient(/*timeout = "300000"*/ )
public class OrderControllerWithMockedDatabaseTests extends BaseIntegrationTests {

  private static final String PAYMENT_URL = "http://payments";
  private static final String CUSTOMER_URL = "http://customers";
  private static final String ADDRESS_URL = "http://addresses";
  private static final String CARD_URL = "http://cards";
  private static final String CART_URL = "http://carts";

  private static final String PATH = "/orders";

  @MockBean private GetOrdersByCountryRepositoryPort getOrdersRepository;
  @MockBean private GetOrderByIdRepositoryPort getOrderByIdRepository;
  @MockBean private DeleteOrderByIdRepositoryPort deleteOrderByIdRepository;
  @MockBean private CreateOrderRepositoryPort createOrderRepository;
  @MockBean private UpdateShippingByIdRepositoryPort updateShippingByIdRepository;

  @Captor private ArgumentCaptor<OrderEntity> orderEntityCaptor;

  @Test
  @DisplayName(
      "Should return InternalServerError status when accessing 'get orders' API and some error happen trying to get orders in database")
  void
      shouldReturnInternalServerErrorStatusWhenAccessingGetOrdersAPIAndSomeErrorHappenTryingToGetOrdersInDatabase() {
    given(getOrdersRepository.getOrders(anyString()))
        .willReturn(Flux.error(new RuntimeException(ERROR_MESSAGE)));

    testClient
        .get()
        .uri(PATH)
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody()
        .jsonPath(TIMESTAMP_ATTRIBUTE)
        .isNotEmpty()
        .jsonPath(PATH_ATTRIBUTE)
        .isEqualTo(PATH)
        .jsonPath(STATUS_ATTRIBUTE)
        .isEqualTo(INTERNAL_SERVER_ERROR_CODE)
        .jsonPath(ERROR_ATTRIBUTE)
        .isEqualTo(INTERNAL_SERVER_ERROR_NAME)
        .jsonPath(REQUESTID_ATTRIBUTE)
        .isEqualTo(REQUEST_TRACE_ID);

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
        .uri(String.format("%s/{orderId}", PATH), ORDER_ID)
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody()
        .jsonPath(TIMESTAMP_ATTRIBUTE)
        .isNotEmpty()
        .jsonPath(PATH_ATTRIBUTE)
        .isEqualTo(String.format("%s/%s", PATH, ORDER_ID))
        .jsonPath(STATUS_ATTRIBUTE)
        .isEqualTo(INTERNAL_SERVER_ERROR_CODE)
        .jsonPath(ERROR_ATTRIBUTE)
        .isEqualTo(INTERNAL_SERVER_ERROR_NAME)
        .jsonPath(REQUESTID_ATTRIBUTE)
        .isEqualTo(REQUEST_TRACE_ID);

    then(getOrderByIdRepository)
        .should(times(1))
        .getOrder(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID));
  }

  @Test
  @DisplayName(
      "Should return InternalServerError status when accessing 'delete order' API and some error happen trying to delete order in database")
  void
      shouldReturnInternalServerErrorStatusWhenAccessingDeleteOrderAPIAndSomeErrorHappenTryingToDeleteOrderInDatabase() {
    given(deleteOrderByIdRepository.delete(anyString(), anyString()))
        .willReturn(Mono.error(new RuntimeException(ERROR_MESSAGE)));

    testClient
        .delete()
        .uri(String.format("%s/{orderId}", PATH), ORDER_ID)
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody()
        .jsonPath(TIMESTAMP_ATTRIBUTE)
        .isNotEmpty()
        .jsonPath(PATH_ATTRIBUTE)
        .isEqualTo(String.format("%s/%s", PATH, ORDER_ID))
        .jsonPath(STATUS_ATTRIBUTE)
        .isEqualTo(INTERNAL_SERVER_ERROR_CODE)
        .jsonPath(ERROR_ATTRIBUTE)
        .isEqualTo(INTERNAL_SERVER_ERROR_NAME)
        .jsonPath(REQUESTID_ATTRIBUTE)
        .isEqualTo(REQUEST_TRACE_ID);

    then(deleteOrderByIdRepository)
        .should(times(1))
        .delete(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID));
  }

  @Test
  @DisplayName(
      "Should return InternalServerError status when accessing 'create order' API and some error happen trying to create order in database")
  void
      shouldReturnInternalServerErrorStatusWhenAccessingCreateOrderAPIAndSomeErrorHappenTryingToCreateOrderInDatabase() {
    OrderEntity order = from(OrderEntity.class).gimme(AUTHORISED_ORDER_ENTITY);
    CreateOrderRequest request = from(CreateOrderRequest.class).gimme(VALID_CREATE_ORDER_REQUEST);

    CustomerResponse customerResponse = from(CustomerResponse.class).gimme(CUSTOMER_RESPONSE);
    CustomerAddressResponse customerAddressResponse =
        from(CustomerAddressResponse.class).gimme(CUSTOMER_ADDRESS_RESPONSE);
    CustomerCardResponse customerCardResponse =
        from(CustomerCardResponse.class).gimme(CUSTOMER_CARD_RESPONSE);
    List<CartItemResponse> cartItemsResponse =
        List.of(
            from(CartItemResponse.class).gimme(CART_ITEM_RESPONSE1),
            from(CartItemResponse.class).gimme(CART_ITEM_RESPONSE3));
    PaymentResponse paymentResponse =
        from(PaymentResponse.class).gimme(AUTHORISED_PAYMENT_RESPONSE);

    mockGet(CUSTOMER_URL, Mono.just(customerResponse), CustomerResponse.class);
    mockGet(ADDRESS_URL, Flux.just(customerAddressResponse), CustomerAddressResponse.class);
    mockGet(CARD_URL, Flux.just(customerCardResponse), CustomerCardResponse.class);
    mockGet(CART_URL, Flux.fromIterable(cartItemsResponse), CartItemResponse.class);
    mockPost(PAYMENT_URL, Mono.just(paymentResponse), PaymentResponse.class);

    given(createOrderRepository.create(anyString(), any(OrderEntity.class)))
        .willReturn(Mono.error(new RuntimeException(ERROR_MESSAGE)));

    testClient
        .post()
        .uri(PATH)
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .body(BodyInserters.fromValue(request))
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody()
        .jsonPath(TIMESTAMP_ATTRIBUTE)
        .isNotEmpty()
        .jsonPath(PATH_ATTRIBUTE)
        .isEqualTo(PATH)
        .jsonPath(STATUS_ATTRIBUTE)
        .isEqualTo(INTERNAL_SERVER_ERROR_CODE)
        .jsonPath(ERROR_ATTRIBUTE)
        .isEqualTo(INTERNAL_SERVER_ERROR_NAME)
        .jsonPath(REQUESTID_ATTRIBUTE)
        .isEqualTo(REQUEST_TRACE_ID);

    then(createOrderRepository)
        .should(times(1))
        .create(eq(CUSTOMER_ADDRESS_COUNTRY), orderEntityCaptor.capture());
    var capturedOrder = orderEntityCaptor.getValue();

    assertThat(capturedOrder.getCustomer()).isEqualTo(order.getCustomer());
    assertThat(capturedOrder.getAddress()).isEqualTo(order.getAddress());
    assertThat(capturedOrder.getCard()).isEqualTo(order.getCard());
    assertThat(capturedOrder.getItems()).isEqualTo(order.getItems());
    assertThat(capturedOrder.getPayment()).isEqualTo(order.getPayment());
    assertThat(capturedOrder.getTotalAmount()).isEqualTo(order.getTotalAmount());
    assertThat(capturedOrder.getOrderId()).isNull();
    assertThat(capturedOrder.getRegistrationDate()).isNotNull();
  }
}
