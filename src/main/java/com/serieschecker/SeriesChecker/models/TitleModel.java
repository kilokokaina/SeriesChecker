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
    private int titleYear;
    private int titleSeasonNumber;
    private int titleEpisodeDuration;
    private String titlePG;

    @Override
    public String toString() {
        return String.format("""
                Title: %s
                Year: %d
                Country: %s
                """, getTitleName(), getTitleYear(), getTitleCountry());
    }
}
