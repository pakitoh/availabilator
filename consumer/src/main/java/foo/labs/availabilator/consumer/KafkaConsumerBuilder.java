package foo.labs.availabilator.consumer;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

public class KafkaConsumerBuilder {
    public static final String BOOTSTRAP_SERVERS_PROPERTY_NAME = "bootstrap.servers";
    public static final String CONSUMER_GROUP_PROPERTY_NAME = "group.id";
    public static final String KEY_DESERIALIZER_PROPERTY_NAME = "key.deserializer";
    public static final String VALUE_DESERIALIZER_PROPERTY_NAME = "value.deserializer";
    public static final String AUTO_COMMIT_PROPERTY_NAME = "enable.auto.commit";
    public static final String AUTO_COMMIT_INTERVAL_PROPERTY_NAME = "auto.commit.interval.ms";
    public static final String SECURITY_PROTOCOL_PROPERTY_NAME = "security.protocol";
    public static final String TRUSTSTORE_PROPERTY_NAME = "ssl.truststore.location";
    public static final String TRUSTSTORE_PASS_PROPERTY_NAME = "ssl.truststore.password";
    public static final String KEYSTORE_PROPERTY_NAME = "ssl.keystore.location";
    public static final String KEYSTORE_TYPE_PROPERTY_NAME = "ssl.keystore.type";
    public static final String KEYSTORE_PASS_PROPERTY_NAME = "ssl.keystore.password";
    public static final String KEY_PASS_PROPERTY_NAME = "ssl.key.password";
    public static final String SSL = "SSL";

    public KafkaConsumer<String, AvailabilatorRecord> build(
        String bootstrapServers,
        String consumerGroupId,
        String keyDeserializer,
        String valueDeserializer,
        String autoCommit,
        String autoCommitInterval,
        String ssl,
        String truststore,
        String truststorePass,
        String keystore,
        String keystorePass,
        String keystoreType,
        String keyPass
    ) {
        Properties props = new Properties();
        props.setProperty(BOOTSTRAP_SERVERS_PROPERTY_NAME, bootstrapServers);
        props.setProperty(CONSUMER_GROUP_PROPERTY_NAME, consumerGroupId);
        props.setProperty(KEY_DESERIALIZER_PROPERTY_NAME, keyDeserializer);
        props.setProperty(VALUE_DESERIALIZER_PROPERTY_NAME, valueDeserializer);
        props.setProperty(AUTO_COMMIT_PROPERTY_NAME, autoCommit);
        props.setProperty(AUTO_COMMIT_INTERVAL_PROPERTY_NAME, autoCommitInterval);
        if("true".equals(ssl)) {
            props.setProperty(SECURITY_PROTOCOL_PROPERTY_NAME, SSL);
            props.setProperty(TRUSTSTORE_PROPERTY_NAME, truststore);
            props.setProperty(TRUSTSTORE_PASS_PROPERTY_NAME, truststorePass);
            props.setProperty(KEYSTORE_PROPERTY_NAME, keystore);
            props.setProperty(KEYSTORE_PASS_PROPERTY_NAME, keystorePass);
            props.setProperty(KEYSTORE_TYPE_PROPERTY_NAME, keystoreType);
            props.setProperty(KEY_PASS_PROPERTY_NAME, keyPass);
        }
        return new KafkaConsumer<>(props);
    }
}
