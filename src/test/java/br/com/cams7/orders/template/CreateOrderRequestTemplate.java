package br.com.cams7.orders.template;

import static br.com.cams7.orders.template.DomainTemplateLoader.INVALID_CREATE_ORDER_REQUEST;
import static br.com.cams7.orders.template.DomainTemplateLoader.VALID_CREATE_ORDER_REQUEST;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_ID;
import static br.com.cams7.orders.template.domain.CustomerCardTemplate.CUSTOMER_CARD_ID;
import static br.com.cams7.orders.template.domain.CustomerTemplate.CUSTOMER_ID;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.adapter.controller.request.CreateOrderRequest;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class CreateOrderRequestTemplate {

  public static final String CUSTOMER_URL = String.format("http://test/customers/%s", CUSTOMER_ID);
  public static final String ADDRESS_URL =
      String.format("http://test/addresses/%s", CUSTOMER_ADDRESS_ID);
  public static final String CARD_URL = String.format("http://test/cards/%s", CUSTOMER_CARD_ID);
  public static final String ITEMS_URL =
      "http://test/carts/bad2ba11-19b2-4af8-b41e-dccc47f69929/items";
  public static final String INVALID_URL = "http://test";

  public static void loadTemplates() {
    of(CreateOrderRequest.class)
        .addTemplate(
            VALID_CREATE_ORDER_REQUEST,
            new Rule() {
              {
                add("customerUrl", CUSTOMER_URL);
                add("addressUrl", ADDRESS_URL);
                add("cardUrl", CARD_URL);
                add("itemsUrl", ITEMS_URL);
              }
            })
        .addTemplate(INVALID_CREATE_ORDER_REQUEST)
        .inherits(
            VALID_CREATE_ORDER_REQUEST,
            new Rule() {
              {
                add("customerUrl", INVALID_URL);
              }
            });
  }
}
