package foo.labs.availabilator.checker;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KafkaClientTest {
    public static final String ADDRESS = "https://host";
    public static final long DURATION = 100L;
    public static final int STATUS = 200;
    public static final long TIMESTAMP = 1L;

    @Mock
    private KafkaProducer<String, String> producer;

    @Test
    public void shouldSendAvailabilityToKafka() {
        Availability availability = new Availability(TIMESTAMP, ADDRESS, DURATION, STATUS);
        KafkaClient kafkaClient = new KafkaClient(new ContextBuilder().build(new HashMap<>()));
        kafkaClient.setProducer(producer);

        kafkaClient.store(availability);

        verify(producer).send(
                argThat(withParams(ContextBuilder.DEFAULT_TOPIC, ADDRESS, availability)),
                any(KafkaClient.StoreCallback.class));
    }

    private ArgumentMatcher<ProducerRecord> withParams(
            String topic,
            String key,
            Availability value) {
        return (ProducerRecord pr) -> pr.topic().equals(topic)
                    && pr.key().toString().equals(key)
                    && pr.value().equals(value.asJson());
    }
}