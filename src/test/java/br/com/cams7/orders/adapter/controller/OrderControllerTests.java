package br.com.cams7.orders.adapter.controller;

import static br.com.cams7.orders.adapter.repository.model.OrderModel.COLLECTION_NAME;
import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_PAYMENT_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE1;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE2;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE3;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_ADDRESS_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_CARD_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.DECLINED_PAYMENT_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.INVALID_CREATE_ORDER_REQUEST;
import static br.com.cams7.orders.template.DomainTemplateLoader.ORDER_MODEL;
import static br.com.cams7.orders.template.DomainTemplateLoader.ORDER_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.SHIPPING_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.VALID_CREATE_ORDER_REQUEST;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.ORDER_ID;
import static br.com.six2six.fixturefactory.Fixture.from;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.test.StepVerifier.create;

import br.com.cams7.orders.adapter.controller.request.CreateOrderRequest;
import br.com.cams7.orders.adapter.controller.response.OrderResponse;
import br.com.cams7.orders.adapter.repository.model.OrderModel;
import br.com.cams7.orders.adapter.webclient.response.CartItemResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerAddressResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerCardResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerResponse;
import br.com.cams7.orders.adapter.webclient.response.PaymentResponse;
import br.com.cams7.orders.adapter.webclient.response.ShippingResponse;
import br.com.cams7.orders.core.port.out.exception.ResponseStatusException;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// @Disabled("Disabled until bug has been fixed!")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient(/*timeout = "300000"*/ )
public class OrderControllerTests extends BaseIntegrationTests {

  private static final String INVALID_COUNTRY = "DO";
  private static final String PAYMENT_URL = "http://payments";
  private static final String SHIPPING_URL = "http://shippings";
  private static final String CUSTOMER_URL = "http://customers";
  private static final String ADDRESS_URL = "http://addresses";
  private static final String CARD_URL = "http://cards";
  private static final String CART_URL = "http://carts";

  private static final String PATH = "/orders";

