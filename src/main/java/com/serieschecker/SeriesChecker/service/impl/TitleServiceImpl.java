package com.serieschecker.SeriesChecker.service.impl;

import com.serieschecker.SeriesChecker.models.TitleModel;
import com.serieschecker.SeriesChecker.repos.TitleRepository;
import com.serieschecker.SeriesChecker.service.TitleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TitleServiceImpl implements TitleService {
    public final TitleRepository titleRepository;

    @Autowired
    public TitleServiceImpl(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    @Override
    public TitleModel findByName(String titleName) {
        return titleRepository.findByTitleName(titleName);
    }

    @Override
    public void save(TitleModel titleModel) {
        titleRepository.save(titleModel);
    }

    @Override
    public void delete(Long titleId) {
        titleRepository.deleteById(titleId);
    }

    @Override
    public List<TitleModel> findByGenre(String titleGenre) {
        return titleRepository.findByGenre(titleGenre);
    }

    public List<TitleModel> findAllByGenre(Set<String> genreSet) {
        List<TitleModel> resultList = new ArrayList<>();

        for (String genre : genreSet) {
            resultList.addAll(findByGenre(genre));
        }

        return resultList;
    }
}
