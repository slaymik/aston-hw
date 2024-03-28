package ru.aston_hw.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston_hw.entities.Weather;


import static org.junit.jupiter.api.Assertions.*;

class WeatherRepositoryTest {

    private WeatherRepository sut;
    private SessionFactory sessionFactory;

    @BeforeEach
    void init() {
        sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();
        sut = new WeatherRepository(sessionFactory);
    }

    @AfterEach
    void cleanUp(){
        sessionFactory.close();
    }

    @Test
    void saveWeather() {
        String location = "Россия,Москва";
        String weather = "broken clouds";

        sut.saveWeather(location, weather);

        Session session = sessionFactory.openSession();
        Weather savedWeather = session.createQuery("FROM Weather WHERE location = :location", Weather.class)
                .setParameter("location", location)
                .uniqueResult();

        assertNotNull(savedWeather);
        assertEquals(location, savedWeather.getLocation());
        assertEquals(weather, savedWeather.getWeather());
    }
}