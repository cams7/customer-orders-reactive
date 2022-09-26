package br.com.cams7.orders.core;

import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_ORDER_ENTITY;
import static br.com.cams7.orders.template.DomainTemplateLoader.DECLINED_ORDER_ENTITY;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.six2six.fixturefactory.Fixture.from;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static reactor.test.StepVerifier.create;

import br.com.cams7.orders.BaseTests;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.out.GetOrdersByCountryRepositoryPort;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
public class GetOrdersByCountryUseCaseTests extends BaseTests {

  @InjectMocks private GetOrdersByCountryUseCase getOrdersByCountryUseCase;

  @Mock private GetOrdersByCountryRepositoryPort getOrdersByCountryRepository;

  @Test
  @DisplayName("Should get orders when pass valid country")
  void shouldGetOrdersWhenPassValidCountry() {
    List<OrderEntity> orders =
        List.of(
            from(OrderEntity.class).gimme(AUTHORISED_ORDER_ENTITY),
            from(OrderEntity.class).gimme(DECLINED_ORDER_ENTITY));

    given(getOrdersByCountryRepository.getOrders(anyString()))
        .willReturn(Flux.fromIterable(orders));

    create(getOrdersByCountryUseCase.execute(CUSTOMER_ADDRESS_COUNTRY))
        .expectSubscription()
        .expectNextSequence(orders)
        .verifyComplete();

    then(getOrdersByCountryRepository).should(times(1)).getOrders(eq(CUSTOMER_ADDRESS_COUNTRY));
  }

  @Test
  @DisplayName("Should throw error when 'get orders in database' throws error")
  void shouldThrowErrorWhenGetOrdersInDatabaseThrowsError() {

    given(getOrdersByCountryRepository.getOrders(anyString()))
        .willReturn(Flux.error(new RuntimeException(ERROR_MESSAGE)));

    create(getOrdersByCountryUseCase.execute(CUSTOMER_ADDRESS_COUNTRY))
        .expectSubscription()
        .expectErrorMatches(exception -> isRuntimeException(exception))
        .verify();

    then(getOrdersByCountryRepository).should(times(1)).getOrders(eq(CUSTOMER_ADDRESS_COUNTRY));
  }

  private static boolean isRuntimeException(Throwable throwable) {
    if (!RuntimeException.class.equals(throwable.getClass())) return false;
    var exception = (RuntimeException) throwable;
    return exception.getMessage().equals(ERROR_MESSAGE);
  }
}
