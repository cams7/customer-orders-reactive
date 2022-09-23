package br.com.cams7.orders.adapter.controller;

import static br.com.cams7.orders.core.utils.DateUtils.getFormattedDateTime;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.com.cams7.orders.adapter.controller.response.OrderResponse;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.in.GetOrderByIdUseCasePort;
import br.com.cams7.orders.core.port.in.GetOrdersByCountryUseCasePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Customer orders")
@RequiredArgsConstructor
// @Log4j2
@RestController
@RequestMapping(path = "/orders", produces = APPLICATION_JSON_VALUE)
public class OrderController {

  private final GetOrdersByCountryUseCasePort getOrdersByCountryUseCase;
  private final GetOrderByIdUseCasePort getOrderByIdUseCase;
  private final ModelMapper modelMapper;

  @Operation(description = "Get orders")
  @Parameters({
    @Parameter(
        name = "country",
        in = HEADER,
        required = true,
        description = "Customer country",
        example = "BR"),
    @Parameter(
        name = "requestTraceId",
        in = HEADER,
        required = true,
        description = "Cross transaction unique ID",
        example = "123BR")
  })
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Ok"),
    @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(schema = @Schema(hidden = true)))
  })
  @ResponseStatus(OK)
  @GetMapping
  Flux<OrderResponse> getOrders(
      @RequestHeader("country") String country,
      @RequestHeader(value = "requestTraceId") String requestTraceId) {
    return getOrdersByCountryUseCase.execute(country).map(this::getOrder);
  }

  @Operation(description = "Get order")
  @Parameters({
    @Parameter(
        name = "country",
        in = HEADER,
        required = true,
        description = "Customer country",
        example = "BR"),
    @Parameter(
        name = "requestTraceId",
        in = HEADER,
        required = true,
        description = "Cross transaction unique ID",
        example = "123BR")
  })
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Ok"),
    @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(schema = @Schema(hidden = true)))
  })
  @ResponseStatus(OK)
  @GetMapping("/{orderId}")
  Mono<OrderResponse> getOrder(
      @RequestHeader("country") String country,
      @RequestHeader(value = "requestTraceId") String requestTraceId,
      @Parameter(
              name = "orderId",
              required = true,
              description = "Order id",
              example = "57a98d98e4b00679b4a830af")
          @PathVariable
          String orderId) {
    return getOrderByIdUseCase.execute(country, orderId).map(this::getOrder);
  }

  private OrderResponse getOrder(OrderEntity entity) {
    var response = modelMapper.map(entity, OrderResponse.class);
    response.setRegistrationDate(getFormattedDateTime(entity.getRegistrationDate()));
    return response;
  }
}
