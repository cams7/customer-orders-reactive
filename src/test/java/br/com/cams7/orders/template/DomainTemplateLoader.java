package br.com.cams7.orders.template;

import br.com.cams7.orders.template.domain.CartItemTemplate;
import br.com.cams7.orders.template.domain.CustomerAddressTemplate;
import br.com.cams7.orders.template.domain.CustomerCardTemplate;
import br.com.cams7.orders.template.domain.CustomerTemplate;
import br.com.cams7.orders.template.domain.OrderEntityTemplate;
import br.com.cams7.orders.template.domain.PaymentTemplate;
import br.com.cams7.orders.template.webclient.CartItemResponseTemplate;
import br.com.cams7.orders.template.webclient.CustomerAddressResponseTemplate;
import br.com.cams7.orders.template.webclient.CustomerCardResponseTemplate;
import br.com.cams7.orders.template.webclient.CustomerResponseTemplate;
import br.com.cams7.orders.template.webclient.PaymentRequestTemplate;
import br.com.cams7.orders.template.webclient.PaymentResponseTemplate;
import br.com.cams7.orders.template.webclient.ShippingRequestTemplate;
import br.com.cams7.orders.template.webclient.ShippingResponseTemplate;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public final class DomainTemplateLoader implements TemplateLoader {
  public static final String CUSTOMER = "CUSTOMER";
  public static final String CUSTOMER_ADDRESS = "CUSTOMER_ADDRESS";
  public static final String CUSTOMER_CARD = "CUSTOMER_CARD";
  public static final String CART_ITEM1 = "CART_ITEM1";
  public static final String CART_ITEM2 = "CART_ITEM2";
  public static final String CART_ITEM3 = "CART_ITEM3";
  public static final String AUTHORISED_PAYMENT = "AUTHORISED_PAYMENT";
  public static final String DECLINED_PAYMENT = "DECLINED_PAYMENT";
  public static final String DECLINED_ORDER_ENTITY = "DECLINED_ORDER_ENTITY";
  public static final String AUTHORISED_ORDER_ENTITY = "AUTHORISED_ORDER_ENTITY";
  public static final String ORDER_MODEL = "ORDER_MODEL";
  public static final String ORDER_RESPONSE = "ORDER_RESPONSE";
  public static final String VALID_CREATE_ORDER_REQUEST = "VALID_CREATE_ORDER_REQUEST";
  public static final String INVALID_CREATE_ORDER_REQUEST = "INVALID_CREATE_ORDER_REQUEST";
  public static final String CUSTOMER_RESPONSE = "CUSTOMER_RESPONSE";
  public static final String CUSTOMER_ADDRESS_RESPONSE = "CUSTOMER_ADDRESS_RESPONSE";
  public static final String CUSTOMER_CARD_RESPONSE = "CUSTOMER_CARD_RESPONSE";
  public static final String CART_ITEM_RESPONSE1 = "CART_ITEM_RESPONSE1";
  public static final String CART_ITEM_RESPONSE2 = "CART_ITEM_RESPONSE2";
  public static final String CART_ITEM_RESPONSE3 = "CART_ITEM_RESPONSE3";
  public static final String AUTHORISED_PAYMENT_REQUEST = "AUTHORISED_PAYMENT_REQUEST";
  public static final String DECLINED_PAYMENT_REQUEST = "DECLINED_PAYMENT_REQUEST";
  public static final String AUTHORISED_PAYMENT_RESPONSE = "AUTHORISED_PAYMENT_RESPONSE";
  public static final String DECLINED_PAYMENT_RESPONSE = "DECLINED_PAYMENT_RESPONSE";
  public static final String SHIPPING_REQUEST = "SHIPPING_REQUEST";
  public static final String SHIPPING_RESPONSE = "SHIPPING_RESPONSE";

  @Override
  public void load() {
    CustomerTemplate.loadTemplates();
    CustomerAddressTemplate.loadTemplates();
    CustomerCardTemplate.loadTemplates();
    CartItemTemplate.loadTemplates();
    PaymentTemplate.loadTemplates();
    OrderEntityTemplate.loadTemplates();
    OrderModelTemplate.loadTemplates();
    OrderResponseTemplate.loadTemplates();
    CreateOrderRequestTemplate.loadTemplates();
    CustomerResponseTemplate.loadTemplates();
    CustomerAddressResponseTemplate.loadTemplates();
    CustomerCardResponseTemplate.loadTemplates();
    CartItemResponseTemplate.loadTemplates();
    PaymentRequestTemplate.loadTemplates();
    PaymentResponseTemplate.loadTemplates();
    ShippingRequestTemplate.loadTemplates();
    ShippingResponseTemplate.loadTemplates();
  }
}
