package br.com.cams7.orders.adapter.controller;

import static br.com.cams7.orders.adapter.repository.utils.DatabaseCollectionUtils.getCollectionByCountry;
import static reactor.test.StepVerifier.create;

import br.com.cams7.orders.BaseTests;
import br.com.cams7.orders.core.port.out.AddShippingOrderServicePort;
import br.com.cams7.orders.core.port.out.GetCartItemsServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerAddressServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerCardServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerServicePort;
import br.com.cams7.orders.core.port.out.VerifyPaymentServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.web.reactive.server.WebTestClient;

public abstract class BaseIntegrationTests extends BaseTests {

  protected static final String REQUEST_TRACE_ID = "123";

  protected static String TIMESTAMP_ATTRIBUTE = "$.timestamp";
  protected static String PATH_ATTRIBUTE = "$.path";
  protected static String ERROR_ATTRIBUTE = "$.error";
  protected static String MESSAGE_ATTRIBUTE = "$.message";
  protected static String TRACE_ATTRIBUTE = "$.trace";
  protected static String REQUESTID_ATTRIBUTE = "$.requestId";
  protected static String EXCEPTION_ATTRIBUTE = "$.exception";

  protected static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

  protected static final String ERROR_MESSAGE = "Something wrong";

  @Autowired protected WebTestClient testClient;

  @Autowired protected ReactiveMongoOperations mongoOperations;

  @MockBean protected GetCustomerServicePort getCustomerService;
  @MockBean protected GetCustomerAddressServicePort getCustomerAddressService;
  @MockBean protected GetCustomerCardServicePort getCustomerCardService;
  @MockBean protected GetCartItemsServicePort getCartItemsService;
  @MockBean protected VerifyPaymentServicePort verifyPaymentService;
  @MockBean protected AddShippingOrderServicePort addShippingOrderService;

  protected void dropCollection(String country, String collectionName) {
    create(mongoOperations.dropCollection(getCollectionName(country, collectionName)))
        .verifyComplete();
  }

  protected static String getCollectionName(String country, String collectionName) {
    return getCollectionByCountry(country, collectionName);
  }
}
