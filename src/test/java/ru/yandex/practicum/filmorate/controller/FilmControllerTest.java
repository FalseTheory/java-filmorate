package ru.yandex.practicum.filmorate.controller;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    URI url = URI.create("/films");

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FilmService service;


    @Test
    void contextLoads() {
    }

    @Test
    public void creatingFilmWithEmptyBodyShouldThrowInternalError() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(500, result.getResponse().getStatus());


    }

    @Test
    public void creatingFilmWithoutNameShouldCauseException() throws Exception {
        String body = "{\n" +
                      "\"name\": \"\",\n" +
                      "\"description\": \"Description\",\n" +
                      "\"releaseDate\": \"1980-03-25\",\n" +
                      "\"duration\": 200\n" +
                      "}";

        MvcResult res = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();

        assertEquals(400, res.getResponse().getStatus());


    }

    @Test
    public void creatingFilmWithoutReleaseDateShouldCauseBadRequest() throws Exception {
        String body = "{\n" +
                      "\"name\": \"Test\",\n" +
                      "\"description\": \"Description\",\n" +
                      "\"duration\": 200\n" +
                      "}";

        MvcResult res = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();

        assertEquals(400, res.getResponse().getStatus());


    }

    @Test
    public void creatingFilmWithoutDurationShouldCauseException() throws Exception {
        String body = "{\n" +
                      "\"name\": \"Test\",\n" +
                      "\"description\": \"Description\",\n" +
                      "\"releaseDate\": \"1980-03-25\"\n" +
                      "}";

        MvcResult res = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();


        assertEquals(400, res.getResponse().getStatus());


    }

    @Test
    public void creatingFilmWithoutDescriptionShouldReturnOkStatusCode() throws Exception {
        String body = "{\n" +
                      "\"name\": \"Test\",\n" +
                      "\"releaseDate\": \"1980-03-25\",\n" +
                      "\"duration\": 200\n" +
                      "}";

        MvcResult res = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();
        assertEquals(200, res.getResponse().getStatus());


    }

    @Test
    public void creatingFilmWithNotANumberDurationShouldThrowInternalServerError() throws Exception {
        String body = "{\n" +
                      "\"name\": \"Test\",\n" +
                      "\"description\": \"Description\",\n" +
                      "\"releaseDate\": \"1980-03-25\",\n" +
                      "\"duration\": \"NotANumber\"\n" +
                      "}";

        MvcResult res = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();

        assertEquals(500, res.getResponse().getStatus());
    }

    @Test
    public void creatingFilmWithNegativeDurationShouldThrowException() throws Exception {
        String body = "{\n" +
                      "\"name\": \"Test\",\n" +
                      "\"description\": \"Description\",\n" +
                      "\"releaseDate\": \"1980-03-25\",\n" +
                      "\"duration\": -9999\n" +
                      "}";

        MvcResult res = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();


        assertEquals(400, res.getResponse().getStatus());


    }

    @Test
    public void creatingFilmRecordBeforeFirstMovieCreationShouldThrowException() throws Exception {
        String body = "{\n" +
                      "\"name\": \"Test\",\n" +
                      "\"description\": \"Description\",\n" +
                      "\"releaseDate\": \"1780-03-25\",\n" +
                      "\"duration\": 100\n" +
                      "}";


        MvcResult res = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();

        assertEquals(400, res.getResponse().getStatus());


    }

    @Test
    public void creatingFilmRecordOfFirstMovieShouldReturnOkStatusCode() throws Exception {
        String body = "{\n" +
                      "\"name\": \"Test\",\n" +
                      "\"description\": \"Description\",\n" +
                      "\"releaseDate\": \"1895-12-28\",\n" +
                      "\"duration\": 100\n" +
                      "}";


        MvcResult res = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();


        assertEquals(200, res.getResponse().getStatus());

    }
}
