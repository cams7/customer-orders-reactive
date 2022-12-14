package br.com.cams7.orders.core;

import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_ORDER_ENTITY;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.ORDER_ID;
import static br.com.six2six.fixturefactory.Fixture.from;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static reactor.test.StepVerifier.create;

import br.com.cams7.orders.BaseTests;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.out.GetOrderByIdRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class GetOrderByIdUseCaseTests extends BaseTests {

  @InjectMocks private GetOrderByIdUseCase getOrderByIdUseCase;

  @Mock private GetOrderByIdRepositoryPort getOrderByIdRepository;

  @Test
  @DisplayName("Should get order when pass valid order id")
  void shouldGetOrderWhenPassValidOrderId() {
    OrderEntity order = from(OrderEntity.class).gimme(AUTHORISED_ORDER_ENTITY);

    given(getOrderByIdRepository.getOrder(anyString(), anyString())).willReturn(Mono.just(order));

    create(getOrderByIdUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, ORDER_ID))
        .expectSubscription()
        .expectNext(order)
        .verifyComplete();

    then(getOrderByIdRepository)
        .should(times(1))
        .getOrder(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID));
  }

  @Test
  @DisplayName("Should throw error when 'get order by id in database' throws error")
  void shouldThrowErrorWhenGetOrderByIdInDatabaseThrowsError() {

    given(getOrderByIdRepository.getOrder(anyString(), anyString()))
        .willReturn(Mono.error(new RuntimeException(ERROR_MESSAGE)));

    create(getOrderByIdUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, ORDER_ID))
        .expectSubscription()
        .expectErrorMatches(exception -> isRuntimeException(exception))
        .verify();

    then(getOrderByIdRepository)
        .should(times(1))
        .getOrder(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID));
  }

  private static boolean isRuntimeException(Throwable throwable) {
    if (!RuntimeException.class.equals(throwable.getClass())) return false;
    var exception = (RuntimeException) throwable;
    return exception.getMessage().equals(ERROR_MESSAGE);
  }
}
