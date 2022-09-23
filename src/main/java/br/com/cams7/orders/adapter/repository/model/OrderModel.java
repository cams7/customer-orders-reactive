package br.com.cams7.orders.adapter.repository.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class OrderModel {

  public static final String COLLECTION_NAME = "orders";

  @Id private String id;

  private Customer customer;

  private CustomerAddress address;

  private CustomerCard card;

  private List<CartItem> items;

  private LocalDateTime registrationDate;

  private Float total;
}
