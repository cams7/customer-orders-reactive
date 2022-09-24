package br.com.cams7.orders.adapter.controller;

import static br.com.cams7.orders.core.utils.DateUtils.getFormattedDateTime;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.com.cams7.orders.adapter.controller.response.OrderResponse;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.in.DeleteOrderByIdUseCasePort;
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
import org.springframework.web.bind.annotation.DeleteMapping;
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

  private static final String COUNTRY_HEADER = "country";
  private static final String COUNTRY_DESCRIPTION = "Customer country";
  private static final String COUNTRY_EXAMPLE = "BR";
  private static final String REQUEST_TRACE_ID_HEADER = "requestTraceId";
  private static final String REQUEST_TRACE_ID_DESCRIPTION = "Cross transaction unique ID";
  private static final String REQUEST_TRACE_ID_EXAMPLE = "123BR";
  private static final String STATUS_200_CODE = "200";
  private static final String STATUS_200_OK = "Ok";
  private static final String STATUS_400_CODE = "400";
  private static final String STATUS_400_BAD_REQUEST = "Bad Request";
  private static final String STATUS_500_CODE = "500";
  private static final String STATUS_500_BAD_INTERNAL_SERVER_ERROR = "Internal Server Error";

  private final GetOrdersByCountryUseCasePort getOrdersByCountryUseCase;
  private final GetOrderByIdUseCasePort getOrderByIdUseCase;
  private final DeleteOrderByIdUseCasePort deleteOrderByIdUseCase;
  private final ModelMapper modelMapper;

  @Operation(description = "Get orders")
  @Parameters({
    @Parameter(
        name = COUNTRY_HEADER,
        in = HEADER,
        required = true,
        description = COUNTRY_DESCRIPTION,
        example = COUNTRY_EXAMPLE),
    @Parameter(
        name = REQUEST_TRACE_ID_HEADER,
        in = HEADER,
        required = true,
        description = REQUEST_TRACE_ID_DESCRIPTION,
        example = REQUEST_TRACE_ID_EXAMPLE)
  })
  @ApiResponses({
    @ApiResponse(responseCode = STATUS_200_CODE, description = STATUS_200_OK),
    @ApiResponse(
        responseCode = STATUS_400_CODE,
        description = STATUS_400_BAD_REQUEST,
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = STATUS_500_CODE,
        description = STATUS_500_BAD_INTERNAL_SERVER_ERROR,
        content = @Content(schema = @Schema(hidden = true)))
  })
  @ResponseStatus(OK)
  @GetMapping
  Flux<OrderResponse> getOrders(
      @RequestHeader(COUNTRY_HEADER) String country,
      @RequestHeader(REQUEST_TRACE_ID_HEADER) String requestTraceId) {
    return getOrdersByCountryUseCase.execute(country).map(this::getOrder);
  }

  @Operation(description = "Get order")
  @Parameters({
    @Parameter(
        name = COUNTRY_HEADER,
        in = HEADER,
        required = true,
        description = COUNTRY_DESCRIPTION,
        example = COUNTRY_EXAMPLE),
    @Parameter(
        name = REQUEST_TRACE_ID_HEADER,
        in = HEADER,
        required = true,
        description = REQUEST_TRACE_ID_DESCRIPTION,
        example = REQUEST_TRACE_ID_EXAMPLE)
  })
  @ApiResponses({
    @ApiResponse(responseCode = STATUS_200_CODE, description = STATUS_200_OK),
    @ApiResponse(
        responseCode = STATUS_400_CODE,
        description = STATUS_400_BAD_REQUEST,
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = STATUS_500_CODE,
        description = STATUS_500_BAD_INTERNAL_SERVER_ERROR,
        content = @Content(schema = @Schema(hidden = true)))
  })
  @ResponseStatus(OK)
  @GetMapping("/{orderId}")
  Mono<OrderResponse> getOrder(
      @RequestHeader(COUNTRY_HEADER) String country,
      @RequestHeader(REQUEST_TRACE_ID_HEADER) String requestTraceId,
      @Parameter(
              name = "orderId",
              required = true,
              description = "Order id",
              example = "57a98d98e4b00679b4a830af")
          @PathVariable
          String orderId) {
    return getOrderByIdUseCase.execute(country, orderId).map(this::getOrder);
  }

  @Operation(description = "Delete order")
  @Parameters({
    @Parameter(
        name = COUNTRY_HEADER,
        in = HEADER,
        required = true,
        description = COUNTRY_DESCRIPTION,
        example = COUNTRY_EXAMPLE),
    @Parameter(
        name = REQUEST_TRACE_ID_HEADER,
        in = HEADER,
        required = true,
        description = REQUEST_TRACE_ID_DESCRIPTION,
        example = REQUEST_TRACE_ID_EXAMPLE)
  })
  @ApiResponses({
    @ApiResponse(responseCode = STATUS_200_CODE, description = STATUS_200_OK),
    @ApiResponse(
        responseCode = STATUS_400_CODE,
        description = STATUS_400_BAD_REQUEST,
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = STATUS_500_CODE,
        description = STATUS_500_BAD_INTERNAL_SERVER_ERROR,
        content = @Content(schema = @Schema(hidden = true)))
  })
  @ResponseStatus(OK)
  @DeleteMapping("/{orderId}")
  Mono<Void> deleteOrder(
      @RequestHeader(COUNTRY_HEADER) String country,
      @RequestHeader(REQUEST_TRACE_ID_HEADER) String requestTraceId,
      @Parameter(
              name = "orderId",
              required = true,
              description = "Order id",
              example = "57a98d98e4b00679b4a830af")
          @PathVariable
          String orderId) {
    return deleteOrderByIdUseCase.execute(country, orderId);
  }

  private OrderResponse getOrder(OrderEntity entity) {
    var response = modelMapper.map(entity, OrderResponse.class);
    response.setRegistrationDate(getFormattedDateTime(entity.getRegistrationDate()));
    return response;
  }
}
