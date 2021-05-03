package foo.labs.availabilator.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsumerTest {

    public static final String BOOTSTRAP_SERVERS = "host:port";
    public static final String CONSUMER_GROUP = "consumerGroupId";
    public static final String AUTO_COMMIT_INTERVAL = "250";
    public static final String POLLING_TIMEOUT = "500";
    public static final String SSL_ENABLED = "true";
    public static final String TRUSTSTORE = "myTruststore.jks";
    public static final String TRUSTSTORE_PASS = "letmein";
    public static final String KEYSTORE = "myKeystore.p12";
    public static final String KEYSTORE_PASS = "letmein";
    public static final String KEYSTORE_TYPE = "PKCS12";
    public static final String KEY_PASS = "letmein";
    public static final String TOPIC = "topic";
    public static final String LOCATION = "http://host";
    public static final Integer RESPONSE_TIME = 185;
    public static final Integer STATUS_CODE = 200;
    public static final Boolean MATCHES = Boolean.TRUE;
    public static final int PARTITION = 1;
    public static final long OFFSET = 25L;
    public static final String KEY = "key";

    Map<String, String> ctx = Map.ofEntries(
            Map.entry(ContextBuilder.BOOTSTRAP_SERVERS, BOOTSTRAP_SERVERS),
            Map.entry(ContextBuilder.CONSUMER_GROUP_ID, CONSUMER_GROUP),
            Map.entry(ContextBuilder.AUTO_COMMIT_INTERVAL, AUTO_COMMIT_INTERVAL),
            Map.entry(ContextBuilder.POLLING_TIMEOUT, POLLING_TIMEOUT),
            Map.entry(ContextBuilder.SSL_ENABLED, SSL_ENABLED),
            Map.entry(ContextBuilder.TRUSTSTORE, TRUSTSTORE),
            Map.entry(ContextBuilder.TRUSTSTORE_PASS, TRUSTSTORE_PASS),
            Map.entry(ContextBuilder.KEYSTORE, KEYSTORE),
            Map.entry(ContextBuilder.KEYSTORE_PASS, KEYSTORE_PASS),
            Map.entry(ContextBuilder.KEYSTORE_TYPE, KEYSTORE_TYPE),
            Map.entry(ContextBuilder.KEY_PASS, KEY_PASS),
            Map.entry(ContextBuilder.TOPIC, TOPIC));

    @Mock
    KafkaConsumerBuilder kafkaConsumerBuilder;

    @Mock
    KafkaConsumer<String, AvailabilatorRecord> kafkaConsumer;

    @Mock
    ConsumerRecords<String, AvailabilatorRecord> records;

    @Mock
    Writer writer;

    @Test
    public void shouldBuildUsingValuesInCtx() {
        Consumer consumer = new Consumer(ctx, kafkaConsumerBuilder, writer);

        assertThat(consumer.getTopic(), equalTo(TOPIC));
        verify(kafkaConsumerBuilder).build(
                BOOTSTRAP_SERVERS,
                CONSUMER_GROUP,
                AUTO_COMMIT_INTERVAL,
                SSL_ENABLED,
                TRUSTSTORE,
                TRUSTSTORE_PASS,
                KEYSTORE,
                KEYSTORE_PASS,
                KEYSTORE_TYPE,
                KEY_PASS);
    }

    @Test
    public void shouldSubscribeToTopicWhenInit() {
        when(kafkaConsumerBuilder.build(
                BOOTSTRAP_SERVERS,
                CONSUMER_GROUP,
                AUTO_COMMIT_INTERVAL,
                SSL_ENABLED,
                TRUSTSTORE,
                TRUSTSTORE_PASS,
                KEYSTORE,
                KEYSTORE_PASS,
                KEYSTORE_TYPE,
                KEY_PASS)).thenReturn(kafkaConsumer);
        Consumer consumer = new Consumer(ctx, kafkaConsumerBuilder, writer);

        consumer.init();

        verify(kafkaConsumer).subscribe(List.of(TOPIC));
    }

    @Test
    public void shouldCallWriterAfterPolling() {
        Instant expectedTimestamp = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        AvailabilatorRecord availability = buildAvailabilityRecord(expectedTimestamp);
        Duration expectedPollingTimeout = Duration.of(Long.parseLong(POLLING_TIMEOUT), ChronoUnit.MILLIS);
        ConsumerRecord<String, AvailabilatorRecord> cr = buildConsumerRecord(availability);
        when(kafkaConsumer.poll(expectedPollingTimeout))
                .thenReturn(records);
        when(records.iterator()).thenReturn(List.of(cr).listIterator());
        when(kafkaConsumerBuilder.build(
                BOOTSTRAP_SERVERS,
                CONSUMER_GROUP,
                AUTO_COMMIT_INTERVAL,
                SSL_ENABLED,
                TRUSTSTORE,
                TRUSTSTORE_PASS,
                KEYSTORE,
                KEYSTORE_PASS,
                KEYSTORE_TYPE,
                KEY_PASS)).thenReturn(kafkaConsumer);
        Consumer consumer = new Consumer(ctx, kafkaConsumerBuilder, writer);

        consumer.poll();

        verify(kafkaConsumer).poll(expectedPollingTimeout);
        verify(writer).store(availability);
    }

    @Test
    public void shouldCallCloseWhenClosing() {
        when(kafkaConsumerBuilder.build(
                BOOTSTRAP_SERVERS,
                CONSUMER_GROUP,
                AUTO_COMMIT_INTERVAL,
                SSL_ENABLED,
                TRUSTSTORE,
                TRUSTSTORE_PASS,
                KEYSTORE,
                KEYSTORE_PASS,
                KEYSTORE_TYPE,
                KEY_PASS)).thenReturn(kafkaConsumer);
        Consumer consumer = new Consumer(ctx, kafkaConsumerBuilder, writer);

        consumer.close();

        verify(kafkaConsumer).close();
    }

    private AvailabilatorRecord buildAvailabilityRecord(Instant expectedTimestamp) {
        return AvailabilatorRecord.build(
                expectedTimestamp.toEpochMilli(),
                LOCATION,
                RESPONSE_TIME,
                STATUS_CODE,
                MATCHES);
    }

    private ConsumerRecord<String, AvailabilatorRecord> buildConsumerRecord(AvailabilatorRecord availability) {
        return new ConsumerRecord<>(TOPIC, PARTITION, OFFSET, KEY, availability);
    }
}