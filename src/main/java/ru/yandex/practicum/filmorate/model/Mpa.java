package ru.yandex.practicum.filmorate.model;

import lombok.*;


@Data
@EqualsAndHashCode(of = "id")
public class Mpa {
    long id;
    String name;
}
