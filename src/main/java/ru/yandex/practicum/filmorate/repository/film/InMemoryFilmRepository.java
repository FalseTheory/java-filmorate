package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmRepository implements FilmRepository {

    private Long idCount = 0L;

    Map<Long, Film> films = new HashMap<>();
    Map<Long, Set<Long>> filmLikes = new HashMap<>();

    @Override
    public Film save(Film film) {
        film.setId(++idCount);
        films.put(film.getId(), film);
        filmLikes.put(film.getId(), new HashSet<>());
        return film;
    }

    @Override
    public void update(Film film) {
        Film oldFilm = films.get(film.getId());

        oldFilm.setName(film.getName());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setDuration(film.getDuration());
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Optional<Film> get(long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public void addUserLike(long filmId, long userId) {
        filmLikes.get(filmId).add(userId);
    }

    @Override
    public void deleteUserLike(long filmId, long userId) {
        filmLikes.get(filmId).remove(userId);
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        return films.entrySet().stream()
                .sorted((entry1, entry2) -> {
                    int likesCount1 = filmLikes.get(entry1.getKey()).size();
                    int likesCount2 = filmLikes.get(entry2.getKey()).size();
                    return Integer.compare(likesCount2, likesCount1);

                })
                .map(Map.Entry::getValue)
                .limit(count)
                .collect(Collectors.toList());
    }
}
