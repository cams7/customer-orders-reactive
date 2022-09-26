package br.com.cams7.orders.template.domain;

import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM1;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM2;
import static br.com.cams7.orders.template.DomainTemplateLoader.CART_ITEM3;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.core.domain.CartItem;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class CartItemTemplate {

  public static final String CART_ITEM1_PRODUCT_ID = "9aff4cc5-f921-4157-8976-41ceae93ae54";
  public static final int CART_ITEM1_QUANTITY = 3;
  public static final float CART_ITEM1_UNIT_PRICE = 18.0f;
  public static final String CART_ITEM2_PRODUCT_ID = "e67ef6e3-10f5-42dd-8b98-dd0d793ca2fa";
  public static final int CART_ITEM2_QUANTITY = 2;
  public static final float CART_ITEM2_UNIT_PRICE = 35.0f;
  public static final String CART_ITEM3_PRODUCT_ID = "2c22ea64-39f1-474b-92d2-b92684dedaa0";
  public static final int CART_ITEM3_QUANTITY = 1;
  public static final float CART_ITEM3_UNIT_PRICE = 27.0f;

  public static void loadTemplates() {
    of(CartItem.class)
        .addTemplate(
            CART_ITEM1,
            new Rule() {
              {
                add("productId", CART_ITEM1_PRODUCT_ID);
                add("quantity", CART_ITEM1_QUANTITY);
                add("unitPrice", CART_ITEM1_UNIT_PRICE);
              }
            })
        .addTemplate(
            CART_ITEM2,
            new Rule() {
              {
                add("productId", CART_ITEM2_PRODUCT_ID);
                add("quantity", CART_ITEM2_QUANTITY);
                add("unitPrice", CART_ITEM2_UNIT_PRICE);
              }
            })
        .addTemplate(
            CART_ITEM3,
            new Rule() {
              {
                add("productId", CART_ITEM3_PRODUCT_ID);
                add("quantity", CART_ITEM3_QUANTITY);
                add("unitPrice", CART_ITEM3_UNIT_PRICE);
              }
            });
  }
}
