package br.com.cams7.orders.template.webclient;

import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE1;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE2;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM_RESPONSE3;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM1_PRODUCT_ID;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM1_QUANTITY;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM1_UNIT_PRICE;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM2_PRODUCT_ID;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM2_QUANTITY;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM2_UNIT_PRICE;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM3_PRODUCT_ID;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM3_QUANTITY;
import static br.com.cams7.orders.template.domain.CartItemTemplate.CART_ITEM3_UNIT_PRICE;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.adapter.webclient.response.CartItemResponse;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class CartItemResponseTemplate {

  public static final String CART_ITEM1_ID = "06e54a69-1179-4fa2-9997-8a883d9e30e8";
  public static final String CART_ITEM2_ID = "ad5a7ad8-112a-4af2-83a1-c4596d3eb581";
  public static final String CART_ITEM3_ID = "9231f141-47cb-475f-9df9-b0f5e8d32c7e";

  public static void loadTemplates() {
    of(CartItemResponse.class)
        .addTemplate(
            CART_ITEM_RESPONSE1,
            new Rule() {
              {
                add("id", CART_ITEM1_ID);
                add("productId", CART_ITEM1_PRODUCT_ID);
                add("quantity", CART_ITEM1_QUANTITY);
                add("unitPrice", CART_ITEM1_UNIT_PRICE);
              }
            })
        .addTemplate(
            CART_ITEM_RESPONSE2,
            new Rule() {
              {
                add("id", CART_ITEM2_ID);
                add("productId", CART_ITEM2_PRODUCT_ID);
                add("quantity", CART_ITEM2_QUANTITY);
                add("unitPrice", CART_ITEM2_UNIT_PRICE);
              }
            })
        .addTemplate(
            CART_ITEM_RESPONSE3,
            new Rule() {
              {
                add("id", CART_ITEM3_ID);
                add("productId", CART_ITEM3_PRODUCT_ID);
                add("quantity", CART_ITEM3_QUANTITY);
                add("unitPrice", CART_ITEM3_UNIT_PRICE);
              }
            });
  }
}
