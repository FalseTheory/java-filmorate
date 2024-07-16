package ru.yandex.practicum.filmorate.controller;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    URI url = URI.create("/films");

    @Autowired
    private MockMvc mvc;


    @Test
    void contextLoads() {
    }

    @Test
    public void creatingFilmWithEmptyBodyShouldThrowBadRequest() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(400, result.getResponse().getStatus());


    }

    @Test
    public void creatingFilmWithoutNameShouldCauseException() {
        String body = """
                {
                  "name": "",
                  "description": "Description",
                  "releaseDate": "1980-03-25",
                  "duration": 200
                }""";

        assertThrows(ServletException.class, () -> mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn());


    }

    @Test
    public void creatingFilmWithoutReleaseDateShouldCauseException() {
        String body = """
                {
                  "name": "Test",
                  "description": "Description",
                  "duration": 200
                }""";

        assertThrows(ServletException.class, () -> mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn());


    }

    @Test
    public void creatingFilmWithoutDurationShouldCauseException() {
        String body = """
                {
                  "name": "Test",
                  "description": "Description",
                  "releaseDate": "1980-03-25"
                }""";

        assertThrows(ServletException.class, () -> mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn());


    }

    @Test
    public void creatingFilmWithoutDescriptionShouldReturnOkStatusCode() throws Exception {
        String body = """
                {
                  "name": "Test",
                  "releaseDate": "1980-03-25",
                  "duration": 200
                }""";

        MvcResult res = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();
        assertEquals(200, res.getResponse().getStatus());


    }

    @Test
    public void creatingFilmWithNotANumberDurationShouldThrowBadRequest() throws Exception {
        String body = """
                {
                  "name": "Test",
                  "description": "Description",
                  "releaseDate": "1980-03-25",
                  "duration": "NotANumber"
                }""";

        MvcResult res = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();

        assertEquals(400, res.getResponse().getStatus());
    }

    @Test
    public void creatingFilmWithNegativeDurationShouldThrowException() {
        String body = """
                {
                  "name": "Test",
                  "description": "Description",
                  "releaseDate": "1980-03-25",
                  "duration": -9999
                }""";

        assertThrows(ServletException.class, () -> mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn());


    }

    @Test
    public void creatingFilmRecordBeforeFirstMovieCreationShouldThrowException() {
        String body = """
                {
                  "name": "Test",
                  "description": "Description",
                  "releaseDate": "1780-03-25",
                  "duration": 100
                }""";

        assertThrows(ServletException.class, () -> mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn());


    }

    @Test
    public void creatingFilmRecordOfFirstMovieShouldReturnOkStatusCode() throws Exception {
        String body = """
                {
                  "name": "Test",
                  "description": "Description",
                  "releaseDate": "1895-12-28",
                  "duration": 100
                }""";


        MvcResult res = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();


        assertEquals(200, res.getResponse().getStatus());

    }
}
