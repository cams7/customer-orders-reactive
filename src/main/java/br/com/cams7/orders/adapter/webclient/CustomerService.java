package br.com.cams7.orders.adapter.webclient;

import static br.com.cams7.orders.adapter.commons.ApiConstants.COUNTRY_HEADER;
import static br.com.cams7.orders.adapter.commons.ApiConstants.REQUEST_TRACE_ID_HEADER;

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
public class CustomerService extends BaseWebclient
    implements GetCustomerServicePort, GetCustomerAddressServicePort, GetCustomerCardServicePort {

  private final WebClient.Builder builder;
  private final ModelMapper modelMapper;

  @Override
  public Mono<Customer> getCustomer(String customerUrl) {
    return getWebClient(builder, customerUrl)
        .get()
        .header(COUNTRY_HEADER, getCountry())
        .header(REQUEST_TRACE_ID_HEADER, getRequestTraceId())
        .retrieve()
        .bodyToMono(CustomerResponse.class)
        .map(this::getCustomer);
  }

  @Override
  public Mono<CustomerAddress> getCustomerAddress(String addressUrl) {
    return getWebClient(builder, addressUrl)
        .get()
        .header(COUNTRY_HEADER, getCountry())
        .header(REQUEST_TRACE_ID_HEADER, getRequestTraceId())
        .retrieve()
        .bodyToMono(CustomerAddressResponse.class)
        .map(this::getCustomerAddress);
  }

  @Override
  public Mono<CustomerCard> getCustomerCard(String cardUrl) {
    return getWebClient(builder, cardUrl)
        .get()
        .header(COUNTRY_HEADER, getCountry())
        .header(REQUEST_TRACE_ID_HEADER, getRequestTraceId())
        .retrieve()
        .bodyToMono(CustomerCardResponse.class)
        .map(this::getCustomerCard);
  }

  private Customer getCustomer(CustomerResponse response) {
    var customer = modelMapper.map(response, Customer.class);
    customer.setCustomerId(response.getId());
    return customer;
  }

  private CustomerAddress getCustomerAddress(CustomerAddressResponse response) {
    var customerAddress = modelMapper.map(response, CustomerAddress.class);
    customerAddress.setAddressId(response.getId());
    return customerAddress;
  }

  private CustomerCard getCustomerCard(CustomerCardResponse response) {
    var customerCard = modelMapper.map(response, CustomerCard.class);
    customerCard.setCardId(response.getId());
    return customerCard;
  }
}
