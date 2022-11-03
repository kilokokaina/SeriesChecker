package com.serieschecker.SeriesChecker.service;

import com.serieschecker.SeriesChecker.models.TitleModel;

import java.util.List;

public interface TitleService {
    TitleModel findByName(String titleName);
    List<TitleModel> findByGenre(String titleGenre);
    void save(TitleModel titleModel);
    void delete(Long titleId);
}
