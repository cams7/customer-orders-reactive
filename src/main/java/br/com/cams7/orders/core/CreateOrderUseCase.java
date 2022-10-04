package br.com.cams7.orders.core;

import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.BAD_REQUEST_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.INTERNAL_SERVER_ERROR_CODE;
import static java.util.regex.Pattern.matches;

import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.in.CreateOrderUseCasePort;
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
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateOrderUseCase implements CreateOrderUseCasePort {

  private static final float SHIPPING = 10.5f;
  private static final String ID_REGEX = "^[\\w\\-]+$";

  private final GetCustomerServicePort getCustomerService;
  private final GetCustomerAddressServicePort getCustomerAddressService;
  private final GetCustomerCardServicePort getCustomerCardService;
  private final GetCartItemsServicePort getCartItemsService;
  private final VerifyPaymentServicePort verifyPaymentService;
  private final AddShippingOrderServicePort addShippingOrderService;
  private final CreateOrderRepositoryPort createOrderRepository;
  private final UpdateShippingByIdRepositoryPort updateShippingByIdRepository;

  @Override
  public Mono<OrderEntity> execute(
      String country, String requestTraceId, CreateOrderCommand command) {
    return Mono.zip(
            getCustomerService.getCustomer(country, requestTraceId, command.getCustomerUrl()),
            getCustomerAddressService.getCustomerAddress(
                country, requestTraceId, command.getAddressUrl()),
            getCustomerCardService.getCustomerCard(country, requestTraceId, command.getCardUrl()))
        .map(
            t ->
                new OrderEntity()
                    .withCustomer(t.getT1())
                    .withAddress(t.getT2())
                    .withCard(t.getT3()))
        .zipWith(
            getCartItemsService
                .getCartItems(country, requestTraceId, command.getItemsUrl())
                .collectList())
        .flatMap(t -> verifyCartItems(t.getT1().withItems(t.getT2())))
        .flatMap(order -> verifyPayment(country, requestTraceId, order))
        .flatMap(order -> createOrder(country, order))
        .flatMap(order -> addShippingOrder(country, requestTraceId, order))
        .flatMap(
            shippingAndOrder ->
                updateShipping(country, shippingAndOrder.shippingId, shippingAndOrder.order));
  }

  private static Mono<OrderEntity> verifyCartItems(OrderEntity order) {
    if (CollectionUtils.isEmpty(order.getItems())) {
      return Mono.error(
          new ResponseStatusException("There aren't items in the cart", BAD_REQUEST_CODE));
    }
    return Mono.just(order);
  }

  private Mono<OrderEntity> verifyPayment(
      String country, String requestTraceId, OrderEntity order) {
    var customerId = order.getCustomer().getCustomerId();
    var totalAmount = getTotalAmount(order);
    return verifyPaymentService
        .verify(country, requestTraceId, customerId, totalAmount)
        .flatMap(payment -> Mono.just(order.withTotalAmount(totalAmount).withPayment(payment)));
  }

  private Mono<OrderEntity> createOrder(String country, OrderEntity order) {
    var payment = order.getPayment();
    if (!payment.isAuthorised()) {
      return Mono.error(new ResponseStatusException(payment.getMessage(), BAD_REQUEST_CODE));
    }
    order.setRegistrationDate(ZonedDateTime.now());
    return createOrderRepository.create(country, order);
  }

  private Mono<ShippingAndOrder> addShippingOrder(
      String country, String requestTraceId, OrderEntity order) {
    return addShippingOrderService
        .add(country, requestTraceId, order.getOrderId())
        .map(shippingId -> new ShippingAndOrder(shippingId, order));
  }

  private Mono<OrderEntity> updateShipping(String country, String shippingId, OrderEntity order) {
    var orderId = order.getOrderId();
    var isRegisteredShipping = shippingId != null && matches(ID_REGEX, shippingId);
    return updateShippingByIdRepository
        .updateShipping(country, orderId, isRegisteredShipping)
        .flatMap(
            modifiedCount -> {
              if (modifiedCount != null && modifiedCount > 0) {
                return Mono.just(order.withRegisteredShipping(isRegisteredShipping));
              }
              return Mono.error(
                  new ResponseStatusException(
                      String.format(
                          "The registeredShipping field of order %s hasn't been changed", orderId),
                      INTERNAL_SERVER_ERROR_CODE));
            });
  }

  private static float getTotalAmount(OrderEntity order) {
    return (float)
        (order.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum()
            + SHIPPING);
  }

  @AllArgsConstructor
  private static class ShippingAndOrder {
    private String shippingId;
    private OrderEntity order;
  }
}
