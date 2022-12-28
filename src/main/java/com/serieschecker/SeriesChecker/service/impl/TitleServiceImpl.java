package com.serieschecker.SeriesChecker.service.impl;

import com.serieschecker.SeriesChecker.dto.TitleInfoDTO;
import com.serieschecker.SeriesChecker.models.TitleModel;
import com.serieschecker.SeriesChecker.repos.TitleRepository;
import com.serieschecker.SeriesChecker.service.TitleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
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
        for (String genre : genreSet) resultList.addAll(findByGenre(genre));

        return resultList;
    }

    public List<TitleModel> chooseTitleList(TitleInfoDTO titleInfoDTO) {
        List<TitleModel> resultTitleList;

        if (titleInfoDTO.getTitleGenre() == null) resultTitleList = titleRepository.findAll();
        else resultTitleList = findAllByGenre(Set.of(titleInfoDTO.getTitleGenre()));

        if (titleInfoDTO.getTitlePG() != null) {
            int titleInfoIntPG = Integer.parseInt(titleInfoDTO.getTitlePG().replace("+", ""));
            resultTitleList.removeIf(titleModel -> {
                if (titleModel.getTitlePG() != null) {
                    int intTitlePG = Integer.parseInt(titleModel.getTitlePG().replace("+", ""));
                    return intTitlePG > titleInfoIntPG;
                } else return false;
            });
        }

        resultTitleList.removeIf(titleModel ->
                titleInfoDTO.getTitleDuration() >= titleModel.getTitleEpisodeDuration() + 10 ||
                titleInfoDTO.getTitleDuration() <= titleModel.getTitleEpisodeDuration() - 10
        );

        return resultTitleList;
    }
}
