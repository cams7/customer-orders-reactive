package br.com.cams7.orders.arch;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static lombok.AccessLevel.PRIVATE;

import com.tngtech.archunit.library.Architectures.LayeredArchitecture;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
final class ArchUnitHelper {
  public static final String SPRINGFRAMEWORK_PACKAGE = "org.springframework";

  public static final String ALL_CLASSES_PACKAGE = "br.com.cams7.orders";

  public static final String CORE_PACKAGE = ALL_CLASSES_PACKAGE + ".core";
  public static final String USECASE_PACKAGE = CORE_PACKAGE;
  public static final String CORE_COMMONS_PACKAGE = CORE_PACKAGE + ".commons";
  public static final String DOMAIN_PACKAGE = CORE_PACKAGE + ".domain";
  public static final String PORT_PACKAGE = CORE_PACKAGE + ".port";
  public static final String PORT_IN_PACKAGE = PORT_PACKAGE + ".in";
  public static final String PORT_IN_PARAMS_PACKAGE = PORT_IN_PACKAGE + ".params";
  public static final String PORT_OUT_PACKAGE = PORT_PACKAGE + ".out";
  public static final String PORT_OUT_EXCEPTION_PACKAGE = PORT_OUT_PACKAGE + ".exception";
  public static final String PORT_OUT_PROPERTIES_PACKAGE = PORT_OUT_PACKAGE + ".properties";
  public static final String UTILS_PACKAGE = CORE_PACKAGE + ".utils";

  public static final String ADAPTER_PACKAGE = ALL_CLASSES_PACKAGE + ".adapter";
  public static final String ADAPTER_COMMONS_PACKAGE = ADAPTER_PACKAGE + ".commons";
  public static final String ADAPTER_CONFIGURATION_PACKAGE = ADAPTER_PACKAGE + ".configuration";
  public static final String CONTROLLER_PACKAGE = ADAPTER_PACKAGE + ".controller";
  public static final String CONTROLLER_REQUEST_PACKAGE = CONTROLLER_PACKAGE + ".request";
  public static final String CONTROLLER_RESPONSE_PACKAGE = CONTROLLER_PACKAGE + ".response";
  public static final String REPOSITORY_PACKAGE = ADAPTER_PACKAGE + ".repository";
  public static final String REPOSITORY_MODEL_PACKAGE = REPOSITORY_PACKAGE + ".model";
  public static final String REPOSITORY_UTILS_PACKAGE = REPOSITORY_PACKAGE + ".utils";
  public static final String WEBCLIENT_PACKAGE = ADAPTER_PACKAGE + ".webclient";
  public static final String WEBCLIENT_REQUEST_PACKAGE = WEBCLIENT_PACKAGE + ".request";
  public static final String WEBCLIENT_RESPONSE_PACKAGE = WEBCLIENT_PACKAGE + ".response";

  public static final String SPRINGFRAMEWORK_LAYER_PATH = SPRINGFRAMEWORK_PACKAGE + "..";
  public static final String CORE_LAYER_PATH = CORE_PACKAGE + "..";
  public static final String DOMAIN_LAYER_PATH = DOMAIN_PACKAGE + "..";
  public static final String PORT_LAYER_PATH = PORT_PACKAGE + "..";
  public static final String PORT_IN_LAYER_PATH = PORT_IN_PACKAGE + "..";
  public static final String PORT_OUT_LAYER_PATH = PORT_OUT_PACKAGE + "..";
  public static final String CONTROLLER_LAYER_PATH = CONTROLLER_PACKAGE + "..";
  public static final String REPOSITORY_LAYER_PATH = REPOSITORY_PACKAGE + "..";
  public static final String WEBCLIENT_LAYER_PATH = WEBCLIENT_PACKAGE + "..";
  public static final String ADAPTER_COMMON_LAYER_PATH = ADAPTER_COMMONS_PACKAGE + "..";
  public static final String ADAPTER_CONFIGURATION_LAYER_PATH =
      ADAPTER_CONFIGURATION_PACKAGE + "..";

  public static final String DOMAIN_SUFFIX = "Entity";
  public static final String USECASE_PORT_SUFFIX = "UseCasePort";
  public static final String REPOSITORY_PORT_SUFFIX = "RepositoryPort";
  public static final String SERVICE_PORT_SUFFIX = "ServicePort";
  public static final String EXCEPTION_SUFFIX = "Exception";

  public static final String PROPERTIES_SUFFIX = "Properties";

  public static final String UTILS_SUFFIX = "Utils";

  public static final String CONTROLLER_SUFFIX = "Controller";
  public static final String CONTROLLER_REQUEST_SUFFIX = "Request";
  public static final String CONTROLLER_RESPONSE_SUFFIX = "Response";

  public static final String REPOSITORY_SUFFIX = "Repository";
  public static final String REPOSITORY_MODEL_SUFFIX = "Model";

  public static final String WEBCLIENT_SUFFIX = "Service";
  public static final String WEBCLIENT_REQUEST_SUFFIX = "Request";
  public static final String WEBCLIENT_RESPONSE_SUFFIX = "Response";

  public static final String CORE_LAYER = "Core";
  public static final String PORT_IN_LAYER = "PortIn";
  public static final String PORT_OUT_LAYER = "PortOut";
  public static final String CONTROLLER_LAYER = "Controller";
  public static final String REPOSITORY_LAYER = "Repository";
  public static final String WEBCLIENT_LAYER = "WebClient";
  public static final String ADAPTER_COMMONS_LAYER = "AdapterCommons";
  public static final String ADAPTER_CONFIGURATION_LAYER = "AdapterConfiguration";

  public static LayeredArchitecture getLayeredArchitecture() {
    return layeredArchitecture()
        .layer(CORE_LAYER)
        .definedBy(CORE_LAYER_PATH)
        .layer(PORT_IN_LAYER)
        .definedBy(PORT_IN_LAYER_PATH)
        .layer(PORT_OUT_LAYER)
        .definedBy(PORT_OUT_LAYER_PATH)
        .layer(CONTROLLER_LAYER)
        .definedBy(CONTROLLER_LAYER_PATH)
        .layer(REPOSITORY_LAYER)
        .definedBy(REPOSITORY_LAYER_PATH)
        .layer(WEBCLIENT_LAYER)
        .definedBy(WEBCLIENT_LAYER_PATH)
        .layer(ADAPTER_COMMONS_LAYER)
        .definedBy(ADAPTER_COMMON_LAYER_PATH)
        .layer(ADAPTER_CONFIGURATION_LAYER)
        .definedBy(ADAPTER_CONFIGURATION_LAYER_PATH);
  }
}
