package br.com.cams7.orders.template.webclient;

import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_ADDRESS_RESPONSE;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_CITY;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_COUNTRY;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_FEDERATIVE_UNIT;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_ID;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_NUMBER;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_POSTCODE;
import static br.com.cams7.orders.template.domain.CustomerAddressTemplate.CUSTOMER_ADDRESS_STREET;
import static br.com.six2six.fixturefactory.Fixture.of;
import static lombok.AccessLevel.PRIVATE;

import br.com.cams7.orders.adapter.webclient.response.CustomerAddressResponse;
import br.com.six2six.fixturefactory.Rule;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class CustomerAddressResponseTemplate {

  public static void loadTemplates() {
    of(CustomerAddressResponse.class)
        .addTemplate(
            CUSTOMER_ADDRESS_RESPONSE,
            new Rule() {
              {
                add("id", CUSTOMER_ADDRESS_ID);
                add("number", CUSTOMER_ADDRESS_NUMBER);
                add("street", CUSTOMER_ADDRESS_STREET);
                add("postcode", CUSTOMER_ADDRESS_POSTCODE);
                add("city", CUSTOMER_ADDRESS_CITY);
                add("federativeUnit", CUSTOMER_ADDRESS_FEDERATIVE_UNIT);
                add("country", CUSTOMER_ADDRESS_COUNTRY);
              }
            });
  }
}
