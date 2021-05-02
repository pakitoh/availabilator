package foo.labs.availabilator.consumer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AvailabilatorRecordPayload {

    private final Long timestamp;
    private final String address;
    private final int responseTime;
    private final int statusCode;
    private final boolean matches;

    @JsonCreator
    public AvailabilatorRecordPayload(
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("address") String address,
            @JsonProperty("responseTime") int responseTime,
            @JsonProperty("statusCode") int statusCode,
            @JsonProperty("matches") boolean matches) {
        this.timestamp = timestamp;
        this.address = address;
        this.responseTime = responseTime;
        this.statusCode = statusCode;
        this.matches = matches;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getAddress() {
        return address;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean matches() {
        return matches;
    }

    @Override
    public String toString() {
        return "{" +
                "timestamp=" + timestamp +
                ", address='" + address + '\'' +
                ", responseTime=" + responseTime +
                ", statusCode=" + statusCode +
                ", matches=" + matches +
                '}';
    }
}
