package com.serieschecker.SeriesChecker.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
//@Entity
public class NewsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long newsId;

    private String newsTitle;
    private String newsText;
}
