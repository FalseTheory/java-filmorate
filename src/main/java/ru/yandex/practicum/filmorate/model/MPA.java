package ru.yandex.practicum.filmorate.model;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class MPA {
    private final long id;
    private final String name;
}
