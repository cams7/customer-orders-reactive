package br.com.cams7.orders.core.commons;

import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.INTERNAL_SERVER_ERROR_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.NOT_FOUND_CODE;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.core.port.out.exception.ResponseStatusException;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

@NoArgsConstructor(access = PRIVATE)
public class CommonExceptions {

  public static <T> Mono<T> responseNotFoundException(String message) {
    return Mono.error(new ResponseStatusException(message, NOT_FOUND_CODE));
  }

  public static <T> Mono<T> responseInternalServerErrorException(String message) {
    return Mono.error(new ResponseStatusException(message, INTERNAL_SERVER_ERROR_CODE));
  }
}
