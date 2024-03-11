package ru.aston_hw.utils;

import com.google.gson.*;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CountryCodeFormatter {
    private final Map<String, String> countryCodes;

    public CountryCodeFormatter(Properties properties) {
        this.countryCodes = getCountryCodes(properties);
    }

    public String getCountryCode(String countryName) {
        return countryCodes.entrySet().stream()
                .filter(entry -> entry.getKey().toLowerCase().contains(countryName.toLowerCase()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("Unknown");
    }

    private Map<String, String> getCountryCodes(Properties properties) {
        Map<String, String> countryCodes = new HashMap<>();
        JsonArray countriesArray = JsonParser.parseReader(new InputStreamReader(Objects.requireNonNull(getClass()
                .getClassLoader().getResourceAsStream(properties.getProperty("countries.json.file"))), StandardCharsets.UTF_8)).getAsJsonObject().getAsJsonArray(properties.getProperty("countries.json.field.countries"));

        for (int i = 0; i < countriesArray.size(); i++) {
            JsonElement countryElement = countriesArray.get(i);
            if (countryElement.isJsonObject()) {
                JsonObject country = countryElement.getAsJsonObject();
                String nameField = properties.getProperty("countries.json.field.name");
                String countryNames = country.getAsJsonPrimitive(nameField).getAsString();
                String countryCode = country.getAsJsonPrimitive(properties.getProperty("countries.json.field.code")).getAsString();

                for (Map.Entry<String, JsonElement> entry : country.entrySet()) {
                    if (entry.getKey().startsWith(nameField.concat("_"))) {
                        countryNames = countryNames.concat(", ").concat(entry.getValue().getAsString());
                    }
                }
                countryCodes.put(countryNames, countryCode);
            }
        }
        return countryCodes;
    }
}