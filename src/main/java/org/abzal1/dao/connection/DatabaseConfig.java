package org.abzal1.dao.connection;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class DatabaseConfig {


    private static final String PROPERTIES_FILE = "config.properties";
    @Getter
    private static String dbUrl;
    @Getter
    private static String dbUser;
    @Getter
    private static String dbPassword;

    // The static block is ideal for one-time initialization of static variables.
    // In this case, it ensures that the database connection details are loaded
    // as soon as the class is loaded, making them available for all subsequent uses of the class.
    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            if (input == null) {
                throw new IOException("Unable to find " + PROPERTIES_FILE);
            }
            properties.load(input);

            dbUrl = properties.getProperty("db.url");
            dbUser = properties.getProperty("db.user");
            dbPassword = properties.getProperty("db.password");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
