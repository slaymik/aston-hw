package ru.aston_hw.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.Parameter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(MockServerExtension.class)
@MockServerSettings(ports = 1080)
class WeatherServletTest {
    private MockServerClient mockServer;
    private WeatherServlet sut;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void init(ClientAndServer mockServer) {
        this.mockServer = mockServer;
        sut = new WeatherServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void doGetReturnsCorrectWeather() throws IOException {
        when(request.getParameter("country")).thenReturn("Russia");
        when(request.getParameter("city")).thenReturn("Yekaterinburg");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        mockServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/weather")
                        .withQueryStringParameters(new Parameter("q", "Yekaterinburg,RU"), new Parameter("appid", "apiKey")))
                .respond(response()
                        .withStatusCode(200)
                        .withBody("""
                                {
                                "weather":
                                    [
                                        {
                                        "description": "few clouds"
                                        }
                                    ],
                                "main":
                                    {
                                        "temp": 273.15
                                    }
                                 }
                                """));

        sut.doGet(request, response);

        verify(response).setContentType("text/html;charset=UTF-8");
        assertEquals("Погода в Yekaterinburg, Russia: few clouds, 0.00°C", stringWriter.toString().trim());
    }

    @Test
    void doGetRussianInputReturnsCorrectWeather() throws IOException {
        when(request.getParameter("country")).thenReturn("Россия");
        when(request.getParameter("city")).thenReturn("Екатеринбург");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        mockServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/weather")
                        .withQueryStringParameters(new Parameter("q", "Екатеринбург,RU"), new Parameter("appid", "apiKey")))
                .respond(response()
                        .withStatusCode(200)
                        .withBody("""
                                {
                                "weather":
                                    [
                                        {
                                        "description": "few clouds"
                                        }
                                    ],
                                "main":
                                    {
                                        "temp": 273.15
                                    }
                                 }
                                """));

        sut.doGet(request, response);

        verify(response).setContentType("text/html;charset=UTF-8");
        assertEquals("Погода в Екатеринбург, Россия: few clouds, 0.00°C", stringWriter.toString().trim());
    }

    @Test
    void doGetInvalidInput() throws IOException {
        when(request.getParameter("country")).thenReturn("null");
        when(request.getParameter("city")).thenReturn("null");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        mockServer
                .when(request()
                        .withMethod("GET")
                        .withPath("/weather")
                        .withQueryStringParameters(new Parameter("q", "null,Unknown"), new Parameter("appid", "apiKey")))
                .respond(response()
                        .withStatusCode(404));

        sut.doGet(request, response);

        verify(response).setContentType("text/html;charset=UTF-8");
        assertEquals("Погода в null, null: Данные об этом городе не найдены", stringWriter.toString().trim());
    }
}