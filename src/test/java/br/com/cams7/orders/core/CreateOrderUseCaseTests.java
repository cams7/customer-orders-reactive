package br.com.cams7.orders.core;

import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.BAD_REQUEST_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.INTERNAL_SERVER_ERROR_CODE;
import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_ORDER_ENTITY;
import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_PAYMENT;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM1;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM2;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM3;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_ADDRESS;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_CARD;
import static br.com.cams7.orders.template.DomainTemplateLoader.DECLINED_PAYMENT;
import static br.com.cams7.orders.template.DomainTemplateLoader.VALID_CREATE_ORDER_REQUEST;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.cams7.orders.template.domain.CustomerTemplate.CUSTOMER_ID;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.AUTHORISED_TOTAL_AMOUNT;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.DECLINED_TOTAL_AMOUNT;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.ORDER_ID;
import static br.com.six2six.fixturefactory.Fixture.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static reactor.test.StepVerifier.create;

import br.com.cams7.orders.BaseTests;
import br.com.cams7.orders.adapter.controller.request.CreateOrderRequest;
import br.com.cams7.orders.core.domain.CartItem;
import br.com.cams7.orders.core.domain.Customer;
import br.com.cams7.orders.core.domain.CustomerAddress;
import br.com.cams7.orders.core.domain.CustomerCard;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.domain.Payment;
import br.com.cams7.orders.core.port.in.params.CreateOrderCommand;
import br.com.cams7.orders.core.port.out.AddShippingOrderServicePort;
import br.com.cams7.orders.core.port.out.CreateOrderRepositoryPort;
import br.com.cams7.orders.core.port.out.GetCartItemsServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerAddressServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerCardServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerServicePort;
import br.com.cams7.orders.core.port.out.UpdateShippingByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.VerifyPaymentServicePort;
import br.com.cams7.orders.core.port.out.exception.ResponseStatusException;
import java.util.List;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class CreateOrderUseCaseTests extends BaseTests {

  @InjectMocks private CreateOrderUseCase createOrderUseCase;

  @Mock private GetCustomerServicePort getCustomerService;
  @Mock private GetCustomerAddressServicePort getCustomerAddressService;
  @Mock private GetCustomerCardServicePort getCustomerCardService;
  @Mock private GetCartItemsServicePort getCartItemsService;
  @Mock private VerifyPaymentServicePort verifyPaymentService;
  @Mock private AddShippingOrderServicePort addShippingOrderService;
  @Mock private CreateOrderRepositoryPort createOrderRepository;
  @Mock private UpdateShippingByIdRepositoryPort updateShippingByIdRepository;

  @Captor private ArgumentCaptor<OrderEntity> orderEntityCaptor;

  @BeforeEach
  public void before() throws IllegalAccessException {
    FieldUtils.writeField(createOrderUseCase, "shippingAmount", 10.5f, true);
  }

  @Test
  @DisplayName("Should create order when pass valid URLs")
  void shouldCreateOrderWhenPassValidURLs() {
    var createOrder = getCreateOrder();
    OrderEntity order = from(OrderEntity.class).gimme(AUTHORISED_ORDER_ENTITY);
    Customer customer = from(Customer.class).gimme(CUSTOMER);
    CustomerAddress customerAddress = from(CustomerAddress.class).gimme(CUSTOMER_ADDRESS);
    CustomerCard customerCard = from(CustomerCard.class).gimme(CUSTOMER_CARD);
    List<CartItem> cartItems =
        List.of(from(CartItem.class).gimme(CART_ITEM1), from(CartItem.class).gimme(CART_ITEM3));
    Payment payment = from(Payment.class).gimme(AUTHORISED_PAYMENT);

    given(getCustomerService.getCustomer(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customer));
    given(getCustomerAddressService.getCustomerAddress(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerAddress));
    given(getCustomerCardService.getCustomerCard(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerCard));
    given(getCartItemsService.getCartItems(anyString(), anyString(), anyString()))
        .willReturn(Flux.fromIterable(cartItems));
    given(verifyPaymentService.verify(anyString(), anyString(), anyString(), anyFloat()))
        .willReturn(Mono.just(payment));
    given(addShippingOrderService.add(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(order.getOrderId()));
    given(createOrderRepository.create(anyString(), any(OrderEntity.class)))
        .willReturn(Mono.just(order));
    given(updateShippingByIdRepository.updateShipping(anyString(), anyString(), anyBoolean()))
        .willReturn(Mono.just(1l));

    create(createOrderUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, REQUEST_TRACE_ID, createOrder))
        .expectSubscription()
        .expectNext(order)
        .verifyComplete();

    then(getCustomerService)
        .should(times(1))
        .getCustomer(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCustomerUrl()));
    then(getCustomerAddressService)
        .should(times(1))
        .getCustomerAddress(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getAddressUrl()));
    then(getCustomerCardService)
        .should(times(1))
        .getCustomerCard(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCardUrl()));
    then(getCartItemsService)
        .should(times(1))
        .getCartItems(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getItemsUrl()));
    then(verifyPaymentService)
        .should(times(1))
        .verify(
            eq(CUSTOMER_ADDRESS_COUNTRY),
            eq(REQUEST_TRACE_ID),
            eq(CUSTOMER_ID),
            eq(AUTHORISED_TOTAL_AMOUNT));
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

    then(addShippingOrderService)
        .should(times(1))
        .add(eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(ORDER_ID));

    then(updateShippingByIdRepository)
        .should(times(1))
        .updateShipping(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID), eq(Boolean.TRUE));
  }

  @Test
  @DisplayName("Should create order when not get shipping id")
  void shouldCreateOrderWhenNotGetShippingId() {
    var createOrder = getCreateOrder();
    OrderEntity order = from(OrderEntity.class).gimme(AUTHORISED_ORDER_ENTITY);
    Customer customer = from(Customer.class).gimme(CUSTOMER);
    CustomerAddress customerAddress = from(CustomerAddress.class).gimme(CUSTOMER_ADDRESS);
    CustomerCard customerCard = from(CustomerCard.class).gimme(CUSTOMER_CARD);
    List<CartItem> cartItems =
        List.of(from(CartItem.class).gimme(CART_ITEM1), from(CartItem.class).gimme(CART_ITEM3));
    Payment payment = from(Payment.class).gimme(AUTHORISED_PAYMENT);

    given(getCustomerService.getCustomer(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customer));
    given(getCustomerAddressService.getCustomerAddress(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerAddress));
    given(getCustomerCardService.getCustomerCard(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerCard));
    given(getCartItemsService.getCartItems(anyString(), anyString(), anyString()))
        .willReturn(Flux.fromIterable(cartItems));
    given(verifyPaymentService.verify(anyString(), anyString(), anyString(), anyFloat()))
        .willReturn(Mono.just(payment));
    given(addShippingOrderService.add(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(""));
    given(createOrderRepository.create(anyString(), any(OrderEntity.class)))
        .willReturn(Mono.just(order));
    given(updateShippingByIdRepository.updateShipping(anyString(), anyString(), anyBoolean()))
        .willReturn(Mono.just(1l));

    create(createOrderUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, REQUEST_TRACE_ID, createOrder))
        .expectSubscription()
        .expectNext(order.withRegisteredShipping(false))
        .verifyComplete();

    then(getCustomerService)
        .should(times(1))
        .getCustomer(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCustomerUrl()));
    then(getCustomerAddressService)
        .should(times(1))
        .getCustomerAddress(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getAddressUrl()));
    then(getCustomerCardService)
        .should(times(1))
        .getCustomerCard(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCardUrl()));
    then(getCartItemsService)
        .should(times(1))
        .getCartItems(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getItemsUrl()));
    then(verifyPaymentService)
        .should(times(1))
        .verify(
            eq(CUSTOMER_ADDRESS_COUNTRY),
            eq(REQUEST_TRACE_ID),
            eq(CUSTOMER_ID),
            eq(AUTHORISED_TOTAL_AMOUNT));
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

    then(addShippingOrderService)
        .should(times(1))
        .add(eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(ORDER_ID));

    then(updateShippingByIdRepository)
        .should(times(1))
        .updateShipping(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID), eq(Boolean.FALSE));
  }

  @Test
  @DisplayName("Should throw error when pass decline payment")
  void shouldThrowErrorWhenPassDeclinePayment() {
    var createOrder = getCreateOrder();
    Customer customer = from(Customer.class).gimme(CUSTOMER);
    CustomerAddress customerAddress = from(CustomerAddress.class).gimme(CUSTOMER_ADDRESS);
    CustomerCard customerCard = from(CustomerCard.class).gimme(CUSTOMER_CARD);
    List<CartItem> cartItems =
        List.of(
            from(CartItem.class).gimme(CART_ITEM1),
            from(CartItem.class).gimme(CART_ITEM2),
            from(CartItem.class).gimme(CART_ITEM3));
    Payment payment = from(Payment.class).gimme(DECLINED_PAYMENT);

    given(getCustomerService.getCustomer(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customer));
    given(getCustomerAddressService.getCustomerAddress(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerAddress));
    given(getCustomerCardService.getCustomerCard(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerCard));
    given(getCartItemsService.getCartItems(anyString(), anyString(), anyString()))
        .willReturn(Flux.fromIterable(cartItems));
    given(verifyPaymentService.verify(anyString(), anyString(), anyString(), anyFloat()))
        .willReturn(Mono.just(payment));

    create(createOrderUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, REQUEST_TRACE_ID, createOrder))
        .expectSubscription()
        .expectErrorMatches(
            exception ->
                isResponseStatusException(exception, BAD_REQUEST_CODE, payment.getMessage()))
        .verify();

    then(getCustomerService)
        .should(times(1))
        .getCustomer(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCustomerUrl()));
    then(getCustomerAddressService)
        .should(times(1))
        .getCustomerAddress(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getAddressUrl()));
    then(getCustomerCardService)
        .should(times(1))
        .getCustomerCard(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCardUrl()));
    then(getCartItemsService)
        .should(times(1))
        .getCartItems(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getItemsUrl()));
    then(verifyPaymentService)
        .should(times(1))
        .verify(
            eq(CUSTOMER_ADDRESS_COUNTRY),
            eq(REQUEST_TRACE_ID),
            eq(CUSTOMER_ID),
            eq(DECLINED_TOTAL_AMOUNT));
    then(createOrderRepository).should(never()).create(anyString(), any(OrderEntity.class));
    then(addShippingOrderService).should(never()).add(anyString(), anyString(), anyString());
    then(updateShippingByIdRepository)
        .should(never())
        .updateShipping(anyString(), anyString(), anyBoolean());
  }

  @Test
  @DisplayName("Should throw error when don't have cart items")
  void shouldThrowErrorWhenDoNotHaveCartItems() {
    var createOrder = getCreateOrder();
    Customer customer = from(Customer.class).gimme(CUSTOMER);
    CustomerAddress customerAddress = from(CustomerAddress.class).gimme(CUSTOMER_ADDRESS);
    CustomerCard customerCard = from(CustomerCard.class).gimme(CUSTOMER_CARD);
    List<CartItem> cartItems = List.of();

    given(getCustomerService.getCustomer(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customer));
    given(getCustomerAddressService.getCustomerAddress(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerAddress));
    given(getCustomerCardService.getCustomerCard(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerCard));
    given(getCartItemsService.getCartItems(anyString(), anyString(), anyString()))
        .willReturn(Flux.fromIterable(cartItems));

    create(createOrderUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, REQUEST_TRACE_ID, createOrder))
        .expectSubscription()
        .expectErrorMatches(
            exception ->
                isResponseStatusException(
                    exception, BAD_REQUEST_CODE, "There aren't items in the cart"))
        .verify();

    then(getCustomerService)
        .should(times(1))
        .getCustomer(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCustomerUrl()));
    then(getCustomerAddressService)
        .should(times(1))
        .getCustomerAddress(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getAddressUrl()));
    then(getCustomerCardService)
        .should(times(1))
        .getCustomerCard(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCardUrl()));
    then(getCartItemsService)
        .should(times(1))
        .getCartItems(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getItemsUrl()));
    then(verifyPaymentService)
        .should(never())
        .verify(anyString(), anyString(), anyString(), anyFloat());
    then(createOrderRepository).should(never()).create(anyString(), any(OrderEntity.class));
    then(addShippingOrderService).should(never()).add(anyString(), anyString(), anyString());
    then(updateShippingByIdRepository)
        .should(never())
        .updateShipping(anyString(), anyString(), anyBoolean());
  }

  @Test
  @DisplayName("Should throw error when 'get customer' throws error")
  void shouldThrowErrorWhenGetCustomerThrowsError() {
    var createOrder = getCreateOrder();

    CustomerAddress customerAddress = from(CustomerAddress.class).gimme(CUSTOMER_ADDRESS);
    CustomerCard customerCard = from(CustomerCard.class).gimme(CUSTOMER_CARD);
    List<CartItem> cartItems =
        List.of(from(CartItem.class).gimme(CART_ITEM1), from(CartItem.class).gimme(CART_ITEM3));

    given(getCustomerService.getCustomer(anyString(), anyString(), anyString()))
        .willReturn(Mono.error(new RuntimeException(ERROR_MESSAGE)));
    given(getCustomerAddressService.getCustomerAddress(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerAddress));
    given(getCustomerCardService.getCustomerCard(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerCard));
    given(getCartItemsService.getCartItems(anyString(), anyString(), anyString()))
        .willReturn(Flux.fromIterable(cartItems));

    create(createOrderUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, REQUEST_TRACE_ID, createOrder))
        .expectSubscription()
        .expectErrorMatches(exception -> isRuntimeException(exception))
        .verify();

    then(getCustomerService)
        .should(times(1))
        .getCustomer(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCustomerUrl()));
    then(getCustomerAddressService)
        .should(times(1))
        .getCustomerAddress(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getAddressUrl()));
    then(getCustomerCardService)
        .should(times(1))
        .getCustomerCard(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCardUrl()));
    then(getCartItemsService)
        .should(times(1))
        .getCartItems(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getItemsUrl()));
    then(verifyPaymentService)
        .should(never())
        .verify(anyString(), anyString(), anyString(), anyFloat());
    then(createOrderRepository).should(never()).create(anyString(), any(OrderEntity.class));
    then(addShippingOrderService).should(never()).add(anyString(), anyString(), anyString());
    then(updateShippingByIdRepository)
        .should(never())
        .updateShipping(anyString(), anyString(), anyBoolean());
  }

  @Test
  @DisplayName("Should throw error when 'get customer address' throws error")
  void shouldThrowErrorWhenGetCustomerAddressThrowsError() {
    var createOrder = getCreateOrder();

    Customer customer = from(Customer.class).gimme(CUSTOMER);
    CustomerCard customerCard = from(CustomerCard.class).gimme(CUSTOMER_CARD);
    List<CartItem> cartItems =
        List.of(from(CartItem.class).gimme(CART_ITEM1), from(CartItem.class).gimme(CART_ITEM3));

    given(getCustomerService.getCustomer(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customer));
    given(getCustomerAddressService.getCustomerAddress(anyString(), anyString(), anyString()))
        .willReturn(Mono.error(new RuntimeException(ERROR_MESSAGE)));
    given(getCustomerCardService.getCustomerCard(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerCard));
    given(getCartItemsService.getCartItems(anyString(), anyString(), anyString()))
        .willReturn(Flux.fromIterable(cartItems));

    create(createOrderUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, REQUEST_TRACE_ID, createOrder))
        .expectSubscription()
        .expectErrorMatches(exception -> isRuntimeException(exception))
        .verify();

    then(getCustomerService)
        .should(times(1))
        .getCustomer(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCustomerUrl()));
    then(getCustomerAddressService)
        .should(times(1))
        .getCustomerAddress(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getAddressUrl()));
    then(getCustomerCardService)
        .should(times(1))
        .getCustomerCard(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCardUrl()));
    then(getCartItemsService)
        .should(times(1))
        .getCartItems(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getItemsUrl()));
    then(verifyPaymentService)
        .should(never())
        .verify(anyString(), anyString(), anyString(), anyFloat());
    then(createOrderRepository).should(never()).create(anyString(), any(OrderEntity.class));
    then(addShippingOrderService).should(never()).add(anyString(), anyString(), anyString());
    then(updateShippingByIdRepository)
        .should(never())
        .updateShipping(anyString(), anyString(), anyBoolean());
  }

  @Test
  @DisplayName("Should throw error when 'get customer card' throws error")
  void shouldThrowErrorWhenGetCustomerCardThrowsError() {
    var createOrder = getCreateOrder();

    Customer customer = from(Customer.class).gimme(CUSTOMER);
    CustomerAddress customerAddress = from(CustomerAddress.class).gimme(CUSTOMER_ADDRESS);
    List<CartItem> cartItems =
        List.of(from(CartItem.class).gimme(CART_ITEM1), from(CartItem.class).gimme(CART_ITEM3));

    given(getCustomerService.getCustomer(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customer));
    given(getCustomerAddressService.getCustomerAddress(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerAddress));
    given(getCustomerCardService.getCustomerCard(anyString(), anyString(), anyString()))
        .willReturn(Mono.error(new RuntimeException(ERROR_MESSAGE)));
    given(getCartItemsService.getCartItems(anyString(), anyString(), anyString()))
        .willReturn(Flux.fromIterable(cartItems));

    create(createOrderUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, REQUEST_TRACE_ID, createOrder))
        .expectSubscription()
        .expectErrorMatches(exception -> isRuntimeException(exception))
        .verify();

    then(getCustomerService)
        .should(times(1))
        .getCustomer(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCustomerUrl()));
    then(getCustomerAddressService)
        .should(times(1))
        .getCustomerAddress(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getAddressUrl()));
    then(getCustomerCardService)
        .should(times(1))
        .getCustomerCard(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCardUrl()));
    then(getCartItemsService)
        .should(times(1))
        .getCartItems(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getItemsUrl()));
    then(verifyPaymentService)
        .should(never())
        .verify(anyString(), anyString(), anyString(), anyFloat());
    then(createOrderRepository).should(never()).create(anyString(), any(OrderEntity.class));
    then(addShippingOrderService).should(never()).add(anyString(), anyString(), anyString());
    then(updateShippingByIdRepository)
        .should(never())
        .updateShipping(anyString(), anyString(), anyBoolean());
  }

  @Test
  @DisplayName("Should throw error when 'get cart items' throws error")
  void shouldThrowErrorWhenGetCartItemsThrowsError() {
    var createOrder = getCreateOrder();

    Customer customer = from(Customer.class).gimme(CUSTOMER);
    CustomerAddress customerAddress = from(CustomerAddress.class).gimme(CUSTOMER_ADDRESS);
    CustomerCard customerCard = from(CustomerCard.class).gimme(CUSTOMER_CARD);

    given(getCustomerService.getCustomer(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customer));
    given(getCustomerAddressService.getCustomerAddress(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerAddress));
    given(getCustomerCardService.getCustomerCard(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerCard));
    given(getCartItemsService.getCartItems(anyString(), anyString(), anyString()))
        .willReturn(Flux.error(new RuntimeException(ERROR_MESSAGE)));

    create(createOrderUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, REQUEST_TRACE_ID, createOrder))
        .expectSubscription()
        .expectErrorMatches(exception -> isRuntimeException(exception))
        .verify();

    then(getCustomerService)
        .should(times(1))
        .getCustomer(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCustomerUrl()));
    then(getCustomerAddressService)
        .should(times(1))
        .getCustomerAddress(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getAddressUrl()));
    then(getCustomerCardService)
        .should(times(1))
        .getCustomerCard(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCardUrl()));
    then(getCartItemsService)
        .should(times(1))
        .getCartItems(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getItemsUrl()));
    then(verifyPaymentService)
        .should(never())
        .verify(anyString(), anyString(), anyString(), anyFloat());
    then(createOrderRepository).should(never()).create(anyString(), any(OrderEntity.class));
    then(addShippingOrderService).should(never()).add(anyString(), anyString(), anyString());
    then(updateShippingByIdRepository)
        .should(never())
        .updateShipping(anyString(), anyString(), anyBoolean());
  }

  @Test
  @DisplayName("Should throw error when 'verify payment' throws error")
  void shouldThrowErrorWhenVerifyPaymentThrowsError() {
    var createOrder = getCreateOrder();
    Customer customer = from(Customer.class).gimme(CUSTOMER);
    CustomerAddress customerAddress = from(CustomerAddress.class).gimme(CUSTOMER_ADDRESS);
    CustomerCard customerCard = from(CustomerCard.class).gimme(CUSTOMER_CARD);
    List<CartItem> cartItems =
        List.of(
            from(CartItem.class).gimme(CART_ITEM1),
            from(CartItem.class).gimme(CART_ITEM2),
            from(CartItem.class).gimme(CART_ITEM3));

    given(getCustomerService.getCustomer(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customer));
    given(getCustomerAddressService.getCustomerAddress(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerAddress));
    given(getCustomerCardService.getCustomerCard(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerCard));
    given(getCartItemsService.getCartItems(anyString(), anyString(), anyString()))
        .willReturn(Flux.fromIterable(cartItems));
    given(verifyPaymentService.verify(anyString(), anyString(), anyString(), anyFloat()))
        .willReturn(Mono.error(new RuntimeException(ERROR_MESSAGE)));

    create(createOrderUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, REQUEST_TRACE_ID, createOrder))
        .expectSubscription()
        .expectErrorMatches(exception -> isRuntimeException(exception))
        .verify();

    then(getCustomerService)
        .should(times(1))
        .getCustomer(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCustomerUrl()));
    then(getCustomerAddressService)
        .should(times(1))
        .getCustomerAddress(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getAddressUrl()));
    then(getCustomerCardService)
        .should(times(1))
        .getCustomerCard(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCardUrl()));
    then(getCartItemsService)
        .should(times(1))
        .getCartItems(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getItemsUrl()));
    then(verifyPaymentService)
        .should(times(1))
        .verify(
            eq(CUSTOMER_ADDRESS_COUNTRY),
            eq(REQUEST_TRACE_ID),
            eq(CUSTOMER_ID),
            eq(DECLINED_TOTAL_AMOUNT));
    then(createOrderRepository).should(never()).create(anyString(), any(OrderEntity.class));
    then(addShippingOrderService).should(never()).add(anyString(), anyString(), anyString());
    then(updateShippingByIdRepository)
        .should(never())
        .updateShipping(anyString(), anyString(), anyBoolean());
  }

  @Test
  @DisplayName("Should throw error when 'create order in database' throws error")
  void shouldThrowErrorWhenCreateOrderInDatabaseThrowsError() {
    var createOrder = getCreateOrder();
    OrderEntity order = from(OrderEntity.class).gimme(AUTHORISED_ORDER_ENTITY);
    Customer customer = from(Customer.class).gimme(CUSTOMER);
    CustomerAddress customerAddress = from(CustomerAddress.class).gimme(CUSTOMER_ADDRESS);
    CustomerCard customerCard = from(CustomerCard.class).gimme(CUSTOMER_CARD);
    List<CartItem> cartItems =
        List.of(from(CartItem.class).gimme(CART_ITEM1), from(CartItem.class).gimme(CART_ITEM3));
    Payment payment = from(Payment.class).gimme(AUTHORISED_PAYMENT);

    given(getCustomerService.getCustomer(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customer));
    given(getCustomerAddressService.getCustomerAddress(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerAddress));
    given(getCustomerCardService.getCustomerCard(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerCard));
    given(getCartItemsService.getCartItems(anyString(), anyString(), anyString()))
        .willReturn(Flux.fromIterable(cartItems));
    given(verifyPaymentService.verify(anyString(), anyString(), anyString(), anyFloat()))
        .willReturn(Mono.just(payment));
    given(createOrderRepository.create(anyString(), any(OrderEntity.class)))
        .willReturn(Mono.error(new RuntimeException(ERROR_MESSAGE)));

    create(createOrderUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, REQUEST_TRACE_ID, createOrder))
        .expectSubscription()
        .expectErrorMatches(exception -> isRuntimeException(exception))
        .verify();

    then(getCustomerService)
        .should(times(1))
        .getCustomer(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCustomerUrl()));
    then(getCustomerAddressService)
        .should(times(1))
        .getCustomerAddress(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getAddressUrl()));
    then(getCustomerCardService)
        .should(times(1))
        .getCustomerCard(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCardUrl()));
    then(getCartItemsService)
        .should(times(1))
        .getCartItems(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getItemsUrl()));
    then(verifyPaymentService)
        .should(times(1))
        .verify(
            eq(CUSTOMER_ADDRESS_COUNTRY),
            eq(REQUEST_TRACE_ID),
            eq(CUSTOMER_ID),
            eq(AUTHORISED_TOTAL_AMOUNT));
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

    then(addShippingOrderService).should(never()).add(anyString(), anyString(), anyString());
    then(updateShippingByIdRepository)
        .should(never())
        .updateShipping(anyString(), anyString(), anyBoolean());
  }

  @Test
  @DisplayName("Should throw error when 'add shipping' throws error")
  void shouldThrowErrorWhenAddShippingThrowsError() {
    var createOrder = getCreateOrder();
    OrderEntity order = from(OrderEntity.class).gimme(AUTHORISED_ORDER_ENTITY);
    Customer customer = from(Customer.class).gimme(CUSTOMER);
    CustomerAddress customerAddress = from(CustomerAddress.class).gimme(CUSTOMER_ADDRESS);
    CustomerCard customerCard = from(CustomerCard.class).gimme(CUSTOMER_CARD);
    List<CartItem> cartItems =
        List.of(from(CartItem.class).gimme(CART_ITEM1), from(CartItem.class).gimme(CART_ITEM3));
    Payment payment = from(Payment.class).gimme(AUTHORISED_PAYMENT);

    given(getCustomerService.getCustomer(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customer));
    given(getCustomerAddressService.getCustomerAddress(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerAddress));
    given(getCustomerCardService.getCustomerCard(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerCard));
    given(getCartItemsService.getCartItems(anyString(), anyString(), anyString()))
        .willReturn(Flux.fromIterable(cartItems));
    given(verifyPaymentService.verify(anyString(), anyString(), anyString(), anyFloat()))
        .willReturn(Mono.just(payment));
    given(createOrderRepository.create(anyString(), any(OrderEntity.class)))
        .willReturn(Mono.just(order));
    given(addShippingOrderService.add(anyString(), anyString(), anyString()))
        .willReturn(Mono.error(new RuntimeException(ERROR_MESSAGE)));

    create(createOrderUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, REQUEST_TRACE_ID, createOrder))
        .expectSubscription()
        .expectErrorMatches(exception -> isRuntimeException(exception))
        .verify();

    then(getCustomerService)
        .should(times(1))
        .getCustomer(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCustomerUrl()));
    then(getCustomerAddressService)
        .should(times(1))
        .getCustomerAddress(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getAddressUrl()));
    then(getCustomerCardService)
        .should(times(1))
        .getCustomerCard(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCardUrl()));
    then(getCartItemsService)
        .should(times(1))
        .getCartItems(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getItemsUrl()));
    then(verifyPaymentService)
        .should(times(1))
        .verify(
            eq(CUSTOMER_ADDRESS_COUNTRY),
            eq(REQUEST_TRACE_ID),
            eq(CUSTOMER_ID),
            eq(AUTHORISED_TOTAL_AMOUNT));
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

    then(addShippingOrderService)
        .should(times(1))
        .add(eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(ORDER_ID));
    then(updateShippingByIdRepository)
        .should(never())
        .updateShipping(anyString(), anyString(), anyBoolean());
  }

  @Test
  @DisplayName("Should throw error when 'update shipping' returns zero")
  void shouldThrowErrorWhenUpdateShippingReturnsZero() {
    var createOrder = getCreateOrder();
    OrderEntity order = from(OrderEntity.class).gimme(AUTHORISED_ORDER_ENTITY);
    Customer customer = from(Customer.class).gimme(CUSTOMER);
    CustomerAddress customerAddress = from(CustomerAddress.class).gimme(CUSTOMER_ADDRESS);
    CustomerCard customerCard = from(CustomerCard.class).gimme(CUSTOMER_CARD);
    List<CartItem> cartItems =
        List.of(from(CartItem.class).gimme(CART_ITEM1), from(CartItem.class).gimme(CART_ITEM3));
    Payment payment = from(Payment.class).gimme(AUTHORISED_PAYMENT);

    given(getCustomerService.getCustomer(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customer));
    given(getCustomerAddressService.getCustomerAddress(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerAddress));
    given(getCustomerCardService.getCustomerCard(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(customerCard));
    given(getCartItemsService.getCartItems(anyString(), anyString(), anyString()))
        .willReturn(Flux.fromIterable(cartItems));
    given(verifyPaymentService.verify(anyString(), anyString(), anyString(), anyFloat()))
        .willReturn(Mono.just(payment));
    given(addShippingOrderService.add(anyString(), anyString(), anyString()))
        .willReturn(Mono.just(order.getOrderId()));
    given(createOrderRepository.create(anyString(), any(OrderEntity.class)))
        .willReturn(Mono.just(order));
    given(updateShippingByIdRepository.updateShipping(anyString(), anyString(), anyBoolean()))
        .willReturn(Mono.just(0l));

    create(createOrderUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, REQUEST_TRACE_ID, createOrder))
        .expectSubscription()
        .expectErrorMatches(
            exception ->
                isResponseStatusException(
                    exception,
                    INTERNAL_SERVER_ERROR_CODE,
                    String.format(
                        "The registeredShipping field of order %s hasn't been changed", ORDER_ID)))
        .verify();

    then(getCustomerService)
        .should(times(1))
        .getCustomer(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCustomerUrl()));
    then(getCustomerAddressService)
        .should(times(1))
        .getCustomerAddress(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getAddressUrl()));
    then(getCustomerCardService)
        .should(times(1))
        .getCustomerCard(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getCardUrl()));
    then(getCartItemsService)
        .should(times(1))
        .getCartItems(
            eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(createOrder.getItemsUrl()));
    then(verifyPaymentService)
        .should(times(1))
        .verify(
            eq(CUSTOMER_ADDRESS_COUNTRY),
            eq(REQUEST_TRACE_ID),
            eq(CUSTOMER_ID),
            eq(AUTHORISED_TOTAL_AMOUNT));
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

    then(addShippingOrderService)
        .should(times(1))
        .add(eq(CUSTOMER_ADDRESS_COUNTRY), eq(REQUEST_TRACE_ID), eq(ORDER_ID));

    then(updateShippingByIdRepository)
        .should(times(1))
        .updateShipping(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID), eq(Boolean.TRUE));
  }

  private static boolean isResponseStatusException(
      Throwable throwable, int statusCode, String message) {
    if (!ResponseStatusException.class.equals(throwable.getClass())) return false;
    var exception = (ResponseStatusException) throwable;
    return statusCode == exception.getStatusCode() && exception.getMessage().equals(message);
  }

  private static boolean isRuntimeException(Throwable throwable) {
    if (!RuntimeException.class.equals(throwable.getClass())) return false;
    var exception = (RuntimeException) throwable;
    return exception.getMessage().equals(ERROR_MESSAGE);
  }

  private static CreateOrderCommand getCreateOrder() {
    CreateOrderRequest request = from(CreateOrderRequest.class).gimme(VALID_CREATE_ORDER_REQUEST);
    return new CreateOrderCommand(
        request.getCustomerUrl(),
        request.getAddressUrl(),
        request.getCardUrl(),
        request.getItemsUrl());
  }
}
