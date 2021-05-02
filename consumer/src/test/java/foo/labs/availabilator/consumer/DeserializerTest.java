package foo.labs.availabilator.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DeserializerTest {
    public final String TOPIC = "topic";

    @Test
    public void shouldReturnNullWhenInvalidRecord() {
        String invalidInput = "INVALID";
        Deserializer deserializer =  new Deserializer();

        AvailabilatorRecord result = deserializer.deserialize(
                TOPIC, invalidInput.getBytes(StandardCharsets.UTF_8));

        assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldDeserializeValidRecordWithoutOptionalField() throws Exception {
        Long expectedTimestamp = 1619853814308L;
        String expectedAddress = "http://host";
        Integer expectedResponseTime = 185;
        Integer expectedStatusCode = 200;
        String input = String.format("""
                    {
                    "schema":{
                        "type":"struct",
                                "fields": [
                        {
                            "field":"timestamp", "type":"int64", "name":"org.apache.kafka.connect.data.Timestamp", "optional":false
                        },
                        {
                            "field":"address", "type":"string", "optional":false
                        },
                        {
                            "field":"responseTime", "type":"int32", "optional":false
                        },
                        {
                            "field":"statusCode", "type":"int16", "optional":false
                        },
                        {
                            "field":"matches", "type":"boolean", "optional":true
                        }
                    ]
                    },
                    "payload":{
                        "timestamp":%d,
                        "address":"%s",
                        "responseTime":%d,
                        "statusCode":%d
                    }
                }
                """,
                expectedTimestamp,
                expectedAddress,
                expectedResponseTime,
                expectedStatusCode);
        Deserializer deserializer =  new Deserializer();

        AvailabilatorRecord result = deserializer.deserialize(
                TOPIC,
                input.getBytes(StandardCharsets.UTF_8));

        assertThat(result.getTimestamp(), equalTo(expectedTimestamp));
        assertThat(result.getAddress(), equalTo(expectedAddress));
        assertThat(result.getResponseTime(), equalTo(expectedResponseTime));
        assertThat(result.getStatusCode(), equalTo(expectedStatusCode));
        assertThat(result.matches(), equalTo(Boolean.FALSE));
    }

    @Test
    public void shouldDeserializeValidRecordWithOptionalField() throws Exception {
        Long expectedTimestamp = 1619853814308L;
        String expectedAddress = "http://host";
        Integer expectedResponseTime = 185;
        Integer expectedStatusCode = 200;
        boolean expectedMatches = true;
        String input = String.format("""
                    {
                    "schema":{
                        "type":"struct",
                                "fields": [
                        {
                            "field":"timestamp", "type":"int64", "name":"org.apache.kafka.connect.data.Timestamp", "optional":false
                        },
                        {
                            "field":"address", "type":"string", "optional":false
                        },
                        {
                            "field":"responseTime", "type":"int32", "optional":false
                        },
                        {
                            "field":"statusCode", "type":"int16", "optional":false
                        },
                        {
                            "field":"matches", "type":"boolean", "optional":true
                        }
                    ]
                    },
                    "payload":{
                        "timestamp":%d,
                        "address":"%s",
                        "responseTime":%d,
                        "statusCode":%d,
                        "matches":%b
                    }
                }
                """,
                expectedTimestamp,
                expectedAddress,
                expectedResponseTime,
                expectedStatusCode,
                expectedMatches);
        Deserializer deserializer =  new Deserializer();

        AvailabilatorRecord result = deserializer.deserialize(
                TOPIC,
                input.getBytes(StandardCharsets.UTF_8));

        assertThat(result.getTimestamp(), equalTo(expectedTimestamp));
        assertThat(result.getAddress(), equalTo(expectedAddress));
        assertThat(result.getResponseTime(), equalTo(expectedResponseTime));
        assertThat(result.getStatusCode(), equalTo(expectedStatusCode));
        assertThat(result.matches(), equalTo(expectedMatches));
    }
}

