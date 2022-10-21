package br.com.cams7.orders;

import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;
import static org.apache.commons.lang3.ClassUtils.getPackageName;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import br.com.cams7.orders.template.DomainTemplateLoader;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.scheduler.Schedulers;

public abstract class BaseTests {
  protected static final String REQUEST_TRACE_ID = "123";
  protected static final String ERROR_MESSAGE = "Something wrong";

  @BeforeAll
  static void setup() {
    loadTemplates(getPackageName(DomainTemplateLoader.class));
    BlockHound.install(builder -> builder.allowBlockingCallsInside("java.time.ZoneRegion", "ofId"));
  }

  @Test
  @DisplayName("Should work when install BlockHound")
  void shouldWorkWhenInstallBlockHound() {
    try {
      FutureTask<?> task =
          new FutureTask<>(
              () -> {
                Thread.sleep(0); // NOSONAR
                return "";
              });
      Schedulers.parallel().schedule(task);

      task.get(10, TimeUnit.SECONDS);
      fail("should fail");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof BlockingOperationError);
    }
  }
}
