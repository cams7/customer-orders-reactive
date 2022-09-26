package br.com.cams7.orders.arch;

import static br.com.cams7.orders.arch.ArchUnitHelper.ALL_CLASSES_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.CONTROLLER_LAYER;
import static br.com.cams7.orders.arch.ArchUnitHelper.CONTROLLER_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.CONTROLLER_REQUEST_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.CONTROLLER_REQUEST_SUFFIX;
import static br.com.cams7.orders.arch.ArchUnitHelper.CONTROLLER_RESPONSE_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.CONTROLLER_RESPONSE_SUFFIX;
import static br.com.cams7.orders.arch.ArchUnitHelper.CONTROLLER_SUFFIX;
import static br.com.cams7.orders.arch.ArchUnitHelper.WEBCLIENT_REQUEST_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.WEBCLIENT_RESPONSE_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.getLayeredArchitecture;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packages = ALL_CLASSES_PACKAGE, importOptions = DoNotIncludeTests.class)
class ControllerLayerArchRulesTests {

  @ArchTest
  void shouldBeSuffixedAsControllerWhenInControllerPackage(JavaClasses allClasses) {
    classes()
        .that()
        .resideInAPackage(CONTROLLER_PACKAGE)
        .should()
        .haveSimpleNameEndingWith(CONTROLLER_SUFFIX)
        .check(allClasses);

    classes()
        .that()
        .haveSimpleNameEndingWith(CONTROLLER_SUFFIX)
        .should()
        .resideInAPackage(CONTROLLER_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void shouldBeAnnotatedAsRestControllerWhenInControllerPackage(JavaClasses allClasses) {
    classes()
        .that()
        .resideInAPackage(CONTROLLER_PACKAGE)
        .should()
        .beAnnotatedWith(RestController.class)
        .check(allClasses);
  }

  @ArchTest
  void shouldHaveOnlyRestControllerAnnotationWhenInControllerPackage(JavaClasses allClasses) {
    classes()
        .that()
        .areAnnotatedWith(RestController.class)
        .should()
        .resideInAPackage(CONTROLLER_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void shouldOnlyHaveSuffixAsRequestWhenInRequestPackage(JavaClasses allClasses) {
    classes()
        .that()
        .haveSimpleNameEndingWith(CONTROLLER_REQUEST_SUFFIX)
        .should()
        .resideInAPackage(CONTROLLER_REQUEST_PACKAGE)
        .orShould()
        .resideInAPackage(WEBCLIENT_REQUEST_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void shouldOnlyHaveSuffixAsResponseWhenInResponsePackage(JavaClasses allClasses) {
    classes()
        .that()
        .haveSimpleNameEndingWith(CONTROLLER_RESPONSE_SUFFIX)
        .should()
        .resideInAPackage(CONTROLLER_RESPONSE_PACKAGE)
        .orShould()
        .resideInAPackage(WEBCLIENT_RESPONSE_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void shouldNotAcessControllerLayerWhenOtherLayer(JavaClasses allClasses) {
    getLayeredArchitecture()
        .whereLayer(CONTROLLER_LAYER)
        .mayNotBeAccessedByAnyLayer()
        .check(allClasses);
  }
}
