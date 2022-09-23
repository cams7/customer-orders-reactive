package br.com.cams7.orders.adapter.configuration;

import br.com.cams7.orders.core.port.out.properties.ZoneProperties;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EqualsAndHashCode(callSuper = true)
@Getter
@Configuration
@ConfigurationProperties(prefix = "zone", ignoreInvalidFields = true)
public class ZonePropertiesWithValues extends ZoneProperties {
  @Value("#{{${zone.ids}}}")
  private Map<String, String> ids;
}
