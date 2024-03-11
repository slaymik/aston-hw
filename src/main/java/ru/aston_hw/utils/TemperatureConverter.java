package ru.aston_hw.utils;

public class TemperatureConverter {
    private static final double ABSOLUTE_ZERO_IN_CELSIUS = -273.15;

    public static double convertKelvinToCelsius(double temperature) {
        return temperature + ABSOLUTE_ZERO_IN_CELSIUS;
    }
}