package foo.labs.availabilator.consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static foo.labs.availabilator.consumer.Writer.INSERT_TEMPLATE;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WriterTest {
    public static final String DB_HOST = "dbHost";
    public static final String DB_PORT = "dbPort";
    public static final String DB_USER = "dbUser";
    public static final String DB_PASS = "dbPass";
    public static final String DB_NAME = "dbName";
    public static final String DB_TABLE = "dbTable";
    public static final String LOCATION = "http://host";
    public static final Integer RESPONSE_TIME = 185;
    public static final Integer STATUS_CODE = 200;
    public static final Boolean MATCHES = Boolean.TRUE;

    @Mock
    DbConnectionBuilder dbConnectionBuilder;

    @Mock
    Connection conn;

    @Mock
    PreparedStatement statement;

    Map<String, String> ctx = Map.of(
            ContextBuilder.DB_HOST, DB_HOST,
            ContextBuilder.DB_PORT, DB_PORT,
            ContextBuilder.DB_USER, DB_USER,
            ContextBuilder.DB_USER_PASS, DB_PASS,
            ContextBuilder.DB_NAME, DB_NAME,
            ContextBuilder.DB_TABLE_NAME, DB_TABLE);

    @Test
    public void shouldCreateConnectionUsingValuesInCtx() {
        Writer writer = new Writer(ctx, dbConnectionBuilder);

        verify(dbConnectionBuilder).build(DB_HOST, DB_PORT, DB_USER, DB_PASS, DB_NAME);
    }

    @Test
    public void shouldStoreAvailability() throws SQLException {
        Instant expectedTimestamp = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        AvailabilatorRecord availability = buildAvailabilityRecord(expectedTimestamp);
        String expectedSql = String.format(INSERT_TEMPLATE, DB_TABLE);
        when(dbConnectionBuilder.build(DB_HOST, DB_PORT, DB_USER, DB_PASS, DB_NAME))
                .thenReturn(conn);
        when(conn.prepareStatement(expectedSql))
                .thenReturn(statement);
        Writer writer = new Writer(ctx, dbConnectionBuilder);

        writer.store(availability);

        verify(statement).setObject(
                1,
                LocalDateTime.ofInstant(expectedTimestamp, ZoneOffset.UTC));
        verify(statement).setString(2, LOCATION);
        verify(statement).setInt(3, RESPONSE_TIME);
        verify(statement).setInt(4, STATUS_CODE);
        verify(statement).setBoolean(5, MATCHES);
        verify(statement).executeUpdate();
        verify(statement).close();
    }

    @Test
    public void shouldContinueWorkingWhenSQLErrorAppears() throws SQLException {
        Instant expectedTimestamp = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        AvailabilatorRecord availability = buildAvailabilityRecord(expectedTimestamp);
        String expectedSql = String.format(INSERT_TEMPLATE, DB_TABLE);
        when(dbConnectionBuilder.build(DB_HOST, DB_PORT, DB_USER, DB_PASS, DB_NAME))
                .thenReturn(conn);
        when(conn.prepareStatement(expectedSql))
                .thenReturn(statement);
        when(statement.executeUpdate())
                .thenThrow(new SQLException("Dummy error"));
        Writer writer = new Writer(ctx, dbConnectionBuilder);

        writer.store(availability);

        verify(statement).setObject(
                1,
                LocalDateTime.ofInstant(expectedTimestamp, ZoneOffset.UTC));
        verify(statement).setString(2, LOCATION);
        verify(statement).setInt(3, RESPONSE_TIME);
        verify(statement).setInt(4, STATUS_CODE);
        verify(statement).setBoolean(5, MATCHES);
        verify(statement).executeUpdate();
        verify(statement).close();
    }

    private AvailabilatorRecord buildAvailabilityRecord(Instant expectedTimestamp) {
        return AvailabilatorRecord.build(
                expectedTimestamp.toEpochMilli(),
                LOCATION,
                RESPONSE_TIME,
                STATUS_CODE,
                MATCHES);
    }
}
