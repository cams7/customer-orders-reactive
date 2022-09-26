package br.com.cams7.orders.core;

import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.ORDER_ID;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static reactor.test.StepVerifier.create;

import br.com.cams7.orders.BaseTests;
import br.com.cams7.orders.core.port.out.DeleteOrderByIdRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class DeleteOrderByIdUseCaseTests extends BaseTests {

  @InjectMocks private DeleteOrderByIdUseCase deleteOrderByIdUseCase;

  @Mock private DeleteOrderByIdRepositoryPort deleteOrderByIdRepository;

  @Test
  @DisplayName("Should delete order when pass valid order id")
  void shouldDeleteOrderWhenPassValidOrderId() {
    given(deleteOrderByIdRepository.delete(anyString(), anyString())).willReturn(Mono.just(1l));

    create(deleteOrderByIdUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, ORDER_ID))
        .expectSubscription()
        .verifyComplete();

    then(deleteOrderByIdRepository)
        .should(times(1))
        .delete(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID));
  }

  @Test
  @DisplayName("Should throw error when 'delete order in database' throws error")
  void shouldThrowErrorWhenDeleteOrderInDatabaseThrowsError() {

    given(deleteOrderByIdRepository.delete(anyString(), anyString()))
        .willReturn(Mono.error(new RuntimeException(ERROR_MESSAGE)));

    create(deleteOrderByIdUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, ORDER_ID))
        .expectSubscription()
        .expectErrorMatches(exception -> isRuntimeException(exception))
        .verify();

    then(deleteOrderByIdRepository)
        .should(times(1))
        .delete(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID));
  }

  private static boolean isRuntimeException(Throwable throwable) {
    if (!RuntimeException.class.equals(throwable.getClass())) return false;
    var exception = (RuntimeException) throwable;
    return exception.getMessage().equals(ERROR_MESSAGE);
  }
}
