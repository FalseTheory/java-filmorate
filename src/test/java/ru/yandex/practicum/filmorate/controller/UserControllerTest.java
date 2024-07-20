package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("POST запрос с пустым телом должен выбрасывать ошибку 400")
    @Test
    public void creatingUserWithEmptyBodyShouldThrowBadRequest() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content("")).andReturn();

        assertEquals(400, result.getResponse().getStatus());


    }

    @DisplayName("Попытка обновить пользователя с некорректным id должно выдавать код 400")
    @Test
    public void updatingUserWithNotANumberIdShouldThrowBadRequest() throws Exception {
        String bodyPut = "{\n" +
                         "\"login\": \"doloreUpdate\",\n" +
                         "\"name\": \"est adipisicing\",\n" +
                         "\"id\": \"notANumber\",\n" +
                         "\"email\": \"mail@yandex.ru\",\n" +
                         "\"birthday\": \"1976-09-20\"\n" +
                         "}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON).content(bodyPut)).andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @DisplayName("Создание пользователя с днём рождения в будущем должно выдавать ошибку")
    @Test
    public void creatingUserWithFutureBirthdayShouldThrowValidationException() throws Exception {
        String bodyPost = "{\n" +
                          "\"login\": \"doloreUpdate\",\n" +
                          "\"name\": \"est adipisicing\",\n" +
                          "\"email\": \"mail@yandex.ru\",\n" +
                          "\"birthday\": \"2028-09-20\"\n" +
                          "}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(bodyPost)).andReturn();

        assertEquals(400, result.getResponse().getStatus());


    }

    @DisplayName("Создание пользователя с некорректным email должно приводить к ошибке")
    @Test
    public void creatingUserWithIncorrectEmailPatternShouldResultInError() throws Exception {
        String bodyPost = "{\n" +
                          "\"login\": \"doloreUpdate\",\n" +
                          "\"name\": \"est adipisicing\",\n" +
                          "\"email\": \"@mailyandex.ru\",\n" +
                          "\"birthday\": \"2020-09-20\"\n" +
                          "}";


        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(bodyPost)).andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @DisplayName("Создание пользователя с некорректным логином должно приводить к ошибке")
    @Test
    public void creatingUserWithIncorrectLoginShouldResultInError() throws Exception {
        String bodyPost = "{\n" +
                          "\"login\": \"dolore   Update\",\n" +
                          "\"name\": \"est adipisicing\",\n" +
                          "\"email\": \"mail@yandex.ru\",\n" +
                          "\"birthday\": \"2020-09-20\"\n" +
                          "}";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(bodyPost)).andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    @DisplayName("Обновление пользователя с неправильным email должно быть невозможно")
    public void updatingUserWithWrongEmailShouldReturnError() throws Exception {
        String bodyPost = "{\n" +
                          "\"login\": \"doloreUpdate\",\n" +
                          "\"name\": \"est adipisicing\",\n" +
                          "\"email\": \"mail@mail.ru\",\n" +
                          "\"birthday\": \"2020-09-20\"\n" +
                          "}";
        String bodyPut = "{\n" +
                         "\"id\": \"1\",\n" +
                         "\"login\": \"doloreUpdate\",\n" +
                         "\"name\": \"est adipisicing\",\n" +
                         "\"email\": \"mailyandex.ru\",\n" +
                         "\"birthday\": \"2020-10-20\"\n" +
                         "}";

        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(bodyPost)).andReturn();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON).content(bodyPut)).andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    @DisplayName("Обновление c тем же email должно возвращать статус код 200")
    public void updatingUserWithSameEmailShouldReturnOkStatusCode() throws Exception {
        String bodyPost = "{\n" +
                          "\"login\": \"doloreUpdate\",\n" +
                          "\"name\": \"est adipisicing\",\n" +
                          "\"email\": \"mail@yandex.ru\",\n" +
                          "\"birthday\": \"2020-09-20\"\n" +
                          "}";
        String bodyPut = "{\n" +
                         "\"id\": \"1\",\n" +
                         "\"login\": \"doloreUpdate\",\n" +
                         "\"name\": \"est adipisicing\",\n" +
                         "\"email\": \"mail@yandex.ru\",\n" +
                         "\"birthday\": \"2020-10-20\"\n" +
                         "}";

        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(bodyPost)).andReturn();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON).content(bodyPut)).andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    @DisplayName("Обновление без email должно возвращать статус код 200")
    public void updatingUserWithoutEmailShouldReturnOkStatusCode() throws Exception {
        String bodyPost = "{\n" +
                          "\"login\": \"doloreUpdate\",\n" +
                          "\"name\": \"est adipisicing\",\n" +
                          "\"email\": \"mail@gmail.ru\",\n" +
                          "\"birthday\": \"2020-09-20\"\n" +
                          "}";
        String bodyPut = "{\n" +
                         "\"id\": \"2\",\n" +
                         "\"login\": \"doloreUpdate\",\n" +
                         "\"name\": \"est adipisicing\",\n" +
                         "\"birthday\": \"2020-10-20\"\n" +
                         "}";

        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(bodyPost)).andReturn();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON).content(bodyPut)).andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }


}
