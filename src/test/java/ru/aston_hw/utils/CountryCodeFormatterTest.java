package ru.aston_hw.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class CountryCodeFormatterTest {
    private CountryCodeFormatter sut;

    @BeforeEach
    void init() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
        sut = new CountryCodeFormatter(properties);
    }

    @Test
    void getCountryCodeValid() {
        String country = "Russia";
        assertEquals("RU", sut.getCountryCode(country));
    }

    @Test
    void getCountryCodeInvalid() {
        String country = "aston";
        assertEquals("Unknown", sut.getCountryCode(country));
    }

    @Test
    void getCountryCodeCaseInsensitiveValid() {
        String country = "rUSsia";
        assertEquals("RU", sut.getCountryCode(country));
    }

    @Test
    void getCountryCodeLanguageInsensitiveValid() {
        String country = "Австралия";
        assertEquals("AU", sut.getCountryCode(country));
    }
}