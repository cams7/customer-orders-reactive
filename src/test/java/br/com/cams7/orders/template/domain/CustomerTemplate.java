package br.com.cams7.orders.template.domain;

import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.core.domain.Customer;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class CustomerTemplate {

  public static final String CUSTOMER_ID = "703c327b-8b61-4f32-bf1a-fb3108a6f7e1";
  public static final String CUSTOMER_FULLNAME = "Gustavo Severino Tomás Ramos";
  public static final String CUSTOMER_USERNAME = "gustavo";

  public static void loadTemplates() {
    of(Customer.class)
        .addTemplate(
            CUSTOMER,
            new Rule() {
              {
                add("customerId", CUSTOMER_ID);
                add("fullName", CUSTOMER_FULLNAME);
                add("username", CUSTOMER_USERNAME);
              }
            });
  }
}
