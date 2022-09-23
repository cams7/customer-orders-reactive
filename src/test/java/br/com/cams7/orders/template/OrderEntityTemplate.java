package br.com.cams7.orders.template;

import static br.com.cams7.orders.template.DomainTemplateLoader.ORDER_ENTITY;
import static br.com.six2six.fixturefactory.Fixture.of;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.core.domain.CartItem;
import br.com.cams7.orders.core.domain.Customer;
import br.com.cams7.orders.core.domain.CustomerAddress;
import br.com.cams7.orders.core.domain.CustomerCard;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.six2six.fixturefactory.Rule;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class OrderEntityTemplate {

  public static final String ORDER_ID = "57a98d98e4b00679b4a830af";
  public static final String CUSTOMER_ID = "703c327b-8b61-4f32-bf1a-fb3108a6f7e1";
  public static final String CUSTOMER_FULLNAME = "Gustavo Severino Tomás Ramos";
  public static final String CUSTOMER_USERNAME = "gustavo";
  public static final String CUSTOMER_ADDRESS_ID = "a679084a-03e7-49c7-9516-5a7d6757e1c2";
  public static final String CUSTOMER_ADDRESS_NUMBER = "956";
  public static final String CUSTOMER_ADDRESS_STREET = "Rua Manoel Gregório Mattos";
  public static final String CUSTOMER_ADDRESS_POSTCODE = "89816-170";
  public static final String CUSTOMER_ADDRESS_CITY = "Chapecó";
  public static final String CUSTOMER_ADDRESS_FEDERATIVE_UNIT = "SC";
  public static final String CUSTOMER_ADDRESS_COUNTRY = "BR";
  public static final String CUSTOMER_CARD_ID = "818c8544-dfb1-49b2-8212-eb9dcdbd57c9";
  public static final String CUSTOMER_CARD_LONG_NUM = "5413 0962 7910 9654";
  public static final String CART_ITEM1_PRODUCT_ID = "9aff4cc5-f921-4157-8976-41ceae93ae54";
  public static final int CART_ITEM1_QUANTITY = 3;
  public static final float CART_ITEM1_UNIT_PRICE = 18.0f;
  public static final String CART_ITEM2_PRODUCT_ID = "e67ef6e3-10f5-42dd-8b98-dd0d793ca2fa";
  public static final int CART_ITEM2_QUANTITY = 2;
  public static final float CART_ITEM2_UNIT_PRICE = 35.0f;
  public static final String CART_ITEM3_PRODUCT_ID = "2c22ea64-39f1-474b-92d2-b92684dedaa0";
  public static final int CART_ITEM3_QUANTITY = 1;
  public static final float CART_ITEM3_UNIT_PRICE = 27.0f;
  public static final String REGISTRATION_DATE = "2022-09-22T17:46:53";
  public static final float TOTAL_AMOUNT = 161.55f;

  public static void loadTemplates() {
    of(OrderEntity.class)
        .addTemplate(
            ORDER_ENTITY,
            new Rule() {
              {
                add("orderId", ORDER_ID);
                add("customer", new Customer(CUSTOMER_ID, CUSTOMER_FULLNAME, CUSTOMER_USERNAME));
                add(
                    "address",
                    new CustomerAddress(
                        CUSTOMER_ADDRESS_ID,
                        CUSTOMER_ADDRESS_NUMBER,
                        CUSTOMER_ADDRESS_STREET,
                        CUSTOMER_ADDRESS_POSTCODE,
                        CUSTOMER_ADDRESS_CITY,
                        CUSTOMER_ADDRESS_FEDERATIVE_UNIT,
                        CUSTOMER_ADDRESS_COUNTRY));
                add("card", new CustomerCard(CUSTOMER_CARD_ID, CUSTOMER_CARD_LONG_NUM));
                add(
                    "items",
                    List.of(
                        new CartItem(
                            CART_ITEM1_PRODUCT_ID, CART_ITEM1_QUANTITY, CART_ITEM1_UNIT_PRICE),
                        new CartItem(
                            CART_ITEM2_PRODUCT_ID, CART_ITEM2_QUANTITY, CART_ITEM2_UNIT_PRICE),
                        new CartItem(
                            CART_ITEM3_PRODUCT_ID, CART_ITEM3_QUANTITY, CART_ITEM3_UNIT_PRICE)));
                add(
                    "registrationDate",
                    ZonedDateTime.of(LocalDateTime.parse(REGISTRATION_DATE, ISO_DATE_TIME), UTC));
                add("totalAmount", TOTAL_AMOUNT);
              }
            });
  }
}
