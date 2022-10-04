package br.com.cams7.orders.core.port.in.params;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateOrderCommandTests {

  @Test
  @DisplayName("Should create command instance when pass valid params")
  void shouldCreateCommandInstanceWhenPassValidParams() {
    var customerUrl = "http://localhost/customers/703c327b-8b61-4f32-bf1a-fb3108a6f7e1";
    var addressUrl = "http://localhost:3000/addresses/a679084a-03e7-49c7-9516-5a7d6757e1c2";
    var cardUrl = "https://customers/cards/818c8544-dfb1-49b2-8212-eb9dcdbd57c9";
    var itemsUrl = "http://carts.test.com/carts/bad2ba11-19b2-4af8-b41e-dccc47f69929/items";

    new CreateOrderCommand(customerUrl, addressUrl, cardUrl, itemsUrl);
  }

  @Test
  @DisplayName("Shouldn't create command instance when pass null params")
  void shouldNotCreateCommandInstanceWhenPassNullParams() {
    var exception =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new CreateOrderCommand(null, null, null, null);
            });
    assertThat(exception.getMessage())
        .containsAnyOf("addressUrl: Customer address url must not be blank");
    assertThat(exception.getMessage()).containsAnyOf("customerUrl: Customer url must not be blank");
    assertThat(exception.getMessage()).containsAnyOf("itemsUrl: Cart items url must not be blank");
    assertThat(exception.getMessage())
        .containsAnyOf("cardUrl: Customer card url must not be blank");
  }

  @Test
  @DisplayName("Shouldn't create command instance when pass empty params")
  void shouldNotCreateCommandInstanceWhenPassEmptyParams() {
    var emptyString = "";
    var exception =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new CreateOrderCommand(emptyString, emptyString, emptyString, emptyString);
            });
    assertThat(exception.getMessage())
        .containsAnyOf("addressUrl: Customer address url must not be blank");
    assertThat(exception.getMessage()).containsAnyOf("customerUrl: Customer url must not be blank");
    assertThat(exception.getMessage()).containsAnyOf("itemsUrl: Cart items url must not be blank");
    assertThat(exception.getMessage())
        .containsAnyOf("cardUrl: Customer card url must not be blank");
  }

  @Test
  @DisplayName("Shouldn't create command instance when pass blank params")
  void shouldNotCreateCommandInstanceWhenPassBlankParams() {
    var blankString = " ";
    var exception =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new CreateOrderCommand(blankString, blankString, blankString, blankString);
            });
    assertThat(exception.getMessage())
        .containsAnyOf("addressUrl: Customer address url must not be blank");
    assertThat(exception.getMessage()).containsAnyOf("customerUrl: Customer url must not be blank");
    assertThat(exception.getMessage()).containsAnyOf("itemsUrl: Cart items url must not be blank");
    assertThat(exception.getMessage())
        .containsAnyOf("cardUrl: Customer card url must not be blank");
  }

  @Test
  @DisplayName("Shouldn't create command instance when pass invalid URLs")
  void shouldNotCreateCommandInstanceWhenPassInvalidUrls() {
    var invalidUrl = "http://wrong/test";
    var exception =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new CreateOrderCommand(invalidUrl, invalidUrl, invalidUrl, invalidUrl);
            });
    assertThat(exception.getMessage()).containsAnyOf("addressUrl: Invalid customer address url");
    assertThat(exception.getMessage()).containsAnyOf("customerUrl: Invalid customer url");
    assertThat(exception.getMessage()).containsAnyOf("itemsUrl: Invalid cart items url");
    assertThat(exception.getMessage()).containsAnyOf("cardUrl: Invalid customer card url");
  }
}
