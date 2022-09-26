package br.com.cams7.orders.adapter.controller;

import static br.com.cams7.orders.adapter.repository.utils.DatabaseCollectionUtils.getCollectionByCountry;
import static reactor.test.StepVerifier.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.web.reactive.server.WebTestClient;

public abstract class BaseIntegrationTests extends BaseWebClientTests {

  protected static final String REQUEST_TRACE_ID = "123";

  protected static String TIMESTAMP_ATTRIBUTE = "$.timestamp";
  protected static String PATH_ATTRIBUTE = "$.path";
  protected static String STATUS_ATTRIBUTE = "$.status";
  protected static String ERROR_ATTRIBUTE = "$.error";
  protected static String MESSAGE_ATTRIBUTE = "$.message";
  protected static String TRACE_ATTRIBUTE = "$.trace";
  protected static String REQUESTID_ATTRIBUTE = "$.requestId";
  protected static String EXCEPTION_ATTRIBUTE = "$.exception";

  protected static final String BAD_REQUEST_NAME = "Bad Request";
  protected static final int BAD_REQUEST_CODE = 400;
  public static final String NOT_FOUND_NAME = "Not Found";
  public static final int NOT_FOUND_CODE = 404;
  protected static final String INTERNAL_SERVER_ERROR_NAME = "Internal Server Error";
  protected static final int INTERNAL_SERVER_ERROR_CODE = 500;

  @Autowired protected WebTestClient testClient;

  @Autowired protected ReactiveMongoOperations mongoOperations;

  protected void dropCollection(String country, String collectionName) {
    create(mongoOperations.dropCollection(getCollectionName(country, collectionName)))
        .verifyComplete();
  }

  protected static String getCollectionName(String country, String collectionName) {
    return getCollectionByCountry(country, collectionName);
  }
}
