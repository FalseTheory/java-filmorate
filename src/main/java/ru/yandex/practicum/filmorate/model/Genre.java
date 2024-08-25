package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@EqualsAndHashCode(of = "id")
public class Genre {
    long id;
    String name;
}
