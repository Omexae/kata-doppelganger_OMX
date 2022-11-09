package fr.efrei;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiscountApplierTest {
  public class FooNotifier implements Notifier {
    public FooNotifier() {}

    private int notifierCount = 0;
    private final Set<User> notifiedUsers = new HashSet<>();

    @Override
    public void notify(User user, String message) {
      this.notifierCount ++; // collect number of notifications sent
      this.notifiedUsers.add(user); // collect notified users
    }

    public int notifiedUsersCount() {
      return this.notifiedUsers.size();
    }
  }

  @Test
  void should_notify_twice_when_applying_discount_for_two_users_v1() {
    var users = List.of(
            new User("User1", "user1@gmail.com"),
            new User("User2", "user2@gmail.com")
    );
    var notifier = new FooNotifier();
    var discountApplier = new DiscountApplier(notifier);

    discountApplier.applyV1(0.25, users);

    assertEquals(2, notifier.notifierCount);
  }

  @Test
  void should_notify_twice_when_applying_discount_for_two_users_v2() {
    var users = List.of(
            new User("User1", "user1@gmail.com"),
            new User("User2", "user2@gmail.com")
    );
    var notifier = new FooNotifier();
    var discountApplier = new DiscountApplier(notifier);

    discountApplier.applyV2(0.25, users);
    assertEquals(2, notifier.notifiedUsersCount());
  }
}
