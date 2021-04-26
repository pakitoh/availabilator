package foo.labs.availabilator.checker;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AvailabilityChecker {
    private HttpClient client;
    private final HttpResponse.BodyHandler<String> bodyHandler;
    private Clock clock;
    private String regex;
    private Pattern pattern;

    public AvailabilityChecker(Map<String, String> ctx) {
        client = HttpClient.newHttpClient();
        bodyHandler = HttpResponse.BodyHandlers.ofString();
        clock = new Clock();
        regex = ctx.get(ContextBuilder.REG_EXP);
        pattern = Pattern.compile(regex, Pattern.DOTALL);
    }

    public void setClient(HttpClient client) {
        this.client = client;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public void check(final String address,
                      final Consumer<Availability> consumer) {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(address))
                .build();
        final long startTime = clock.now();
        client.sendAsync(request, bodyHandler)
                .thenAccept(response -> {
                    if(regex.equals("")) {
                        consumer.accept(
                                new Availability(
                                startTime,
                                address,
                                clock.now() - startTime,
                                response.statusCode()));
                    } else {
                        Matcher matcher = pattern.matcher(response.body());
                        consumer.accept(new Availability(
                                startTime,
                                address,
                                clock.now() - startTime,
                                response.statusCode(),
                                matcher.find()));
                    }
                });
    }
}
