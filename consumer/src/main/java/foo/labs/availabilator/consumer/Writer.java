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
            "VALUES (?, ?, ?, ?, ?) " +
            "ON CONFLICT DO NOTHING";
    private static final Logger LOGGER = LoggerFactory.getLogger(Deserializer.class);

    private String dbHost;
    private String dbPort;
    private String dbUser;
    private String dbUserPass;
    private String dbName;
    private String tableName;
    private DbConnectionBuilder dbConnectionBuilder;

    public Writer(Map<String, String> ctx) {
        this(ctx, new DbConnectionBuilder());
    }

    public Writer(Map<String, String> ctx, DbConnectionBuilder dbConnectionBuilder) {
        dbHost = ctx.get(ContextBuilder.DB_HOST);
        dbPort = ctx.get(ContextBuilder.DB_PORT);
        dbUser = ctx.get(ContextBuilder.DB_USER);
        dbUserPass = ctx.get(ContextBuilder.DB_USER_PASS);
        dbName = ctx.get(ContextBuilder.DB_NAME);
        tableName = ctx.get(ContextBuilder.DB_TABLE_NAME);
        this.dbConnectionBuilder = dbConnectionBuilder;
    }

    public void store(AvailabilatorRecord availability) {
        try (Connection conn = dbConnectionBuilder.build(dbHost, dbPort, dbUser, dbUserPass, dbName);
             PreparedStatement st = conn.prepareStatement(String.format(INSERT_TEMPLATE, tableName))) {
            st.setObject(1, LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(availability.getTimestamp()),
                    ZoneOffset.UTC));
            st.setString(2, availability.getAddress());
            st.setInt(3, availability.getResponseTime());
            st.setInt(4, availability.getStatusCode());
            st.setBoolean(5, availability.matches());
            int rowsUpdated = st.executeUpdate();
            LOGGER.trace(rowsUpdated + " rows updated");
        } catch (SQLException | RuntimeException e) {
            LOGGER.warn("Error storing availability in DB: " + e.getMessage(), e);
        }
    }
}
