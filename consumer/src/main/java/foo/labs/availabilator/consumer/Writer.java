package foo.labs.availabilator.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

public class Writer {

    public static final String INSERT_TEMPLATE = "INSERT INTO %s " +
            "(timestamp, address, \"responseTime\", \"statusCode\", matches) " +
            "VALUES (?, ?, ?, ?, ?)";
    private final Logger logger = LoggerFactory.getLogger(Deserializer.class);
    private Connection conn;
    private String tableName;

    public Writer(Map<String, String> ctx) {
        this(ctx, new DbConnectionBuilder());
    }

    public Writer(Map<String, String> ctx, DbConnectionBuilder dbConnectionBuilder) {
        conn = dbConnectionBuilder.build(
                ctx.get(ContextBuilder.DB_HOST),
                ctx.get(ContextBuilder.DB_PORT),
                ctx.get(ContextBuilder.DB_USER),
                ctx.get(ContextBuilder.DB_USER_PASS),
                ctx.get(ContextBuilder.DB_NAME));
        tableName = ctx.get(ContextBuilder.DB_TABLE_NAME);
    }

    public void store(AvailabilatorRecord availabity) {
        try (PreparedStatement st = conn.prepareStatement(String.format(INSERT_TEMPLATE, tableName))) {
            st.setObject(1, LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(availabity.getTimestamp()),
                    ZoneOffset.UTC));
            st.setString(2, availabity.getAddress());
            st.setInt(3, availabity.getResponseTime());
            st.setInt(4, availabity.getStatusCode());
            st.setBoolean(5, availabity.matches());
            int rowsUpdated = st.executeUpdate();
            logger.trace(rowsUpdated + " rows updated");
        } catch (SQLException e) {
            logger.warn("Error inserting in DB: " + e.getMessage(), e);
        }
    }
}
