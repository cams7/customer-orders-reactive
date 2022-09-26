package br.com.cams7.orders.template.domain;

import static br.com.cams7.orders.template.DomainTemplateLoader.AUTHORISED_PAYMENT;
import static br.com.cams7.orders.template.DomainTemplateLoader.DECLINED_PAYMENT;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.core.domain.Payment;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class PaymentTemplate {

  public static final boolean AUTHORISED = true;
  public static final String AUTHORISED_MESSAGE = "Payment authorised";
  public static final boolean DECLINED = false;
  public static final String DECLINED_MESSAGE = "Payment declined: amount exceeds 100.00";

  public static void loadTemplates() {
    of(Payment.class)
        .addTemplate(
            AUTHORISED_PAYMENT,
            new Rule() {
              {
                add("authorised", AUTHORISED);
                add("message", AUTHORISED_MESSAGE);
              }
            })
        .addTemplate(
            DECLINED_PAYMENT,
            new Rule() {
              {
                add("authorised", DECLINED);
                add("message", DECLINED_MESSAGE);
              }
            });
  }
}
