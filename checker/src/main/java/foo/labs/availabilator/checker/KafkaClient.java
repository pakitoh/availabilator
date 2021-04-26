package foo.labs.availabilator.checker;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

public class KafkaClient {
    public static final String BOOTSTRAP_SERVERS_KEY = "bootstrap.servers";
    public static final String CLIENT_ID_KEY = "client.id";
    public static final String KEY_SERIALIZER_KEY = "key.serializer";
    public static final String VALUE_SERIALIZER_KEY = "value.serializer";
    public static final String SECURITY_PROTOCOL_KEY ="security.protocol";
    public static final String TRUSTSTORE_KEY ="ssl.truststore.location";
    public static final String TRUSTSTORE_PASS_KEY ="ssl.truststore.password";
    public static final String KEYSTORE_KEY ="ssl.keystore.location";
    public static final String KEYSTORE_TYPE_KEY ="ssl.keystore.type";
    public static final String KEYSTORE_PASS_KEY ="ssl.keystore.password";
    public static final String KEY_PASS_KEY ="ssl.key.password";
    public static final String SECURITY_PROTOCOL = "SSL";
    private final Logger logger = LoggerFactory.getLogger(KafkaClient.class);
    private KafkaProducer<String, String> producer;
    private final String topic;

    public KafkaClient(Map<String, String> ctx) {
        Properties properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS_KEY, ctx.get(ContextBuilder.KAFKA_HOST) + ":" + ctx.get(ContextBuilder.KAFKA_PORT));
        properties.put(CLIENT_ID_KEY, ctx.get(ContextBuilder.KAFKA_CLIENT_ID));
        if(ctx.get(ContextBuilder.SSL_ENABLED).equalsIgnoreCase(Boolean.TRUE.toString())) {
            properties.put(SECURITY_PROTOCOL_KEY, SECURITY_PROTOCOL);
            properties.put(TRUSTSTORE_KEY, ctx.get(ContextBuilder.TRUSTSTORE));
            properties.put(TRUSTSTORE_PASS_KEY, ctx.get(ContextBuilder.TRUSTSTORE_PASS));
            properties.put(KEYSTORE_KEY, ctx.get(ContextBuilder.KEYSTORE));
            properties.put(KEYSTORE_TYPE_KEY, ctx.get(ContextBuilder.KEYSTORE_TYPE));
            properties.put(KEYSTORE_PASS_KEY, ctx.get(ContextBuilder.KEYSTORE_PASS));
            properties.put(KEY_PASS_KEY, ctx.get(ContextBuilder.KEY_PASS));
        }
        final String serializer = org.apache.kafka.common.serialization.StringSerializer.class.getCanonicalName();
        properties.put(KEY_SERIALIZER_KEY, serializer);
        properties.put(VALUE_SERIALIZER_KEY, serializer);
        producer = new KafkaProducer<String, String>(properties);
        topic = ctx.get(ContextBuilder.TOPIC);
    }

    public void setProducer(KafkaProducer<String, String> producer) {
        this.producer = producer;
    }

    public void store(Availability availability) {
        producer.send(
                new ProducerRecord<String, String>(
                        topic,
                        availability.address(),
                        availability.asJson()),
                new StoreCallback(availability)
        );
    }

    class StoreCallback implements Callback {
        final Availability availability;

        StoreCallback(Availability availability) {
            this.availability = availability;
        }

        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                logger.error("ERROR trying to send to Kafka " + availability.toString() + " : " + e.getMessage());
            }
        }
    }
}
