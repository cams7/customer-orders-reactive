package br.com.cams7.orders.core;

import static br.com.cams7.orders.template.DomainTemplateLoader.ORDER_ENTITY;
import static br.com.cams7.orders.template.OrderEntityTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.six2six.fixturefactory.Fixture.from;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import br.com.cams7.orders.BaseTests;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.out.GetOrdersByCountryRepositoryPort;
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
  @DisplayName("Should get orders by country when pass valid country")
  void shouldGetOrdersByCountryWhenPassValidCountry() {
    OrderEntity order = from(OrderEntity.class).gimme(ORDER_ENTITY);

    given(getOrdersByCountryRepository.getOrders(anyString())).willReturn(Flux.just(order));

    getOrdersByCountryUseCase.execute(CUSTOMER_ADDRESS_COUNTRY);

    then(getOrdersByCountryRepository).should(times(1)).getOrders(eq(CUSTOMER_ADDRESS_COUNTRY));
  }
}
