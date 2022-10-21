package br.com.cams7.orders.core;

import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.in.GetOrderByIdUseCasePort;
import br.com.cams7.orders.core.port.out.GetOrderByIdRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetOrderByIdUseCase implements GetOrderByIdUseCasePort {

  private final GetOrderByIdRepositoryPort getOrderByIdRepository;

  @Override
  public Mono<OrderEntity> execute(final String country, final String orderId) {
    return getOrderByIdRepository.getOrder(country, orderId);
  }
}
