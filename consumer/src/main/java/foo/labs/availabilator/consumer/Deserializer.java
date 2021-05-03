package foo.labs.availabilator.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Deserializer implements org.apache.kafka.common.serialization.Deserializer<AvailabilatorRecord> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Deserializer.class);

    private final ObjectMapper mapper;

    public Deserializer() {
        mapper = new ObjectMapper();
    }

    @Override
    public AvailabilatorRecord deserialize(String s, byte[] bytes) {
        try {
            return mapper.readValue(bytes, AvailabilatorRecord.class);
        } catch (IOException e) {
            LOGGER.error("Error deserializing AvailabilatorRecord from "
                    + new String(bytes, StandardCharsets.UTF_8));
            return null;
        }
    }
}
