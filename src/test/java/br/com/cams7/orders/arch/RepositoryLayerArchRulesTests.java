package br.com.cams7.orders.arch;

import static br.com.cams7.orders.arch.ArchUnitHelper.ALL_CLASSES_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.REPOSITORY_LAYER;
import static br.com.cams7.orders.arch.ArchUnitHelper.REPOSITORY_MODEL_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.REPOSITORY_MODEL_SUFFIX;
import static br.com.cams7.orders.arch.ArchUnitHelper.REPOSITORY_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.REPOSITORY_SUFFIX;
import static br.com.cams7.orders.arch.ArchUnitHelper.getLayeredArchitecture;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import org.springframework.stereotype.Repository;

@AnalyzeClasses(packages = ALL_CLASSES_PACKAGE, importOptions = DoNotIncludeTests.class)
class RepositoryLayerArchRulesTests {

  @ArchTest
  void shouldBeSuffixedAsRepositoryWhenInRepositoryPackage(JavaClasses allClasses) {
    classes()
        .that()
        .resideInAPackage(REPOSITORY_PACKAGE)
        .should()
        .haveSimpleNameEndingWith(REPOSITORY_SUFFIX)
        .check(allClasses);

    classes()
        .that()
        .haveSimpleNameEndingWith(REPOSITORY_SUFFIX)
        .should()
        .resideInAPackage(REPOSITORY_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void shouldBeAnnotatedAsRepositoryWhenInRepositoryPackage(JavaClasses allClasses) {
    classes()
        .that()
        .resideInAPackage(REPOSITORY_PACKAGE)
        .should()
        .beAnnotatedWith(Repository.class)
        .check(allClasses);
  }

  @ArchTest
  void shouldHaveOnlyRepositoryAnnotationWhenInRepositoryPackage(JavaClasses allClasses) {
    classes()
        .that()
        .areAnnotatedWith(Repository.class)
        .should()
        .resideInAPackage(REPOSITORY_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void shouldOnlyHaveSuffixAsModelWhenInModelPackage(JavaClasses allClasses) {
    classes()
        .that()
        .haveSimpleNameEndingWith(REPOSITORY_MODEL_SUFFIX)
        .should()
        .resideInAPackage(REPOSITORY_MODEL_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void shouldNotAcessRepositoryLayerWhenOtherLayer(JavaClasses allClasses) {
    getLayeredArchitecture()
        .whereLayer(REPOSITORY_LAYER)
        .mayNotBeAccessedByAnyLayer()
        .check(allClasses);
  }
}
