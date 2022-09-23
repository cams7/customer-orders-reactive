package br.com.cams7.orders.core;

import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.in.GetOrdersUseCasePort;
import br.com.cams7.orders.core.port.out.GetOrdersRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetOrdersUseCase implements GetOrdersUseCasePort {

  private final GetOrdersRepositoryPort getOrdersRepository;

  @Override
  public Flux<OrderEntity> execute(String country) {
    return getOrdersRepository.findAll(country);
  }
}
