package foo.labs.availabilator.checker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(MockitoExtension.class)
public class AvailabilityTest {
    public static final String VALID_ADDRESS = "https://host";
    public static final long START_TIME = 100L;
    public static final long DURATION = 100L;

    @Test
    public void shouldCConvertToValidJsonWhenNoRegExpChecking() {
        Availability availability = new Availability(
                START_TIME,
                VALID_ADDRESS,
                DURATION,
                HttpURLConnection.HTTP_OK);
        String expectedJson = String.format("""
                {
                    "schema": {
                        "type": "struct",
                        "fields": [
                            { "field": "timestamp", "type": "int64", "name": "org.apache.kafka.connect.data.Timestamp", "optional": false },
                            { "field": "address", "type": "string", "optional": false },
                            { "field": "responseTime", "type": "int32", "optional": false },
                            { "field": "statusCode", "type": "int16", "optional": false },
                            { "field": "matches", "type": "boolean", "optional": true }
                        ]
                    },
                    "payload": {
                      "timestamp":%d,
                      "address":"%s",
                      "responseTime":%d,
                      "statusCode":%d
                    }
                }
                """, START_TIME, VALID_ADDRESS, DURATION,  HttpURLConnection.HTTP_OK);
        String json = availability.asJson();

        assertThat(json, equalTo(expectedJson));
    }

    @Test
    public void shouldCConvertToValidJsonWhenRegExpChecking() {
        Availability availability = new Availability(
                START_TIME,
                VALID_ADDRESS,
                DURATION,
                HttpURLConnection.HTTP_OK,
                Boolean.TRUE);
        String expectedJson = String.format("""
                {
                    "schema": {
                        "type": "struct",
                        "fields": [
                            { "field": "timestamp", "type": "int64", "name": "org.apache.kafka.connect.data.Timestamp", "optional": false },
                            { "field": "address", "type": "string", "optional": false },
                            { "field": "responseTime", "type": "int32", "optional": false },
                            { "field": "statusCode", "type": "int16", "optional": false },
                            { "field": "matches", "type": "boolean", "optional": true }
                        ]
                    },
                    "payload": {
                      "timestamp":%d,
                      "address":"%s",
                      "responseTime":%d,
                      "statusCode":%d,
                      "matches":%b
                    }
                }
                """, START_TIME, VALID_ADDRESS, DURATION,  HttpURLConnection.HTTP_OK, Boolean.TRUE);
        String json = availability.asJson();

        assertThat(json, equalTo(expectedJson));
    }
}
