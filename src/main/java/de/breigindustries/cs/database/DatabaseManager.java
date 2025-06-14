package de.breigindustries.cs.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager {
    
    private static final String DB_NAME = "levi.db";
    private static final String EMPTY_DB_RESOURCE = "/empty.db";
    private static final String DB_URL = "jdbc:sqlite:levi.db";
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    public static void ensureDatabaseExists() {
        File dbFile = new File(DB_NAME);

        if (dbFile.exists() && dbFile.canRead()) {
            logger.info("Database exists and is readable.");
            return;
        }

        logger.warn("Database missing or unreadable. Copying from resources...");

        try (InputStream in = DatabaseManager.class.getResourceAsStream(EMPTY_DB_RESOURCE)) {
            if (in == null) {
                throw new FileNotFoundException("Resource " + EMPTY_DB_RESOURCE + " not found!");
            }

            Files.copy(in, dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.info("Database copied successfully.");
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("Failed to copy database from resources.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Utility only
    private static void executeSQL(String sql) {
        try (Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            
            statement.execute(sql);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            logger.error(e.getSQLState());
        }
    }

}
