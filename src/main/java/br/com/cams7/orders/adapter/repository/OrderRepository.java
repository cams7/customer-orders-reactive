package br.com.cams7.orders.adapter.repository;

import static br.com.cams7.orders.adapter.repository.model.OrderModel.COLLECTION_NAME;
import static br.com.cams7.orders.adapter.repository.utils.DatabaseCollectionUtils.getCollectionByCountry;

import br.com.cams7.orders.adapter.repository.model.OrderModel;
import br.com.cams7.orders.core.domain.OrderEntity;
import br.com.cams7.orders.core.port.out.CreateOrderRepositoryPort;
import br.com.cams7.orders.core.port.out.DeleteOrderByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.GetOrderByIdRepositoryPort;
import br.com.cams7.orders.core.port.out.GetOrdersByCountryRepositoryPort;
import br.com.cams7.orders.core.port.out.UpdateShippingByIdRepositoryPort;
import br.com.cams7.orders.core.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class OrderRepository
    implements GetOrdersByCountryRepositoryPort,
        GetOrderByIdRepositoryPort,
        DeleteOrderByIdRepositoryPort,
        CreateOrderRepositoryPort,
        UpdateShippingByIdRepositoryPort {

  private final DateUtils dateUtils;
  private final ReactiveMongoOperations mongoOperations;
  private final ModelMapper modelMapper;

  @Override
  public Flux<OrderEntity> getOrders(final String country) {
    final var query = new Query(Criteria.where("address.country").is(country));
    return mongoOperations
        .find(query, OrderModel.class, getCollectionName(country))
        .map(order -> getOrder(country, order));
  }

  @Override
  public Mono<OrderEntity> getOrder(final String country, final String orderId) {
    return mongoOperations
        .findById(orderId, OrderModel.class, getCollectionName(country))
        .map(order -> getOrder(country, order));
  }

  @Override
  public Mono<Long> delete(final String country, final String orderId) {
    final var query = new Query(Criteria.where("id").is(orderId));
    return mongoOperations
        .remove(query, OrderModel.class, getCollectionName(country))
        .map(data -> data.getDeletedCount());
  }

  @Override
  public Mono<OrderEntity> create(final String country, final OrderEntity order) {
    return mongoOperations
        .insert(getOrder(order), getCollectionName(country))
        .map(createdOrder -> getOrder(country, createdOrder));
  }

  @Override
  public Mono<Long> updateShipping(
      final String country, final String orderId, final Boolean registeredShipping) {
    final var query = new Query(Criteria.where("id").is(orderId));
    final var update = new Update();
    update.set("registeredShipping", registeredShipping);
    return mongoOperations
        .updateFirst(query, update, OrderModel.class, getCollectionName(country))
        .map(result -> result.getModifiedCount());
  }

  private OrderEntity getOrder(final String country, final OrderModel model) {
    final var entity =
        modelMapper
            .map(model, OrderEntity.class)
            .withOrderId(model.getId())
            .withTotalAmount(model.getTotal())
            .withRegistrationDate(dateUtils.getZonedDateTime(country, model.getRegistrationDate()));
    return entity;
  }

  private OrderModel getOrder(final OrderEntity entity) {
    final var model = modelMapper.map(entity, OrderModel.class);
    model.setId(entity.getOrderId());
    model.setTotal(entity.getTotalAmount());
    model.setRegistrationDate(entity.getRegistrationDate().toLocalDateTime());
    return model;
  }

  private static String getCollectionName(final String country) {
    return getCollectionByCountry(country, COLLECTION_NAME);
  }
}
