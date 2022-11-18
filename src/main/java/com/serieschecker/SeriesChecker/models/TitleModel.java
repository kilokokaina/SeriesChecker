package com.serieschecker.SeriesChecker.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.serieschecker.SeriesChecker.view.TitleView;
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
    @JsonView(TitleView.IdAndName.class)
    private Long titleId;

    @JsonView(TitleView.FullData.class)
    @Column(columnDefinition = "integer default 0")
    private int titleYear;

    @JsonView(TitleView.FullData.class)
    @Column(columnDefinition = "integer default 0")
    private int titleSeasonNumber;

    @Column(columnDefinition = "integer default 0")
    private int titleEpisodeDuration;

    @JsonView(TitleView.FullData.class)
    private String titleCountry;

    private String titlePlatform;

    @JsonView(TitleView.FullData.class)
    private String titleGenre;

    @JsonView(TitleView.IdAndName.class)
    private String titleName;

    @JsonView(TitleView.FullData.class)
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
