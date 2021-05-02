package foo.labs.availabilator.consumer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnectionBuilder {

    public static final String POSTGRES_URL_TEMPLATE = "jdbc:postgresql://%s:%s/%s";
    public static final String USER_PROPERTY_NAME = "user";
    public static final String USER_PASS_PROPERTY_NAME = "password";

    public Connection build(
            String dbHost,
            String dbPort,
            String dbUser,
            String dbPassword,
            String dbName) {
        try {
            Properties dbProps = new Properties();
            dbProps.setProperty(USER_PROPERTY_NAME, dbUser);
            dbProps.setProperty(USER_PASS_PROPERTY_NAME, dbPassword);
            return DriverManager.getConnection(
                    String.format(POSTGRES_URL_TEMPLATE, dbHost, dbPort, dbName),
                    dbProps);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the DB: " + e.getMessage(), e);
        }
    }
}
