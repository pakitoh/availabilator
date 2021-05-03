package foo.labs.availabilator.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class Consumer {

    private KafkaConsumer<String, AvailabilatorRecord> kafkaConsumer;
    private String topic;
    private Duration pollingTimeout;
    private Writer writer;

    public Consumer(
            Map<String, String> ctx,
            KafkaConsumerBuilder kafkaConsumerBuilder,
            Writer writer) {
        this.kafkaConsumer = kafkaConsumerBuilder.build(
                ctx.get(ContextBuilder.BOOTSTRAP_SERVERS),
                ctx.get(ContextBuilder.CONSUMER_GROUP_ID),
                ctx.get(ContextBuilder.AUTO_COMMIT_INTERVAL),
                ctx.get(ContextBuilder.SSL_ENABLED),
                ctx.get(ContextBuilder.TRUSTSTORE),
                ctx.get(ContextBuilder.TRUSTSTORE_PASS),
                ctx.get(ContextBuilder.KEYSTORE),
                ctx.get(ContextBuilder.KEYSTORE_PASS),
                ctx.get(ContextBuilder.KEYSTORE_TYPE),
                ctx.get(ContextBuilder.KEY_PASS));
        this.topic = ctx.get(ContextBuilder.TOPIC);
        this.pollingTimeout = Duration.of(
                Long.parseLong(ctx.get(ContextBuilder.POLLING_TIMEOUT)),
                ChronoUnit.MILLIS);
        this.writer = writer;
    }

    public String getTopic() {
        return topic;
    }

    public void init() {
        kafkaConsumer.subscribe(List.of(topic));
    }

    public void poll() {
        for (ConsumerRecord<String, AvailabilatorRecord> record : kafkaConsumer.poll(pollingTimeout)) {
            writer.store(record.value());
        }
    }

    public void close() {
        kafkaConsumer.close();
    }
}
