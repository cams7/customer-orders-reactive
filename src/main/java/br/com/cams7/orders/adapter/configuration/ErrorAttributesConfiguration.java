package br.com.cams7.orders.adapter.configuration;

import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_HEADER;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.BAD_REQUEST_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.INTERNAL_SERVER_ERROR_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.NOT_FOUND_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.getStatusText;

import br.com.cams7.orders.core.port.out.exception.ResponseStatusException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.Data;
import org.slf4j.MDC;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class ErrorAttributesConfiguration extends DefaultErrorAttributes {

  private static final String MESSAGE_ATTRIBUTE = "message";
  private static final String EXCEPTION_ATTRIBUTE = "exception";
  private static final String STATUS_ATTRIBUTE = "status";
  private static final String ERROR_ATTRIBUTE = "error";
  private static final String TRACE_ATTRIBUTE = "trace";
  private static final String REQUEST_ID_ATTRIBUTE = "requestId";

  private static final String ERRORS_ATTRIBUTE = "errors";
  private static final String ERROR_MESSAGE_SEPARATOR = ",";
  private static final String ATTRIBUTE_SEPARATOR = ":";

  @Override
  public Map<String, Object> getErrorAttributes(
      ServerRequest request, ErrorAttributeOptions options) {
    var errorAttributes = super.getErrorAttributes(request, options);
    var throwable = getError(request);
    if (throwable instanceof ResponseStatusException)
      setResponseStatusException(errorAttributes, throwable);
    else if (throwable instanceof ConstraintViolationException)
      setConstraintViolationException(errorAttributes, throwable);
    else if (throwable instanceof WebClientResponseException)
      setWebClientResponseException(errorAttributes, throwable);

    errorAttributes.put(REQUEST_ID_ATTRIBUTE, MDC.get(REQUEST_TRACE_ID_HEADER));
    return errorAttributes;
  }

  private static void setResponseStatusException(
      Map<String, Object> errorAttributes, Throwable throwable) {
    var exception = (ResponseStatusException) throwable;
    switch (exception.getStatusCode()) {
      case BAD_REQUEST_CODE:
      case NOT_FOUND_CODE:
      case INTERNAL_SERVER_ERROR_CODE:
        setErrorAttributes(errorAttributes, exception);
        break;
      default:
        break;
    }
    setErrorAttributes(errorAttributes, exception.getMessage(), ResponseStatusException.class);
  }

  private static void setWebClientResponseException(
      Map<String, Object> errorAttributes, Throwable throwable) {
    var exception = (WebClientResponseException) throwable;
    setErrorAttributes(
        errorAttributes, exception.getStatusCode().value(), exception.getStatusText());
    setErrorAttributes(errorAttributes, exception.getMessage(), WebClientResponseException.class);
  }

  private static void setConstraintViolationException(
      Map<String, Object> errorAttributes, Throwable throwable) {
    var exception = (ConstraintViolationException) throwable;
    setErrorAttributes(errorAttributes, BAD_REQUEST_CODE, getStatusText(BAD_REQUEST_CODE));
    setErrorAttributes(errorAttributes, null, ConstraintViolationException.class);
    errorAttributes.put(ERRORS_ATTRIBUTE, getValidationErrors(exception.getMessage()));
  }

  private static void setErrorAttributes(
      Map<String, Object> errorAttributes, ResponseStatusException exception) {
    setErrorAttributes(errorAttributes, exception.getStatusCode(), exception.getStatusText());
  }

  private static void setErrorAttributes(
      Map<String, Object> errorAttributes, int statusCode, String statusText) {
    errorAttributes.put(STATUS_ATTRIBUTE, statusCode);
    errorAttributes.put(ERROR_ATTRIBUTE, statusText);
    errorAttributes.put(TRACE_ATTRIBUTE, null);
  }

  private static void setErrorAttributes(
      Map<String, Object> errorAttributes, String errorMessage, Class<?> exceptionType) {
    if (errorMessage != null) errorAttributes.put(MESSAGE_ATTRIBUTE, errorMessage);
    errorAttributes.put(EXCEPTION_ATTRIBUTE, exceptionType.getName());
  }

  private static List<ValidationError> getValidationErrors(String errorMessage) {
    return List.of(errorMessage.split(ERROR_MESSAGE_SEPARATOR)).stream()
        .map(ErrorAttributesConfiguration::getValidationError)
        .collect(Collectors.toList());
  }

  private static ValidationError getValidationError(String errorMessage) {
    var atributeWithMessage = errorMessage.split(ATTRIBUTE_SEPARATOR);
    var error = new ValidationError();
    error.setField(atributeWithMessage[0].trim());
    error.setMessage(atributeWithMessage[1].trim());
    return error;
  }

  @Data
  private static class ValidationError {
    private String message;
    private String field;
  }
}
