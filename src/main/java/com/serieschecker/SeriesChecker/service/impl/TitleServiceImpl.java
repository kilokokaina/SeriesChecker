package com.serieschecker.SeriesChecker.service.impl;

import com.serieschecker.SeriesChecker.dto.TitleInfoDTO;
import com.serieschecker.SeriesChecker.models.TitleModel;
import com.serieschecker.SeriesChecker.models.UserModel;
import com.serieschecker.SeriesChecker.repos.TitleRepository;
import com.serieschecker.SeriesChecker.service.TitleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TitleServiceImpl implements TitleService {
    public final TitleRepository titleRepository;
    public final UserServiceImpl userService;

    @Autowired
    public TitleServiceImpl(TitleRepository titleRepository, UserServiceImpl userService) {
        this.titleRepository = titleRepository;
        this.userService = userService;
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

    public Set<String> getRecList(Authentication authentication) {
        UserModel userModel = userService.findByUsername(authentication.getName());

        Set<TitleModel> favorTitleSet = userModel.getLikedTitle();
        Set<String> genreSet = new HashSet<>();

        favorTitleSet.forEach(title -> {
            String[] titleGenre = title.getTitleGenre().split(", ");
            genreSet.addAll(List.of(titleGenre));
        });

        int genreCount = 0;
        HashMap<String, Integer> genreCountMap = new HashMap<>();
        for (String genre: genreSet) {
            for (TitleModel title: favorTitleSet) {
                if (title.getTitleGenre().contains(genre)) {
                    genreCount++;
                }
            }
            genreCountMap.put(genre, genreCount);
        }

        HashMap<String, Integer> genreCountMapSorted = genreCountMap.entrySet()
                .stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        List<String> favorGenreArray = new ArrayList<>(5);
        int i = 0;
        if (genreCountMapSorted.size() >= 5) {
            for (String genre: genreCountMapSorted.keySet()) {
                favorGenreArray.set(i, genre);
                i++;
                if (i == 5) break;
            }
        } else {
            favorGenreArray = new ArrayList<>(genreCountMapSorted.size());
            for (String genre: genreCountMapSorted.keySet()) {
                favorGenreArray.set(i, genre);
                i++;
            }
        }
        
        return new HashSet<>(favorGenreArray);
    }
}
