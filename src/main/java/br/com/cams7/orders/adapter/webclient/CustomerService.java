package br.com.cams7.orders.adapter.webclient;

import br.com.cams7.orders.adapter.webclient.response.CustomerAddressResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerCardResponse;
import br.com.cams7.orders.adapter.webclient.response.CustomerResponse;
import br.com.cams7.orders.core.domain.Customer;
import br.com.cams7.orders.core.domain.CustomerAddress;
import br.com.cams7.orders.core.domain.CustomerCard;
import br.com.cams7.orders.core.port.out.GetCustomerAddressServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerCardServicePort;
import br.com.cams7.orders.core.port.out.GetCustomerServicePort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService
    implements GetCustomerServicePort, GetCustomerAddressServicePort, GetCustomerCardServicePort {

  private final WebClient.Builder builder;
  private final ModelMapper modelMapper;

  @Override
  public Mono<Customer> getCustomer(String customerUrl) {
    return getWebClient(builder, customerUrl)
        .get()
        .retrieve()
        .bodyToMono(CustomerResponse.class)
        .map(this::getCustomer);
  }

  @Override
  public Mono<CustomerAddress> getCustomerAddress(String addressUrl) {
    return getWebClient(builder, addressUrl)
        .get()
        .retrieve()
        .bodyToMono(CustomerAddressResponse.class)
        .map(this::getCustomerAddress);
  }

  @Override
  public Mono<CustomerCard> getCustomerCard(String cardUrl) {
    return getWebClient(builder, cardUrl)
        .get()
        .retrieve()
        .bodyToMono(CustomerCardResponse.class)
        .map(this::getCustomerCard);
  }

  private static WebClient getWebClient(WebClient.Builder builder, String url) {
    return builder.baseUrl(url).build();
  }

  private Customer getCustomer(CustomerResponse response) {
    return modelMapper.map(response, Customer.class);
  }

  private CustomerAddress getCustomerAddress(CustomerAddressResponse response) {
    return modelMapper.map(response, CustomerAddress.class);
  }

  private CustomerCard getCustomerCard(CustomerCardResponse response) {
    return modelMapper.map(response, CustomerCard.class);
  }
}
