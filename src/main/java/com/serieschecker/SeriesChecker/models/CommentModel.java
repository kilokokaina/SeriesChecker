package com.serieschecker.SeriesChecker.models;

import lombok.Data;

import javax.persistence.*;

@Data
//@Entity
public class CommentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String userName;
    private String commentText;
}
