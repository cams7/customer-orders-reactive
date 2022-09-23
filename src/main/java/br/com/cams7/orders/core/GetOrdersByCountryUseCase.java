package br.com.cams7.orders.core;

import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.in.GetOrdersByCountryUseCasePort;
import br.com.cams7.orders.core.port.out.GetOrdersByCountryRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetOrdersByCountryUseCase implements GetOrdersByCountryUseCasePort {

  private final GetOrdersByCountryRepositoryPort getOrdersRepository;

  @Override
  public Flux<OrderEntity> execute(String country) {
    return getOrdersRepository.getOrders(country);
  }
}
