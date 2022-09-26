package br.com.cams7.orders.adapter.commons;

import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.BAD_REQUEST_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.BAD_REQUEST_NAME;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.INTERNAL_SERVER_ERROR_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.INTERNAL_SERVER_ERROR_NAME;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.NOT_FOUND_CODE;
import static br.com.cams7.orders.core.port.out.exception.ResponseStatusException.NOT_FOUND_NAME;

public final class ApiConstants {

  public static final String COUNTRY_HEADER = "country";
  public static final String COUNTRY_DESCRIPTION = "Customer country";
  public static final String COUNTRY_EXAMPLE = "BR";
  public static final String REQUEST_TRACE_ID_HEADER = "requestTraceId";
  public static final String REQUEST_TRACE_ID_DESCRIPTION = "Cross transaction unique ID";
  public static final String REQUEST_TRACE_ID_EXAMPLE = "123BR";
  public static final String STATUS_200_CODE = "200";
  public static final String STATUS_200_OK = "Ok";
  public static final String STATUS_201_CODE = "201";
  public static final String STATUS_201_CREATED = "Created";
  public static final String STATUS_400_CODE = "" + BAD_REQUEST_CODE + "";
  public static final String STATUS_400_BAD_REQUEST = BAD_REQUEST_NAME;
  public static final String STATUS_404_CODE = "" + NOT_FOUND_CODE + "";
  public static final String STATUS_404_NOT_FOUND = NOT_FOUND_NAME;
  public static final String STATUS_500_CODE = "" + INTERNAL_SERVER_ERROR_CODE + "";
  public static final String STATUS_500_BAD_INTERNAL_SERVER_ERROR = INTERNAL_SERVER_ERROR_NAME;
}
