package com.serieschecker.SeriesChecker.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TitleModel {
    private String titleName;
    private String titleCountry;
    private String titlePlatform;
    private String titleGenre;
    private String titleYear;
    private String titleSeasonNumber;
    private String titleEpisodeDuration;
    private String titlePG;

//    "Год производства", "Страна",
//            "Платформа", "Жанр", "Возраст", "Время"

    public void setFields(String fieldName, String fieldValue) {
        switch (fieldName) {
            case "Год производства" -> setTitleYear(fieldValue);
            case "Страна" -> setTitleCountry(fieldValue);
            case "Платформа" -> setTitlePlatform(fieldValue);
            case "Жанр" -> setTitleGenre(fieldValue);
            case "Возраст" -> setTitlePG(fieldValue);
            case "Время" -> setTitleEpisodeDuration(fieldValue);
        }
    }

    @Override
    public String toString() {
        return String.format("""
                Title: %s
                Year: %s
                Country: %s
                Platform: %s
                Genre: %s
                PG: %s
                Duration: %s
                """, getTitleName(), getTitleYear(), getTitleCountry(), getTitlePlatform(),
                getTitleGenre(), getTitlePG(), getTitleEpisodeDuration());
    }
}
