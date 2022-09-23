package br.com.cams7.orders.adapter.configuration;

import br.com.cams7.orders.core.GetOrdersUseCase;
import br.com.cams7.orders.core.port.in.GetOrdersUseCasePort;
import br.com.cams7.orders.core.port.out.GetOrdersRepositoryPort;
import br.com.cams7.orders.core.utils.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

  @Autowired private ZonePropertiesWithValues zoneProperties;

  @Bean
  ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  DateUtils dateUtils() {
    return new DateUtils(zoneProperties);
  }

  @Bean
  GetOrdersUseCasePort getOrdersUseCase(GetOrdersRepositoryPort getOrdersRepository) {
    return new GetOrdersUseCase(getOrdersRepository);
  }
}
