package br.com.cams7.orders.core;

import br.com.cams7.orders.core.port.in.DeleteOrderByIdUseCasePort;
import br.com.cams7.orders.core.port.out.DeleteOrderByIdRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteOrderByIdUseCase implements DeleteOrderByIdUseCasePort {

  private final DeleteOrderByIdRepositoryPort deleteOrderByIdRepository;

  @Override
  public Mono<Void> execute(final String country, final String orderId) {
    return deleteOrderByIdRepository.delete(country, orderId).flatMap(deletedCount -> Mono.empty());
  }
}
