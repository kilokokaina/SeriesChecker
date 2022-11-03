package com.serieschecker.SeriesChecker.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TitleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long titleId;

    @Column(columnDefinition = "integer default 0")
    private int titleYear;

    @Column(columnDefinition = "integer default 0")
    private int titleSeasonNumber;

    @Column(columnDefinition = "integer default 0")
    private int titleEpisodeDuration;

    private String titleCountry;
    private String titlePlatform;
    private String titleGenre;
    private String titleName;

    @Column(columnDefinition = "varchar(255) default '0+'")
    private String titlePG;

    public void setFields(String fieldName, String fieldValue) {
        switch (fieldName) {
            case "Год производства" -> setTitleYear(Integer.parseInt(fieldValue));
            case "Страна" -> setTitleCountry(fieldValue);
            case "Платформа" -> setTitlePlatform(fieldValue);
            case "Жанр" -> setTitleGenre(fieldValue);
            case "Возраст" -> setTitlePG(fieldValue);
            case "Время" -> setTitleEpisodeDuration(Integer.parseInt(fieldValue));
        }
    }

    @Override
    public String toString() {
        return String.format("""
                \nTitle: %s
                Year: %s
                Seasons: %d
                Country: %s
                Platform: %s
                Genre: %s
                PG: %s
                Duration: %s
                """, getTitleName(), getTitleYear(), getTitleSeasonNumber(),
                getTitleCountry(), getTitlePlatform(), getTitleGenre(), getTitlePG(),
                getTitleEpisodeDuration()
        );
    }
}
