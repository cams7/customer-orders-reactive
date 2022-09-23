package br.com.cams7.orders.core.utils;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.Objects.isNull;

import br.com.cams7.orders.core.port.out.properties.ZoneProperties;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public final class DateUtils {
  private final ZoneProperties zoneProperties;

  public ZonedDateTime getZonedDateTime(String country) {
    var localDateTime = LocalDateTime.now();
    return getZonedDateTime(country, localDateTime);
  }

  public ZonedDateTime getZonedDateTime(String country, LocalDateTime localDateTime) {
    return getZonedDateTime(
        localDateTime, getZoneOffset(zoneProperties.getIds(), country, localDateTime));
  }

  private static ZonedDateTime getZonedDateTime(
      LocalDateTime localDateTime, ZoneOffset zoneOffset) {
    if (isNull(localDateTime)) return null;
    return ZonedDateTime.of(localDateTime, zoneOffset);
  }

  private static ZoneOffset getZoneOffset(
      Map<String, String> zoneIds, String country, LocalDateTime localDateTime) {
    if (MapUtils.isEmpty(zoneIds)
        || StringUtils.isEmpty(country)
        || isNull(localDateTime)
        || !zoneIds.containsKey(country)) return UTC;

    return ZoneId.of(zoneIds.get(country)).getRules().getOffset(localDateTime);
  }

  public static String getFormattedDateTime(ZonedDateTime dateTime) {
    if (isNull(dateTime)) return null;
    return dateTime.format(ISO_DATE_TIME);
  }
}
