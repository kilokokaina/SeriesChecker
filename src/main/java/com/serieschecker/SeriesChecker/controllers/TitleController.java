package com.serieschecker.SeriesChecker.controllers;

import com.serieschecker.SeriesChecker.dto.TitleInfoDTO;
import com.serieschecker.SeriesChecker.models.TitleModel;
import com.serieschecker.SeriesChecker.models.UserModel;
import com.serieschecker.SeriesChecker.service.impl.TitleServiceImpl;
import com.serieschecker.SeriesChecker.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("title")
public class TitleController {
    private final UserServiceImpl userService;
    private final TitleServiceImpl titleService;

    @Autowired
    public TitleController(TitleServiceImpl titleService, UserServiceImpl userService) {
        this.titleService = titleService;
        this.userService = userService;
    }

    @GetMapping
    public String titleMain(@PageableDefault(sort = {"title_id"}, size = 9,
                                    direction = Sort.Direction.ASC) Pageable pageable, Model model,
                            @ModelAttribute("chooseTitle") ArrayList<TitleModel> redirectList) {
        model.addAttribute("page",
                titleService.titleRepository.findAllPageable(pageable)
        );

        return "title";
    }

    @GetMapping("choose_title")
    public String chooseTitlePage(@ModelAttribute("chooseTitle") ArrayList<TitleModel> redirectList, Model model) {
        if (redirectList.isEmpty()) return "redirect:/title";
        model.addAttribute("title_list", redirectList);

        return "chosenTitle";
    }

    @PostMapping("choose_title")
    public ModelAndView chooseTitle(String[] titleGenre, String titlePG, int titleDuration,
                                    RedirectAttributes redirectAttributes) {
        TitleInfoDTO titleInfoDTO = new TitleInfoDTO(titleGenre, titleDuration, titlePG);

        redirectAttributes.addFlashAttribute("chooseTitle",
                titleService.chooseTitleList(titleInfoDTO)
        );

        return new ModelAndView("redirect:/title/choose_title");
    }

    @GetMapping("{id}")
    public String titlePage(@PathVariable(value = "id")TitleModel titleModel, Model model,
                            Authentication authentication) {
        if (authentication != null) {
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

            String[] favorGenreArray = new String[5];
            int i = 0;
            if (genreCountMapSorted.size() >= 5) {
                for (String genre: genreCountMapSorted.keySet()) {
                    favorGenreArray[i] = genre;
                    i++;
                    if (i == 5) break;
                }
            } else {
                favorGenreArray = new String[genreCountMapSorted.keySet().size()];
                for (String genre: genreCountMapSorted.keySet()) {
                    favorGenreArray[i] = genre;
                    i++;
                }
            }

            model.addAttribute("rec_title",
                    titleService.findAllByGenre(Set.of(favorGenreArray)).subList(0, 3));

            log.info("here");
        }
        model.addAttribute("title_model", titleModel);
        return "titlePage";
    }

    @PostMapping("set_to_favorite")
    public @ResponseBody int setToFavorite(@RequestBody String titleName,
                                              Authentication authentication) {
        UserModel userModel = userService.findByUsername(authentication.getName());
        TitleModel titleModel = titleService.findByName(titleName);

        if (!userModel.getLikedTitle().contains(titleModel)) {
            userModel.getLikedTitle().add(titleModel);
            userService.save(userModel);
            return 1;
        } else return 0;
    }
}
