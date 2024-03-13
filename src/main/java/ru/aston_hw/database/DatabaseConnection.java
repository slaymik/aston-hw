package ru.aston_hw.database;

import java.sql.*;
import java.util.Properties;


public class DatabaseConnection {
    private final Connection connection;

    public DatabaseConnection(Properties properties) {
        try {
            Class.forName("org.postgresql.Driver"); // Без этого не работает
            connection = DriverManager.getConnection(properties.getProperty("jdbc.datasource.url"),
                    properties.getProperty("jdbc.datasource.username"),
                    properties.getProperty("jdbc.datasource.password"));
            PreparedStatement createStatement = connection.prepareStatement("""
                        CREATE TABLE IF NOT EXISTS weather_data
                        (
                          id serial NOT NULL,
                          data_time TIMESTAMP NOT NULL,
                          location varchar(255) NOT NULL,
                          weather varchar(255) NOT NULL,
                          PRIMARY KEY (id),
                          UNIQUE (id)
                        );
                        """);
            createStatement.execute();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Возникла ошибка в работе базы данных",e);
        }
    }

    public void saveWeatherData(String location, String weather) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement("""
                    INSERT INTO weather_data (data_time,location,weather)
                                    VALUES (? ,?, ?);
                    """);
            insertStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            insertStatement.setString(2, location);
            insertStatement.setString(3, weather);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Возникла ошибка при сохранении данных",e);
        }
    }
}