package br.com.cams7.orders.adapter.repository;

import static br.com.cams7.orders.adapter.repository.model.OrderModel.COLLECTION_NAME;
import static br.com.cams7.orders.adapter.repository.utils.DatabaseCollectionUtils.getCollectionByCountry;

import br.com.cams7.orders.adapter.repository.model.OrderModel;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.out.GetOrderByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.GetOrdersByCountryRepositoryPort;
import br.com.cams7.orders.core.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class OrderRepository
    implements GetOrdersByCountryRepositoryPort, GetOrderByIdRepositoryPort {

  private final DateUtils dateUtils;
  private final ReactiveMongoOperations mongoOperations;
  private final ModelMapper modelMapper;

  @Override
  public Flux<OrderEntity> getOrders(String country) {
    Query query = new Query(Criteria.where("address.country").is(country));
    return mongoOperations
        .find(query, OrderModel.class, getCollectionName(country))
        .map(this::getOrder);
  }

  @Override
  public Mono<OrderEntity> getOrder(String country, String orderId) {
    return mongoOperations
        .findById(orderId, OrderModel.class, getCollectionName(country))
        .map(this::getOrder);
  }

  private OrderEntity getOrder(OrderModel model) {
    var country = model.getAddress().getCountry();
    var entity =
        modelMapper
            .map(model, OrderEntity.class)
            .withOrderId(model.getId())
            .withTotalAmount(model.getTotal())
            .withRegistrationDate(dateUtils.getZonedDateTime(country, model.getRegistrationDate()));
    return entity;
  }

  private String getCollectionName(String country) {
    return getCollectionByCountry(country, COLLECTION_NAME);
  }
}
