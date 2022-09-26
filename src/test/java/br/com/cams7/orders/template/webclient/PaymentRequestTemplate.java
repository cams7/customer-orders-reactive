package br.com.cams7.orders.template.webclient;

import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_PAYMENT_REQUEST;
import static br.com.cams7.orders.template.DomainTemplateLoader.DECLINED_PAYMENT_REQUEST;
import static br.com.cams7.orders.template.domain.CustomerTemplate.CUSTOMER_ID;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.AUTHORISED_TOTAL_AMOUNT;
import static br.com.cams7.orders.template.domain.OrderEntityTemplate.DECLINED_TOTAL_AMOUNT;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.adapter.webclient.request.PaymentRequest;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class PaymentRequestTemplate {

  public static void loadTemplates() {
    of(PaymentRequest.class)
        .addTemplate(
            AUTHORISED_PAYMENT_REQUEST,
            new Rule() {
              {
                add("customerId", CUSTOMER_ID);
                add("amount", AUTHORISED_TOTAL_AMOUNT);
              }
            })
        .addTemplate(
            DECLINED_PAYMENT_REQUEST,
            new Rule() {
              {
                add("customerId", CUSTOMER_ID);
                add("amount", DECLINED_TOTAL_AMOUNT);
              }
            });
  }
}
