package foo.labs.availabilator.checker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvailabilityCheckerTest {
    public static final String VALID_ADDRESS = "https://host";
    public static final long START_TIME = 100L;
    public static final long DURATION = 100L;
    public static final String RESPONSE_BODY = "RESPONSE_BODY";
    public static final String MULTILINE_RESPONSE_BODY = "123\nRESPONSE_BODY";
    public static final String REG_EXP_THAT_MATCHES = "\\w*";
    public static final String REG_EXP_NO_MATCHES = "\\d{3}";

    @Mock
    Clock clock;

    @Mock
    HttpClient httpClient;

    @Mock
    Consumer<Availability> consumer;

    @Mock
    HttpResponse<String> response;

    @Test
    public void shouldThrowExceptionWhenInvalidURLProvided() {
        final String invalidAddress = "invalid";
        AvailabilityChecker availabilityChecker = new AvailabilityChecker(Map.of(ContextBuilder.REG_EXP, ""));
        availabilityChecker.setClient(httpClient);
        availabilityChecker.setClock(clock);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> availabilityChecker.check(invalidAddress, consumer));
    }

    @Test
    public void shouldSendCallAndConsumeResponseWhenNoRegExp() {
        when(clock.now())
                .thenReturn(START_TIME)
                .thenReturn(START_TIME + DURATION);
        when(
                httpClient.sendAsync(
                        argThat(requestTo(VALID_ADDRESS)),
                        any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.supplyAsync(() -> response));
        when(response.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        AvailabilityChecker availabilityChecker = new AvailabilityChecker(Map.of(ContextBuilder.REG_EXP, ""));
        availabilityChecker.setClient(httpClient);
        availabilityChecker.setClock(clock);

        availabilityChecker.check(VALID_ADDRESS, consumer);

        verify(consumer).accept(
                new Availability(
                        START_TIME,
                        VALID_ADDRESS,
                        DURATION,
                        HttpURLConnection.HTTP_OK));
    }

    @Test
    public void shouldSendCallAndConsumeResponseCheckingBodyWhenRegExpMatches() {
        when(clock.now())
                .thenReturn(START_TIME)
                .thenReturn(START_TIME + DURATION);
        when(
                httpClient.sendAsync(
                        argThat(requestTo(VALID_ADDRESS)),
                        any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.supplyAsync(() -> response));
        when(response.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(response.body()).thenReturn(RESPONSE_BODY);
        AvailabilityChecker availabilityChecker = new AvailabilityChecker(Map.of(ContextBuilder.REG_EXP, REG_EXP_THAT_MATCHES));
        availabilityChecker.setClient(httpClient);
        availabilityChecker.setClock(clock);

        availabilityChecker.check(VALID_ADDRESS, consumer);

        verify(consumer).accept(
                new Availability(
                        START_TIME,
                        VALID_ADDRESS,
                        DURATION,
                        HttpURLConnection.HTTP_OK,
                        Boolean.TRUE));
    }

    @Test
    public void shouldSendCallAndConsumeResponseCheckingBodyWhenRegExpDoesntMatch() {
        when(clock.now())
                .thenReturn(START_TIME)
                .thenReturn(START_TIME + DURATION);
        when(
                httpClient.sendAsync(
                        argThat(requestTo(VALID_ADDRESS)),
                        any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.supplyAsync(() -> response));
        when(response.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(response.body()).thenReturn(RESPONSE_BODY);
        AvailabilityChecker availabilityChecker = new AvailabilityChecker(Map.of(ContextBuilder.REG_EXP, REG_EXP_NO_MATCHES));
        availabilityChecker.setClient(httpClient);
        availabilityChecker.setClock(clock);

        availabilityChecker.check(VALID_ADDRESS, consumer);

        verify(consumer).accept(
                new Availability(
                        START_TIME,
                        VALID_ADDRESS,
                        DURATION,
                        HttpURLConnection.HTTP_OK,
                        Boolean.FALSE));
    }

    @Test
    public void shouldSendCallAndConsumeResponseCheckingBodyWhenRegExpDoesMatchIn2Line() {
        when(clock.now())
                .thenReturn(START_TIME)
                .thenReturn(START_TIME + DURATION);
        when(
                httpClient.sendAsync(
                        argThat(requestTo(VALID_ADDRESS)),
                        any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.supplyAsync(() -> response));
        when(response.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(response.body()).thenReturn(MULTILINE_RESPONSE_BODY);
        AvailabilityChecker availabilityChecker = new AvailabilityChecker(Map.of(ContextBuilder.REG_EXP, REG_EXP_THAT_MATCHES));
        availabilityChecker.setClient(httpClient);
        availabilityChecker.setClock(clock);

        availabilityChecker.check(VALID_ADDRESS, consumer);

        verify(consumer).accept(
                new Availability(
                        START_TIME,
                        VALID_ADDRESS,
                        DURATION,
                        HttpURLConnection.HTTP_OK,
                        Boolean.TRUE));
    }

    private ArgumentMatcher<HttpRequest> requestTo(String address) {
        return (HttpRequest req) -> req.uri().toString().equals(address);
    }
}
