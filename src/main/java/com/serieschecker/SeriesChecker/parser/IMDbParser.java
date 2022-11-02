package com.serieschecker.SeriesChecker.parser;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.springframework.boot.CommandLineRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
//@Component
public class IMDbParser implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("Run: " + getClass());

        List<String> seriesLinkList = new ArrayList<>();
        String sourceUrl = "https://www.kinopoisk.ru/lists/movies/popular-series/?page=%d";

        for (int i = 1; i <= 20; i++) {
            String sourceUrlPage = String.format(sourceUrl, i);

            try {
                Document mainHTML = Jsoup.connect(sourceUrlPage).get();
                Elements itemHref = mainHTML.getElementsByClass("base-movie-main-info_link__YwtP1");
                itemHref.forEach(item -> {
                    seriesLinkList.add(item.attr("href"));
                    System.out.println(item.attr("href"));
                });
            } catch (HttpStatusException ex) {
                ex.printStackTrace();
            }

            Thread.sleep(13000);
        }

        try (FileWriter fileWriter = new FileWriter(
                "/Users/nikol/Desktop/outputTest.txt", true)) {
            for (String link : seriesLinkList) {
                fileWriter.write(link);
                fileWriter.append("\n");
            }

            fileWriter.flush();
        } catch (IOException ignored) {}
    }
}
