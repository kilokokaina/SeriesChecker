package com.serieschecker.SeriesChecker.parser;

import com.serieschecker.SeriesChecker.models.UserModel;
import com.serieschecker.SeriesChecker.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class KinopoiskParser implements CommandLineRunner {
    private static final String sourceUrl = "/Users/nikol/Desktop/Kinopoisk/%s.html";

    private final UserServiceImpl userService;

    @Autowired
    public KinopoiskParser(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public void run(String[] args) throws Exception {
        log.info("Run: " + getClass());

        List<UserModel> userAdminList = userService.findByRole("admin");
        userAdminList.forEach(item -> log.info(item.toString()));

        Document mainHTML = Jsoup.parse(new File(String.format(sourceUrl, "main")));
        Elements seriesLink = mainHTML.getElementsByClass("base-movie-main-info_link__YwtP1");

        int i = 0;
        for (Element link : seriesLink) {
            if (i == 3) break;

            String[] linkArray = link.attr("href").split("/");
            String titlePage = linkArray[linkArray.length - 1];

            try {
                log.info("---------NEW PAGE---------");

                Document titleHTML = Jsoup.parse(new File(String.format(sourceUrl, titlePage)));
                log.info(Objects.requireNonNull(
                        titleHTML.selectFirst("span[data-tid=2da92aed]")).text());

                Elements titleInfoDark = titleHTML.getElementsByClass(
                        "styles_linkDark__7m929 styles_link__3QfAk");
                Elements titleInfoLight = titleHTML.getElementsByClass(
                        "styles_linkLight__cha3C styles_link__3QfAk");

                titleInfoDark.forEach(info -> log.info(info.text()));
                titleInfoLight.forEach(info -> log.info(info.text()));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            i++;
        }
    }
}
