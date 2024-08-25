package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Controller
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class MpaController {
    private final MpaService mpaService;


    @GetMapping
    public List<MPA> getAll() {
        log.info("get All mpa ratings");
        return mpaService.getAll();
    }

    @GetMapping("/{id}")
    public MPA get(@PathVariable Long id) {
        log.info("get mpa rating by id = {}", id);
        return mpaService.get(id);
    }
}
