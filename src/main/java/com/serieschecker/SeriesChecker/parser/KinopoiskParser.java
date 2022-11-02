package com.serieschecker.SeriesChecker.parser;

import com.serieschecker.SeriesChecker.models.TitleModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class KinopoiskParser implements CommandLineRunner {
    private static final String titleSourceUrl = "https://kinopoisk/";
    private static final String linkSourceUrl = "/Users/nikol/Desktop/Kinopoisk/%s.html";

    private static final String[] requiredFields = { "Год производства", "Страна",
            "Платформа", "Жанр", "Возраст", "Время"
    };

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

        Document mainHTML = Jsoup.parse(new File(String.format(linkSourceUrl, "main")));
        Elements seriesLink = mainHTML.getElementsByClass("base-movie-main-info_link__YwtP1");

        List<TitleModel> titleModelList = new ArrayList<>();

        int i = 0;
        for (Element link : seriesLink) {
            if (i == 4) break;

            TitleModel tempTitleModel = new TitleModel();

            String[] linkArray = link.attr("href").split("/");
            String titlePage = linkArray[linkArray.length - 1];

            try {
                log.info("---------NEW PAGE---------");

                Document titleHTML = Jsoup.parse(new File(String.format(linkSourceUrl, titlePage)));
                String titleTV = Objects.requireNonNull(
                        titleHTML.selectFirst("span[data-tid=2da92aed]")).text();

                tempTitleModel.setTitleName(titleTV);

                Element divBody = Objects.requireNonNull(
                        titleHTML.selectFirst("div[data-test-id=encyclopedic-table]"));

                for (String field : requiredFields) {
                    Elements divField = divBody.select(String.format(":containsOwn(%s)", field));
                    Elements fieldsInfo = divField.next();

                    if (divField.size() == 0) tempTitleModel.setFields(field, null);
                    fieldsInfo.forEach(item ->
                            tempTitleModel.setFields(field, item.text())
                    );
                }

                titleModelList.add(tempTitleModel);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            i++;
        }

        titleModelList.forEach(item -> log.info(item.toString()));
    }
}
