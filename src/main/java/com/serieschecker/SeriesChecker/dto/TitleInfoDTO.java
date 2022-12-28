package com.serieschecker.SeriesChecker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class TitleInfoDTO {
    private String[] titleGenre;
    private int titleDuration;
    private String titlePG;
}
