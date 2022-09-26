package br.com.cams7.orders.adapter.webclient;

import static br.com.cams7.orders.template.CreateOrderRequestTemplate.ADDRESS_URL;
import static br.com.cams7.orders.template.CreateOrderRequestTemplate.CARD_URL;
import static br.com.cams7.orders.template.CreateOrderRequestTemplate.CUSTOMER_URL;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_ADDRESS;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_ADDRESS_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_CARD;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_CARD_RESPONSE;
import static br.com.cams7.orders.template.DomainTemplateLoader.CUSTOMER_RESPONSE;
import static br.com.six2six.fixturefactory.Fixture.from;
import static reactor.test.StepVerifier.create;

import br.com.cams7.orders.adapter.webclient.response.CustomerAddressResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerCardResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerResponse;
import br.com.cams7.orders.core.domain.Customer;
import br.com.cams7.orders.core.domain.CustomerAddress;
import br.com.cams7.orders.core.domain.CustomerCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests extends BaseWebClientTests {

  @InjectMocks private CustomerService customerService;

  @Spy private ModelMapper modelMapper = new ModelMapper();

  @Test
  @DisplayName("Should return customer when pass valid URL")
  void shouldReturnCustomerWhenPassValidURL() {
    CustomerResponse response = from(CustomerResponse.class).gimme(CUSTOMER_RESPONSE);
    Customer customer = from(Customer.class).gimme(CUSTOMER);

    mockWebClientForGet(Mono.just(response), CustomerResponse.class);

    create(customerService.getCustomer(CUSTOMER_URL))
        .expectSubscription()
        .expectNext(customer)
        .verifyComplete();
  }

  @Test
  @DisplayName("Should return customer address when pass valid URL")
  void shouldReturnCustomerAddressWhenPassValidURL() {
    CustomerAddressResponse response =
        from(CustomerAddressResponse.class).gimme(CUSTOMER_ADDRESS_RESPONSE);
    CustomerAddress customerAddress = from(CustomerAddress.class).gimme(CUSTOMER_ADDRESS);

    mockWebClientForGet(Mono.just(response), CustomerAddressResponse.class);

    create(customerService.getCustomerAddress(ADDRESS_URL))
        .expectSubscription()
        .expectNext(customerAddress)
        .verifyComplete();
  }

  @Test
  @DisplayName("Should return customer card when pass valid URL")
  void shouldReturnCustomerCardWhenPassValidURL() {
    CustomerCardResponse response = from(CustomerCardResponse.class).gimme(CUSTOMER_CARD_RESPONSE);
    CustomerCard customercard = from(CustomerCard.class).gimme(CUSTOMER_CARD);

    mockWebClientForGet(Mono.just(response), CustomerCardResponse.class);

    create(customerService.getCustomerCard(CARD_URL))
        .expectSubscription()
        .expectNext(customercard)
        .verifyComplete();
  }
}
