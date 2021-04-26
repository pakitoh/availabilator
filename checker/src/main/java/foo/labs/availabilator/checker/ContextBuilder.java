package foo.labs.availabilator.checker;

import java.util.Map;
import java.util.Optional;

public class ContextBuilder {
    public static final String REG_EXP = "REG_EXP";
    public static final String DEFAULT_REG_EXP = "";
    public static final String POOLING_RATE = "POOLING_RATE";
    public static final String DEFAULT_POOLING_RATE = "5000";
    public static final String POOL_SIZE = "THREAD_POOL_SIZE";
    public static final String DEFAULT_POOL_SIZE = "2";
    public static final String INITIAL_DELAY = "INITIAL_DELAY";
    public static final String DEFAULT_INITIAL_DELAY = "0";
    public static final String KAFKA_HOST = "KAFKA_HOST";
    public static final String DEFAULT_KAFKA_HOST = "localhost";
    public static final String KAFKA_PORT = "KAFKA_PORT";
    public static final String DEFAULT_KAFKA_PORT = "9092";
    public static final String KAFKA_CLIENT_ID = "KAFKA_CLIENT_ID";
    public static final String DEFAULT_KAFKA_CLIENT_ID = "Availabilator";
    public static final String SSL_ENABLED = "SSL_ENABLED";
    public static final String DEFAULT_SSL_ENABLED = "false";
    public static final String TRUSTSTORE = "TRUSTSTORE";
    public static final String DEFAULT_TRUSTSTORE = "client.truststore.jks";
    public static final String TRUSTSTORE_PASS = "TRUSTSTORE_PASS";
    public static final String DEFAULT_TRUSTSTORE_PASS = "test123";
    public static final String KEYSTORE = "KEYSTORE";
    public static final String DEFAULT_KEYSTORE = "client.keystore.p12";
    public static final String KEYSTORE_TYPE = "KEYSTORE_TYPE";
    public static final String DEFAULT_KEYSTORE_TYPE = "PKCS12";
    public static final String KEYSTORE_PASS = "KEYSTORE_PASS";
    public static final String DEFAULT_KEYSTORE_PASS = "test456";
    public static final String KEY_PASS = "KEY_PASS";
    public static final String DEFAULT_KEY_PASS = "test789";
    public static final String TOPIC = "TOPIC";
    public static final String DEFAULT_TOPIC = "availability";

    public Map<String, String> build() {
        return build(System.getenv());
    }

    public Map<String, String> build(Map<String, String> initialCtx) {
        return Map.ofEntries(
                getDefaultFromCtx(initialCtx, REG_EXP, DEFAULT_REG_EXP),
                getDefaultFromCtx(initialCtx, POOLING_RATE, DEFAULT_POOLING_RATE),
                getDefaultFromCtx(initialCtx, POOL_SIZE, DEFAULT_POOL_SIZE),
                getDefaultFromCtx(initialCtx, INITIAL_DELAY, DEFAULT_INITIAL_DELAY),
                getDefaultFromCtx(initialCtx, KAFKA_HOST, DEFAULT_KAFKA_HOST),
                getDefaultFromCtx(initialCtx, KAFKA_PORT, DEFAULT_KAFKA_PORT),
                getDefaultFromCtx(initialCtx, KAFKA_CLIENT_ID, DEFAULT_KAFKA_CLIENT_ID),
                getDefaultFromCtx(initialCtx, SSL_ENABLED, DEFAULT_SSL_ENABLED),
                getDefaultFromCtx(initialCtx, TRUSTSTORE, DEFAULT_TRUSTSTORE),
                getDefaultFromCtx(initialCtx, TRUSTSTORE_PASS, DEFAULT_TRUSTSTORE_PASS),
                getDefaultFromCtx(initialCtx, KEYSTORE, DEFAULT_KEYSTORE),
                getDefaultFromCtx(initialCtx, KEYSTORE_TYPE, DEFAULT_KEYSTORE_TYPE),
                getDefaultFromCtx(initialCtx, KEYSTORE_PASS, DEFAULT_KEYSTORE_PASS),
                getDefaultFromCtx(initialCtx, KEY_PASS, DEFAULT_KEY_PASS),
                getDefaultFromCtx(initialCtx, TOPIC, DEFAULT_TOPIC));
    }

    private Map.Entry<String, String> getDefaultFromCtx(Map<String, String> initialCtx, String key, String defaultValue) {
        return Map.entry(key, Optional.ofNullable(initialCtx.get(key)).orElse(defaultValue));
    }
}
