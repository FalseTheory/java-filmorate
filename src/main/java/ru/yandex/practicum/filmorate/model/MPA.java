package ru.yandex.practicum.filmorate.model;

import lombok.*;


@Data
@EqualsAndHashCode(of = "id")
public class MPA {
    long id;
    String name;
}
