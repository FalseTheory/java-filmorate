package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.film.MpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseMpaService implements MpaService {

    private final MpaRepository mpaRepository;

    @Override
    public List<Mpa> getAll() {
        return mpaRepository.getAll();
    }

    @Override
    public Mpa get(long id) {
        return mpaRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("MPA with id - " + id + " not found."));
    }
}
