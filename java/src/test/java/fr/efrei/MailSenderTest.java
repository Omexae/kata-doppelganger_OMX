package fr.efrei;

import fr.efrei.mailprovider.SendRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MailSenderTest {

  @Test
  void should_make_a_sendgrid_request() {
    var httpClient = new HttpClient() {
      private Object receivedRequest;
      @Override
      public Response post(String url, Object request) {
        this.receivedRequest = request;
        return new Response(200, "OK");
      }
    };

    var mailSender = new MailSender(httpClient);
    var user = new User("User1", "user1@gmail.com");
    var message = "Hello world!";

    mailSender.sendV1(user, message);

    var expectedSendRequest = new SendRequest(user.name(), user.email(), "New notification", message);
    assertEquals(expectedSendRequest, httpClient.receivedRequest);
  }

  @Test
  void should_retry_when_getting_a_503_error() {
    var httpClient = new HttpClient() {
      private List<Object> receivedRequests = new ArrayList<>();

      @Override
      public Response post(String url, Object request) {
        this.receivedRequests.add(request);
        return new Response(503, "Aie Aie Aie");
      }
    };

    var mailSender = new MailSender(httpClient);
    var user = new User("User1", "user1@gmail.com");
    var message = "Hello world!";

    mailSender.sendv2(user, message);

    var expectedSentRequest = new SendRequest(user.name(), user.email(), "New notification", message);
    assertAll("retry requests",
      () -> assertEquals(expectedSentRequest, httpClient.receivedRequests.get(0)),
      () -> assertEquals(expectedSentRequest, httpClient.receivedRequests.get(1))
    );
  }
}
