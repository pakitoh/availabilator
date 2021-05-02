package foo.labs.availabilator.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"schema"})
public class AvailabilatorRecord {

    private AvailabilatorRecordPayload payload;

    public void setPayload(AvailabilatorRecordPayload payload) {
        this.payload = payload;
    }

    public Long getTimestamp() {
        return payload.getTimestamp();
    }

    public String getAddress() {
        return payload.getAddress();
    }

    public int getResponseTime() {
        return payload.getResponseTime();
    }

    public int getStatusCode() {
        return payload.getStatusCode();
    }

    public boolean matches() {
        return payload.matches();
    }

    @Override
    public String toString() {
        return  "AvailabilatorRecord=" + payload;
    }

    public static AvailabilatorRecord build(
            long timestamp,
            String address,
            int responseTime,
            int statusCode,
            boolean matches) {
        AvailabilatorRecord availability = new AvailabilatorRecord();
        availability.setPayload(
                new AvailabilatorRecordPayload(
                        timestamp,
                        address,
                        responseTime,
                        statusCode,
                        matches));
        return availability;
    }
}