  private static String ERRORS0_MESSAGE_ATTRIBUTE = "$.errors[0].message";
  private static String ERRORS0_FIELD_ATTRIBUTE = "$.errors[0].field";

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
        .uri(PATH)
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
        .uri(PATH)
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
        .uri(PATH)
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
        .uri(String.format("%s/{orderId}", PATH), model.getId())
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
        .uri(String.format("%s/{orderId}", PATH), ORDER_ID)
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
        .uri(String.format("%s/{orderId}", PATH), model.getId())
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
        .uri(String.format("%s/{orderId}", PATH), model.getId())
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
        .uri(String.format("%s/{orderId}", PATH), ORDER_ID)
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .isEmpty();
  }

  @Test
  @DisplayName("Should do nothing when accessing 'delete order' API and pass a invalid country")
  void shouldDoNothingWhenAccessingDeleteOrderAPIAndPassAInvalidCountry() {
    OrderModel model = from(OrderModel.class).gimme(ORDER_MODEL);

    createOrderCollection(CUSTOMER_ADDRESS_COUNTRY, model);

    testClient
        .delete()
        .uri(String.format("%s/{orderId}", PATH), model.getId())
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

  @Test
  @DisplayName("Should return created order when accessing 'create order' API and pass valid URLs")
  void shouldReturnCreatedOrderWhenAccessingCreateOrderAPIAndPassValidURLs() {
    CreateOrderRequest request = from(CreateOrderRequest.class).gimme(VALID_CREATE_ORDER_REQUEST);
    OrderResponse response = from(OrderResponse.class).gimme(ORDER_RESPONSE);

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
    ShippingResponse shippingResponse = from(ShippingResponse.class).gimme(SHIPPING_RESPONSE);

    mockGet(CUSTOMER_URL, Mono.just(customerResponse), CustomerResponse.class);
    mockGet(ADDRESS_URL, Flux.just(customerAddressResponse), CustomerAddressResponse.class);
    mockGet(CARD_URL, Flux.just(customerCardResponse), CustomerCardResponse.class);
    mockGet(CART_URL, Flux.fromIterable(cartItemsResponse), CartItemResponse.class);
    mockPost(PAYMENT_URL, Mono.just(paymentResponse), PaymentResponse.class);
    mockPost(SHIPPING_URL, Mono.just(shippingResponse), ShippingResponse.class);

    testClient
        .post()
        .uri(PATH)
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .body(BodyInserters.fromValue(request))
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody()
        .jsonPath("$.orderId")
        .isNotEmpty()
        .jsonPath("$.customer.customerId")
        .isEqualTo(response.getCustomer().getCustomerId())
        .jsonPath("$.customer.fullName")
        .isEqualTo(response.getCustomer().getFullName())
        .jsonPath("$.customer.username")
        .isEqualTo(response.getCustomer().getUsername())
        .jsonPath("$.address.addressId")
        .isEqualTo(response.getAddress().getAddressId())
        .jsonPath("$.address.number")
        .isEqualTo(response.getAddress().getNumber())
        .jsonPath("$.address.street")
        .isEqualTo(response.getAddress().getStreet())
        .jsonPath("$.address.postcode")
        .isEqualTo(response.getAddress().getPostcode())
        .jsonPath("$.address.city")
        .isEqualTo(response.getAddress().getCity())
        .jsonPath("$.address.federativeUnit")
        .isEqualTo(response.getAddress().getFederativeUnit())
        .jsonPath("$.address.country")
        .isEqualTo(response.getAddress().getCountry())
        .jsonPath("$.card.cardId")
        .isEqualTo(response.getCard().getCardId())
        .jsonPath("$.items[0].productId")
        .isEqualTo(response.getItems().get(0).getProductId())
        .jsonPath("$.items[0].quantity")
        .isEqualTo(response.getItems().get(0).getQuantity())
        .jsonPath("$.items[0].unitPrice")
        .isEqualTo(response.getItems().get(0).getUnitPrice())
        .jsonPath("$.items[1].productId")
        .isEqualTo(response.getItems().get(1).getProductId())
        .jsonPath("$.items[1].quantity")
        .isEqualTo(response.getItems().get(1).getQuantity())
        .jsonPath("$.items[1].unitPrice")
        .isEqualTo(response.getItems().get(1).getUnitPrice())
        .jsonPath("$.registrationDate")
        .isNotEmpty()
        .jsonPath("$.totalAmount")
        .isEqualTo(response.getTotalAmount());

    create(
            mongoOperations.count(
                new Query().addCriteria(where("id").exists(true)),
                OrderModel.class,
                getCollectionName(CUSTOMER_ADDRESS_COUNTRY, COLLECTION_NAME)))
        .expectSubscription()
        .expectNext(1l)
        .verifyComplete();
  }

  @Test
  @DisplayName(
      "Should return bad request status when accessing 'create order' API and decline payment")
  void shouldReturnBadRequestStatusWhenAccessingCreateOrderAPIAndDeclinePayment() {
    CreateOrderRequest request = from(CreateOrderRequest.class).gimme(VALID_CREATE_ORDER_REQUEST);

    CustomerResponse customerResponse = from(CustomerResponse.class).gimme(CUSTOMER_RESPONSE);
    CustomerAddressResponse customerAddressResponse =
        from(CustomerAddressResponse.class).gimme(CUSTOMER_ADDRESS_RESPONSE);
    CustomerCardResponse customerCardResponse =
        from(CustomerCardResponse.class).gimme(CUSTOMER_CARD_RESPONSE);
    List<CartItemResponse> cartItemsResponse =
        List.of(
            from(CartItemResponse.class).gimme(CART_ITEM_RESPONSE1),
            from(CartItemResponse.class).gimme(CART_ITEM_RESPONSE2),
            from(CartItemResponse.class).gimme(CART_ITEM_RESPONSE3));
    PaymentResponse paymentResponse = from(PaymentResponse.class).gimme(DECLINED_PAYMENT_RESPONSE);

    mockGet(CUSTOMER_URL, Mono.just(customerResponse), CustomerResponse.class);
    mockGet(ADDRESS_URL, Flux.just(customerAddressResponse), CustomerAddressResponse.class);
    mockGet(CARD_URL, Flux.just(customerCardResponse), CustomerCardResponse.class);
    mockGet(CART_URL, Flux.fromIterable(cartItemsResponse), CartItemResponse.class);
    mockPost(PAYMENT_URL, Mono.just(paymentResponse), PaymentResponse.class);

    testClient
        .post()
        .uri(PATH)
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .body(BodyInserters.fromValue(request))
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(TIMESTAMP_ATTRIBUTE)
        .isNotEmpty()
        .jsonPath(PATH_ATTRIBUTE)
        .isEqualTo(PATH)
        .jsonPath(STATUS_ATTRIBUTE)
        .isEqualTo(BAD_REQUEST_CODE)
        .jsonPath(ERROR_ATTRIBUTE)
        .isEqualTo(BAD_REQUEST_NAME)
        .jsonPath(MESSAGE_ATTRIBUTE)
        .isEqualTo(paymentResponse.getMessage())
        .jsonPath(REQUESTID_ATTRIBUTE)
        .isEqualTo(REQUEST_TRACE_ID)
        .jsonPath(EXCEPTION_ATTRIBUTE)
        .isEqualTo(ResponseStatusException.class.getName());

    create(
            mongoOperations.count(
                new Query().addCriteria(where("id").exists(true)),
                OrderModel.class,
                getCollectionName(CUSTOMER_ADDRESS_COUNTRY, COLLECTION_NAME)))
        .expectSubscription()
        .expectNext(0l)
        .verifyComplete();
  }

  @Test
  @DisplayName(
      "Should return bad request status when accessing 'create order' API and don't have cart items")
  void shouldReturnBadRequestStatusWhenAccessingCreateOrderAPIAndDoNotHaveCartItems() {
    CreateOrderRequest request = from(CreateOrderRequest.class).gimme(VALID_CREATE_ORDER_REQUEST);

    CustomerResponse customerResponse = from(CustomerResponse.class).gimme(CUSTOMER_RESPONSE);
    CustomerAddressResponse customerAddressResponse =
        from(CustomerAddressResponse.class).gimme(CUSTOMER_ADDRESS_RESPONSE);
    CustomerCardResponse customerCardResponse =
        from(CustomerCardResponse.class).gimme(CUSTOMER_CARD_RESPONSE);
    List<CartItemResponse> cartItemsResponse = List.of();

    mockGet(CUSTOMER_URL, Mono.just(customerResponse), CustomerResponse.class);
    mockGet(ADDRESS_URL, Flux.just(customerAddressResponse), CustomerAddressResponse.class);
    mockGet(CARD_URL, Flux.just(customerCardResponse), CustomerCardResponse.class);
    mockGet(CART_URL, Flux.fromIterable(cartItemsResponse), CartItemResponse.class);

    testClient
        .post()
        .uri(PATH)
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .body(BodyInserters.fromValue(request))
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(TIMESTAMP_ATTRIBUTE)
        .isNotEmpty()
        .jsonPath(PATH_ATTRIBUTE)
        .isEqualTo(PATH)
        .jsonPath(STATUS_ATTRIBUTE)
        .isEqualTo(BAD_REQUEST_CODE)
        .jsonPath(ERROR_ATTRIBUTE)
        .isEqualTo(BAD_REQUEST_NAME)
        .jsonPath(MESSAGE_ATTRIBUTE)
        .isEqualTo("There aren't items in the cart")
        .jsonPath(REQUESTID_ATTRIBUTE)
        .isEqualTo(REQUEST_TRACE_ID)
        .jsonPath(EXCEPTION_ATTRIBUTE)
        .isEqualTo(ResponseStatusException.class.getName());

    create(
            mongoOperations.count(
                new Query().addCriteria(where("id").exists(true)),
                OrderModel.class,
                getCollectionName(CUSTOMER_ADDRESS_COUNTRY, COLLECTION_NAME)))
        .expectSubscription()
        .expectNext(0l)
        .verifyComplete();
  }

  @Test
  @DisplayName(
      "Should return bad request status when accessing 'create order' API and pass some invalid URL")
  void shouldReturnBadRequestStatusWhenAccessingCreateOrderAPIAndPassSomeInvalidURL() {
    CreateOrderRequest request = from(CreateOrderRequest.class).gimme(INVALID_CREATE_ORDER_REQUEST);

    testClient
        .post()
        .uri(PATH)
        .header("country", CUSTOMER_ADDRESS_COUNTRY)
        .header("requestTraceId", REQUEST_TRACE_ID)
        .body(BodyInserters.fromValue(request))
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath(TIMESTAMP_ATTRIBUTE)
        .isNotEmpty()
        .jsonPath(PATH_ATTRIBUTE)
        .isEqualTo(PATH)
        .jsonPath(STATUS_ATTRIBUTE)
        .isEqualTo(BAD_REQUEST_CODE)
        .jsonPath(ERROR_ATTRIBUTE)
        .isEqualTo(BAD_REQUEST_NAME)
        .jsonPath(REQUESTID_ATTRIBUTE)
        .isEqualTo(REQUEST_TRACE_ID)
        .jsonPath(EXCEPTION_ATTRIBUTE)
        .isEqualTo(ConstraintViolationException.class.getName())
        .jsonPath(ERRORS0_MESSAGE_ATTRIBUTE)
        .isEqualTo("Invalid customer id")
        .jsonPath(ERRORS0_FIELD_ATTRIBUTE)
        .isEqualTo("customerId");

    create(
            mongoOperations.count(
                new Query().addCriteria(where("id").exists(true)),
                OrderModel.class,
                getCollectionName(CUSTOMER_ADDRESS_COUNTRY, COLLECTION_NAME)))
        .expectSubscription()
        .expectNext(0l)
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
