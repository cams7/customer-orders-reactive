package br.com.cams7.orders.template.webclient;

import static br.com.cams7.orders.template.DomainTemplateLoader.SHIPPING_REQUEST;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.ORDER_ID;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.adapter.webclient.request.ShippingRequest;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class ShippingRequestTemplate {

  public static void loadTemplates() {
    of(ShippingRequest.class)
        .addTemplate(
            SHIPPING_REQUEST,
            new Rule() {
              {
                add("orderId", ORDER_ID);
              }
            });
  }
}
