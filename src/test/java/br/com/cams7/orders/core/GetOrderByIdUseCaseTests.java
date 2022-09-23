package br.com.cams7.orders.core;

import static br.com.cams7.orders.template.DomainTemplateLoader.ORDER_ENTITY;
import static br.com.cams7.orders.template.OrderEntityTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.cams7.orders.template.OrderEntityTemplate.ORDER_ID;
import static br.com.six2six.fixturefactory.Fixture.from;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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
    OrderEntity order = from(OrderEntity.class).gimme(ORDER_ENTITY);

    given(getOrderByIdRepository.getOrder(anyString(), anyString())).willReturn(Mono.just(order));

    getOrderByIdUseCase.execute(CUSTOMER_ADDRESS_COUNTRY, ORDER_ID);

    then(getOrderByIdRepository)
        .should(times(1))
        .getOrder(eq(CUSTOMER_ADDRESS_COUNTRY), eq(ORDER_ID));
  }
}
