package foo.labs.availabilator.consumer;

import org.apache.kafka.common.errors.WakeupException;

import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        final Map<String, String> ctx = new ContextBuilder().build();
        Consumer consumer = new Consumer(
                ctx,
                new KafkaConsumerBuilder(),
                new Writer(ctx));
        try {
            consumer.init();
            while (true) {
                consumer.poll();
            }
        } catch (WakeupException e) {
            // ignore for shutdown
        } finally {
            consumer.close();
        }
    }
}
