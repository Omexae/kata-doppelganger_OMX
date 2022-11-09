package fr.efrei;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class CalculatorTest {
  public static final class FooAuthorizer implements Authorizer {
    private final boolean authorize;

    public FooAuthorizer(boolean authorize) {
      this.authorize = authorize;
    }

    @Override
    public boolean authorize() {
      return this.authorize;
    }
  }

  @Test
  void should_throw_when_not_authorized() {
    var falseAuthorizer = new FooAuthorizer(false);
    var calculator = new Calculator(falseAuthorizer);

    Assertions.assertThrows(UnauthorizedAccessException.class,
            () -> calculator.divide(5, 2));
  }

  @Test
  void should_return_result_when_authorized() {
    var trueAuthorizer = new FooAuthorizer(true);
    var calculator = new Calculator(trueAuthorizer);

    Assertions.assertEquals(5 / 2, calculator.divide(5, 2));
  }
}
