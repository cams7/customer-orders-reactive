package br.com.cams7.orders.core.port.out.exception;

import lombok.Getter;

@Getter
public class ResponseStatusException extends RuntimeException {

  private static final long serialVersionUID = 6807779761793856942L;

  public static final int BAD_REQUEST_CODE = 400;
  public static final String BAD_REQUEST_NAME = "Bad Request";
  public static final int NOT_FOUND_CODE = 404;
  public static final String NOT_FOUND_NAME = "Not Found";
  public static final int INTERNAL_SERVER_ERROR_CODE = 500;
  public static final String INTERNAL_SERVER_ERROR_NAME = "Internal Server Error";

  private final int statusCode;

  public ResponseStatusException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public String getStatusText() {
    return getStatusText(statusCode);
  }

  public static String getStatusText(int statusCode) {
    return getStatusText(statusCode, "Unknown");
  }

  private static String getStatusText(int statusCode, String statusName) {
    if (BAD_REQUEST_CODE == statusCode) return BAD_REQUEST_NAME;

    if (NOT_FOUND_CODE == statusCode) return NOT_FOUND_NAME;

    if (INTERNAL_SERVER_ERROR_CODE == statusCode) return INTERNAL_SERVER_ERROR_NAME;

    return statusName;
  }
}
