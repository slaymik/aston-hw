package ru.aston_hw.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;


class DatabaseConnectionTest {

    private DatabaseConnection sut;
    private Connection connection;
    private static final Properties properties = new Properties();

    @BeforeEach
    void init() throws IOException, SQLException, ClassNotFoundException {
        properties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
        sut = new DatabaseConnection(properties);
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(properties.getProperty("jdbc.datasource.url"),
                properties.getProperty("jdbc.datasource.username"),
                properties.getProperty("jdbc.datasource.password"));
    }

    @Test
    void saveWeatherData() throws SQLException {
        sut.saveWeatherData("Екатеринбург, Россия", "scattered clouds, -7.23°C");

        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM weather_data WHERE location = 'Екатеринбург, Россия'");
        assertTrue(resultSet.next());
        assertEquals("Екатеринбург, Россия",resultSet.getString("location"));
        assertEquals("scattered clouds, -7.23°C", resultSet.getString("weather"));
    }

    @AfterEach
    void dropTable() throws SQLException {
        connection.prepareStatement("DROP TABLE IF EXISTS weather_data").execute();
    }
}