package ru.yandex.practicum.filmorate.controller;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.net.URI;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    URI url = URI.create("/users");


    @Autowired
    private MockMvc mvc;


    @Test
    void contextLoads() {
    }

    @Test
    public void creatingUserWithEmptyBodyShouldThrowBadRequest() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(400, result.getResponse().getStatus());


    }

    @Test
    public void updatingUserWithNotANumberIdShouldThrowBadRequest() throws Exception {
        String bodyPut = """
                {
                  "login": "doloreUpdate",
                  "name": "est adipisicing",
                  "id": "notANumber",
                  "email": "mail@yandex.ru",
                  "birthday": "1976-09-20"
                }""";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON).content(bodyPut)).andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void creatingUserWithFutureBirthdayShouldThrowValidationException() {
        String bodyPost = """
                {
                  "login": "doloreUpdate",
                  "name": "est adipisicing",
                  "email": "mail@yandex.ru",
                  "birthday": "2028-09-20"
                }""";

        // Ошибка валидации выкидывается, но не понимаю как проверить именно её, servletException выкидывается первой
        assertThrows(ServletException.class, () -> mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(bodyPost)).andReturn());


    }

}
