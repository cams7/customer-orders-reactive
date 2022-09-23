package br.com.cams7.orders.adapter.repository.utils;

public final class DatabaseCollectionUtils {

  public static String getCollectionByCountry(final String country, final String collectionName) {
    return String.format("%s-%s", country.toUpperCase(), collectionName);
  }
}
