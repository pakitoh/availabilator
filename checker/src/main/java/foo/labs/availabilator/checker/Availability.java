package foo.labs.availabilator.checker;

public record Availability(Long timestamp, String address, Long responseTime, Integer statusCode, Boolean matchesRegExp) {

    public Availability(Long timestamp, String address, Long responseTime, Integer statusCode) {
        this(timestamp, address, responseTime, statusCode, null);
    }

    public String asJson() {
        return String.format("""
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
                              "statusCode":%d%s
                            }
                        }
                        """,
                timestamp,
                address,
                responseTime,
                statusCode,
                matchesRegExp != null? ",\n      \"matches\":true" : "");
    }
}
