package br.com.cams7.orders.core;

import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.BAD_REQUEST_CODE;

import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.in.CreateOrderUseCasePort;
import br.com.cams7.orders.core.port.in.params.CreateOrderCommand;
import br.com.cams7.orders.core.port.out.AddShippingOrderServicePort;
import br.com.cams7.orders.core.port.out.CreateOrderRepositoryPort;
import br.com.cams7.orders.core.port.out.GetCartItemsServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerAddressServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerCardServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerServicePort;
import br.com.cams7.orders.core.port.out.VerifyPaymentServicePort;
import br.com.cams7.orders.core.port.out.exception.ResponseStatusException;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateOrderUseCase implements CreateOrderUseCasePort {

  private static final float SHIPPING = 10.5f;

  private final GetCustomerServicePort getCustomerService;
  private final GetCustomerAddressServicePort getCustomerAddressService;
  private final GetCustomerCardServicePort getCustomerCardService;
  private final GetCartItemsServicePort getCartItemsService;
  private final VerifyPaymentServicePort verifyPaymentService;
  private final AddShippingOrderServicePort addShippingOrderService;
  private final CreateOrderRepositoryPort createOrderRepository;

  @Override
  public Mono<OrderEntity> execute(String country, CreateOrderCommand command) {
    return Mono.zip(
            getCustomerService.getCustomer(command.getCustomerUrl()),
            getCustomerAddressService.getCustomerAddress(command.getAddressUrl()),
            getCustomerCardService.getCustomerCard(command.getCardUrl()))
        .flatMap(
            t ->
                verifyCustomer(
                    new OrderEntity()
                        .withCustomer(t.getT1())
                        .withAddress(t.getT2())
                        .withCard(t.getT3())))
        .zipWith(getCartItemsService.getCartItems(command.getItemsUrl()).collectList())
        .flatMap(t -> verifyCartItems(t.getT1().withItems(t.getT2())))
        .flatMap(this::verifyPayment)
        .flatMap(this::createOrder)
        .flatMap(this::addShippingOrder);
  }

  private Mono<OrderEntity> verifyCustomer(OrderEntity order) {
    return Mono.just(order);
  }

  private Mono<OrderEntity> verifyCartItems(OrderEntity order) {
    if (CollectionUtils.isEmpty(order.getItems())) {
      return Mono.error(
          new ResponseStatusException("There aren't items in the cart", BAD_REQUEST_CODE));
    }
    return Mono.just(order);
  }

  private Mono<OrderEntity> verifyPayment(OrderEntity order) {
    var customerId = order.getCustomer().getCustomerId();
    var totalAmount = getTotalAmount(order);
    return verifyPaymentService
        .verify(customerId, totalAmount)
        .flatMap(payment -> Mono.just(order.withTotalAmount(totalAmount).withPayment(payment)));
  }

  private Mono<OrderEntity> createOrder(OrderEntity order) {
    var payment = order.getPayment();
    if (!payment.isAuthorised()) {
      return Mono.error(new ResponseStatusException(payment.getMessage(), BAD_REQUEST_CODE));
    }
    order.setRegistrationDate(ZonedDateTime.now());
    return createOrderRepository.create(order);
  }

  private Mono<OrderEntity> addShippingOrder(OrderEntity order) {
    return addShippingOrderService.add(order.getOrderId()).flatMap(shippingId -> Mono.just(order));
  }

  private static float getTotalAmount(OrderEntity order) {
    return (float)
        (order.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum()
            + SHIPPING);
  }
}
