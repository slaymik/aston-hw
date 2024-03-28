package ru.aston_hw.servlets;

import com.google.gson.JsonSyntaxException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.aston_hw.repository.WeatherRepository;
import ru.aston_hw.utils.CountryCodeFormatter;
import ru.aston_hw.utils.TemperatureConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@WebServlet("/weather")
public class WeatherServlet extends HttpServlet {
    private static final String CONFIG_FILE = "config.properties";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String country = request.getParameter("country");
        String city = request.getParameter("city");
        String weather = getWeather(country, city);
        SessionFactory sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();
        WeatherRepository repository = new WeatherRepository(sessionFactory);
        repository.saveWeather("%s, %s".formatted(city, country), weather);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("Погода в %s, %s: %s".formatted(city, country, weather));
    }

    private String getWeather(String country, String city) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(buildApiUrl(country, city)).openConnection();
        connection.setRequestMethod("GET");
        return parseApiResponse(fetchApiResponse(connection));
    }

    private String fetchApiResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder responseBody = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }
            reader.close();
            return responseBody.toString();
        } else {
            return "Ошибка: " + responseCode;
        }
    }

    private String parseApiResponse(String responseBody) {
        JsonElement root;
        try {
            root = new Gson().fromJson(responseBody, JsonElement.class);
        } catch (JsonSyntaxException e) {
            return "Данные об этом городе не найдены";
        }
        String description = root.getAsJsonObject().getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

        if (root.getAsJsonObject().has("main")) {
            double temperature = root.getAsJsonObject().getAsJsonObject("main").get("temp").getAsDouble();
            return "%s, %.2f°C".formatted(description, TemperatureConverter.convertKelvinToCelsius(temperature));
        } else {
            return "%s: Информация недоступна".formatted(description);
        }
    }

    private String buildApiUrl(String country, String city) throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
        CountryCodeFormatter formatter = new CountryCodeFormatter(properties);
        String apiKey = properties.getProperty("openweathermap.api.key");
        return properties.getProperty("openweathermap.api.url")
                .concat("?q=%s,%s&appid=%s"
                        .formatted(URLEncoder.encode(city, StandardCharsets.UTF_8), formatter.getCountryCode(country), apiKey));
    }
}