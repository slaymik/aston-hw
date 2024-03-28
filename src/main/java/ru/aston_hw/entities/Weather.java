package ru.aston_hw.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "weather")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "date_time", nullable = false)
    private Timestamp dateTime;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "weather", nullable = false)
    private String weather;

    public Weather(){
    }

    public Weather(Timestamp dateTime, String location, String weather){
        this.dateTime = dateTime;
        this.weather = weather;
        this.location = location;
    }

    public String getWeather() {
        return weather;
    }

    public String getLocation() {
        return location;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public Integer getId() {
        return id;
    }
}