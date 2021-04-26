package foo.labs.availabilator.checker;

import java.util.Map;

public class Main {
    private AvailabilityChecker availabilityChecker;
    private Scheduler scheduler;
    private KafkaClient kafkaClient;

    public Main() {
        final Map<String, String> ctx = new ContextBuilder().build();
        availabilityChecker = new AvailabilityChecker(ctx);
        scheduler = new Scheduler(ctx);
        kafkaClient = new KafkaClient(ctx);
    }

    public void watch(String address) {
        scheduler.schedule(() -> {
            availabilityChecker.check(address, kafkaClient::store);
        });
    }

    public static void main(String[] args) {
        new Main().watch(args[0]);
    }
}
