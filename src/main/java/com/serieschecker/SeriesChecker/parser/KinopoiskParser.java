package com.serieschecker.SeriesChecker.parser;

import com.serieschecker.SeriesChecker.models.TitleModel;
import com.serieschecker.SeriesChecker.service.impl.TitleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.ConsoleHandler;

@Slf4j
@Component
public class KinopoiskParser implements CommandLineRunner {
    private static final String titleSourceUrl = "https://www.kinopoisk.ru%s";
    private static final String linkSourceUrl250 = "https://www.kinopoisk.ru/lists/movies/series-top250/?page=%d";
    private static final String linkSourceUrlPopular = "https://www.kinopoisk.ru/lists/movies/popular-series/?page=%d";

    private static final String[] requiredFields = { "Год производства", "Страна",
            "Платформа", "Жанр", "Возраст", "Время"
    };

    private final TitleServiceImpl titleService;

    @Autowired
    public KinopoiskParser(TitleServiceImpl titleService) {
        this.titleService = titleService;
    }

    private static List<String> getTitleLink(String url, int pageNumber) throws IOException, InterruptedException {
        List<String> seriesLinkList = new ArrayList<>();

        for (int i = 1; i <= pageNumber; i++) {
            String sourceUrlPage = String.format(url, i);

            try {
                Document mainHTML = Jsoup.connect(sourceUrlPage).get();
                Elements itemHref = mainHTML.getElementsByClass("base-movie-main-info_link__YwtP1");

                itemHref.forEach(item -> {
                    seriesLinkList.add(item.attr("href"));
                    log.info(item.attr("href"));
                });

            } catch (HttpStatusException ex) {
                ex.printStackTrace();
            }

            Thread.sleep(13000);
        }

        try(FileWriter fileWriter = new FileWriter("/Users/nikol/Desktop/output250.txt")) {

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            seriesLinkList.forEach(link -> {
                try {

                    bufferedWriter.write(link);
                    bufferedWriter.append("\n");

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            bufferedWriter.flush();
            bufferedWriter.close();
        }

        return seriesLinkList;
    }

    private static void parsePage(List<String> seriesLinkList, TitleServiceImpl titleService) {
        for (String link : seriesLinkList) {
            log.info(link);
            TitleModel tempTitleModel = new TitleModel();

            try {
                Document titleHTML = Jsoup.connect(String.format(titleSourceUrl, link)).get();
                String titleTV = Objects.requireNonNull(
                        titleHTML.selectFirst("span[data-tid=2da92aed]")).text();

                tempTitleModel.setTitleName(titleTV);

                Element divBody = Objects.requireNonNull(
                        titleHTML.selectFirst("div[data-test-id=encyclopedic-table]"));

                for (String field : requiredFields) {
                    Elements divField = divBody.select(String.format(":containsOwn(%s)", field));
                    Elements fieldsInfo = divField.next();

                    if (divField.size() == 0) tempTitleModel.setFields(field, null);
                    fieldsInfo.forEach(item -> {
                        if (field.equals("Год производства")) {
                            String[] itemArray = item.text().split(" ");

                            int titleYear = Integer.parseInt(itemArray[0]);
                            int titleSeason = Integer.parseInt(itemArray[1].split(" ")[0]
                                    .replace("(", ""));

                            tempTitleModel.setTitleYear(titleYear);
                            tempTitleModel.setTitleSeasonNumber(titleSeason);

                        } else if (field.equals("Время")) {
                            int titleDuration = Integer.parseInt(item.text().split(" ")[0]
                                    .replace("—", "0"));

                            tempTitleModel.setTitleEpisodeDuration(titleDuration);

                        } else tempTitleModel.setFields(field, item.text());
                    });
                }

                titleService.save(tempTitleModel);
                Thread.sleep(15000);

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run(String[] args) throws Exception {
        log.info("Run: " + getClass());
        log.info("ULR List Done...Start parsing title pages");
    }
}
