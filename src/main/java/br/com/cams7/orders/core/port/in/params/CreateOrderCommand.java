package br.com.cams7.orders.core.port.in.params;

import br.com.cams7.orders.core.commons.SelfValidating;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class CreateOrderCommand extends SelfValidating<CreateOrderCommand> {

  private static final String URL_REGEX =
      "(^http(s)?\\:\\/\\/([\\w\\.\\-]+(\\:[0-9]+)?)\\/[a-z]+\\/[\\w\\-]+(\\/[a-z]+)?$)|(^$)|(^\\s+$)";

  @NotBlank(message = "Customer url must not be blank")
  @Pattern(regexp = URL_REGEX, message = "Invalid customer url")
  private final String customerUrl;

  @NotBlank(message = "Customer address url must not be blank")
  @Pattern(regexp = URL_REGEX, message = "Invalid customer address url")
  private final String addressUrl;

  @NotBlank(message = "Customer card url must not be blank")
  @Pattern(regexp = URL_REGEX, message = "Invalid customer card url")
  private final String cardUrl;

  @NotBlank(message = "Cart items url must not be blank")
  @Pattern(regexp = URL_REGEX, message = "Invalid cart items url")
  private final String itemsUrl;

  public CreateOrderCommand(
      String customerUrl, String addressUrl, String cardUrl, String itemsUrl) {
    super();
    this.customerUrl = customerUrl;
    this.addressUrl = addressUrl;
    this.cardUrl = cardUrl;
    this.itemsUrl = itemsUrl;
    this.validateSelf();
  }
}
