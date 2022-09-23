package br.com.cams7.orders.template;

import br.com.six2six.fixturefactory.loader.TemplateLoader;

public final class DomainTemplateLoader implements TemplateLoader {

  public static final String ORDER_ENTITY = "ORDER_ENTITY";
  public static final String ORDER_MODEL = "ORDER_MODEL";
  public static final String ORDER_RESPONSE = "ORDER_RESPONSE";

  @Override
  public void load() {
    OrderEntityTemplate.loadTemplates();
    OrderModelTemplate.loadTemplates();
    OrderResponseTemplate.loadTemplates();
  }
}
