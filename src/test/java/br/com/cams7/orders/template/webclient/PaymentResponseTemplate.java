package br.com.cams7.orders.template.webclient;

import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_PAYMENT_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.DECLINED_PAYMENT_RESPONSE;
import static br.com.cams7.orders.template.domain.PaymentTemplate.AUTHORISED;
import static br.com.cams7.orders.template.domain.PaymentTemplate.AUTHORISED_MESSAGE;
import static br.com.cams7.orders.template.domain.PaymentTemplate.DECLINED;
import static br.com.cams7.orders.template.domain.PaymentTemplate.DECLINED_MESSAGE;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.adapter.webclient.response.PaymentResponse;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class PaymentResponseTemplate {

  public static void loadTemplates() {
    of(PaymentResponse.class)
        .addTemplate(
            AUTHORISED_PAYMENT_RESPONSE,
            new Rule() {
              {
                add("authorised", AUTHORISED);
                add("message", AUTHORISED_MESSAGE);
              }
            })
        .addTemplate(
            DECLINED_PAYMENT_RESPONSE,
            new Rule() {
              {
                add("authorised", DECLINED);
                add("message", DECLINED_MESSAGE);
              }
            });
  }
}
