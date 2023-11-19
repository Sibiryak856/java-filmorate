package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    private final Map<Integer, Set<Integer>> filmLikes = new HashMap<>();


    @Override
    public Map<Integer, Film> getAll() {
        return films;
    }

    @Override
    public Film getFilm(Optional<Integer> id) {
        if (id.isEmpty()) {
            throw new ValidateException("Не указан id фильма");
        }
        return films.get(id.get());
    }

    @Override
    public Film create(Film film) {
        film.setId(++filmId);
        films.put(film.getId(), film);
        filmLikes.put(film.getId(), new HashSet<>());
        return film;
    }

    @Override
    public void update(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        Set<Integer> likes = filmLikes.get(filmId);
        likes.add(userId);
        filmLikes.put(filmId, likes);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        Set<Integer> likes = filmLikes.get(filmId);
        likes.remove(userId);
        filmLikes.put(filmId, likes);
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        List<Film> topFilms = new ArrayList<>(films.values());
        return topFilms.stream()
                .sorted(new TopFilmComparator())
                .limit(count)
                .collect(Collectors.toList());
    }

    private class TopFilmComparator implements Comparator<Film> {
        @Override
        public int compare(Film f1, Film f2) {
            if (filmLikes.get(f1.getId()).size() > filmLikes.get(f2.getId()).size()) {
                return -1;
            } else if (filmLikes.get(f1.getId()).size() < filmLikes.get(f2.getId()).size()) {
                return 1;
            }
            return 0;
        }
    }

    @Override
    public Set<Integer> getFilmLikes(Integer id) {
        return filmLikes.get(id);
    }
}
