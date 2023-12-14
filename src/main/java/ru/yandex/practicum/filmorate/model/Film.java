package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class Film {

    private Integer id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private Integer duration;
    @NotNull
    private MpaRate mpa;
    private List<Genre> genres;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("FILM_NAME", name);
        values.put("DESCRIPTION", description);
        values.put("RELEASE_DATE", releaseDate);
        values.put("DURATION", duration);
        values.put("MPA_ID", mpa.getId());
        return values;
    }
}
