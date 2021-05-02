package foo.labs.availabilator.consumer;

import java.util.Map;
import java.util.Optional;

public class ContextBuilder {

    public static final String BOOTSTRAP_SERVERS = "BOOTSTRAP_SERVERS";
    public static final String CONSUMER_GROUP_ID = "GROUP_ID";
    public static final String KEY_DESERIALIZER = "KEY_DESERIALIZER";
    public static final String VALUE_DESERIALIZER = "VALUE_DESERIALIZER";
    public static final String AUTO_COMMIT = "ENABLE_AUTO_COMMIT";
    public static final String AUTO_COMMIT_INTERVAL = "AUTO_COMMIT_INTERVAL_MS";
    public static final String TOPIC = "TOPIC";
    public static final String POLLING_TIMEOUT = "POLLING_TIMEOUT";
    public static final String SSL_ENABLED = "SSL_ENABLED";
    public static final String TRUSTSTORE = "TRUSTSTORE";
    public static final String TRUSTSTORE_PASS = "TRUSTSTORE_PASS";
    public static final String KEYSTORE = "KEYSTORE";
    public static final String KEYSTORE_TYPE = "KEYSTORE_TYPE";
    public static final String KEYSTORE_PASS = "KEYSTORE_PASS";
    public static final String KEY_PASS = "KEY_PASS";
    public static final String DB_HOST = "DB_HOST";
    public static final String DB_PORT = "DB_PORT";
    public static final String DB_USER = "DB_USER";
    public static final String DB_NAME = "DB_NAME";
    public static final String DB_USER_PASS = "DB_USER_PASS";
    public static final String DB_TABLE_NAME = "DB_TABLE_NAME";

    public static final String DEFAULT_KEY_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    public static final String DEFAULT_BOOTSTRAP_SERVERS = "localhost:9092";
    public static final String DEFAULT_CONSUMER_GROUP_ID = "dbWriter";
    public static final String DEFAULT_VALUE_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    public static final String DEFAULT_AUTO_COMMIT = "true";
    public static final String DEFAULT_AUTO_COMMIT_INTERVAL = "1000";
    public static final String DEFAULT_TOPIC = "availability";
    public static final String DEFAULT_POLLING_TIMEOUT = "1000";
    public static final String DEFAULT_SSL_ENABLED = "false";
    public static final String DEFAULT_TRUSTSTORE = "client_truststore.jks";
    public static final String DEFAULT_TRUSTSTORE_PASS = "test123";
    public static final String DEFAULT_KEYSTORE = "client_keystore.p12";
    public static final String DEFAULT_KEYSTORE_PASS = "test456";
    public static final String DEFAULT_KEYSTORE_TYPE = "PKCS12";
    public static final String DEFAULT_KEY_PASS = "test789";
    public static final String DEFAULT_DB_HOST = "localhost";
    public static final String DEFAULT_DB_PORT = "5432";
    public static final String DEFAULT_DB_USER = "postgres";
    public static final String DEFAULT_DB_USER_PASS = "password";
    public static final String DEFAULT_DB_NAME = "my-db";
    public static final String DEFAULT_DB_TABLE_NAME = "availability";

    public Map<String, String> build() {
        return build(System.getenv());
    }

    public Map<String, String> build(Map<String, String> initialCtx) {
        return Map.ofEntries(
                getDefaultFromCtx(initialCtx, BOOTSTRAP_SERVERS, DEFAULT_BOOTSTRAP_SERVERS),
                getDefaultFromCtx(initialCtx, CONSUMER_GROUP_ID, DEFAULT_CONSUMER_GROUP_ID),
                getDefaultFromCtx(initialCtx, KEY_DESERIALIZER, DEFAULT_KEY_DESERIALIZER),
                getDefaultFromCtx(initialCtx, VALUE_DESERIALIZER, DEFAULT_VALUE_DESERIALIZER),
                getDefaultFromCtx(initialCtx, AUTO_COMMIT, DEFAULT_AUTO_COMMIT),
                getDefaultFromCtx(initialCtx, AUTO_COMMIT_INTERVAL, DEFAULT_AUTO_COMMIT_INTERVAL),
                getDefaultFromCtx(initialCtx, SSL_ENABLED, DEFAULT_SSL_ENABLED),
                getDefaultFromCtx(initialCtx, TRUSTSTORE, DEFAULT_TRUSTSTORE),
                getDefaultFromCtx(initialCtx, TRUSTSTORE_PASS, DEFAULT_TRUSTSTORE_PASS),
                getDefaultFromCtx(initialCtx, KEYSTORE, DEFAULT_KEYSTORE),
                getDefaultFromCtx(initialCtx, KEYSTORE_TYPE, DEFAULT_KEYSTORE_TYPE),
                getDefaultFromCtx(initialCtx, KEYSTORE_PASS, DEFAULT_KEYSTORE_PASS),
                getDefaultFromCtx(initialCtx, KEY_PASS, DEFAULT_KEY_PASS),
                getDefaultFromCtx(initialCtx, TOPIC, DEFAULT_TOPIC),
                getDefaultFromCtx(initialCtx, POLLING_TIMEOUT, DEFAULT_POLLING_TIMEOUT),
                getDefaultFromCtx(initialCtx, DB_HOST , DEFAULT_DB_HOST),
                getDefaultFromCtx(initialCtx, DB_PORT , DEFAULT_DB_PORT),
                getDefaultFromCtx(initialCtx, DB_USER , DEFAULT_DB_USER ),
                getDefaultFromCtx(initialCtx, DB_USER_PASS , DEFAULT_DB_USER_PASS ),
                getDefaultFromCtx(initialCtx, DB_NAME , DEFAULT_DB_NAME ),
                getDefaultFromCtx(initialCtx, DB_TABLE_NAME , DEFAULT_DB_TABLE_NAME ));
    }

    private Map.Entry<String, String> getDefaultFromCtx(Map<String, String> initialCtx, String key, String defaultValue) {
        return Map.entry(key, Optional.ofNullable(initialCtx.get(key)).orElse(defaultValue));
    }
}
