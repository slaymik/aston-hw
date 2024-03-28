package ru.aston_hw.repository;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.aston_hw.entities.Weather;

import java.sql.Timestamp;

public class WeatherRepository  {
    private final SessionFactory sessionFactory;

    public WeatherRepository(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public void saveWeather(String location, String weather){
        Weather weatherData = new Weather(new Timestamp(System.currentTimeMillis()),location,weather);
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(weatherData);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}