package com.serieschecker.SeriesChecker.models;

import lombok.Data;

import javax.persistence.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Data
@Entity
public class PostModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String postTitle;
    private String postDescription;
    @Lob
    private String postText;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserModel author;

    private Date postDate = new Date();
    private int postViews;

    public PostModel() {
    }

    public PostModel(String postTitle, String postDescription, String postText, UserModel userModel) {
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.postText = postText;
        this.author = userModel;
    }

    public String getTimeAgo() {
        long daysBetween = ChronoUnit.DAYS.between(getPostDate().toInstant(), new Date().toInstant());

        if (daysBetween == 0) return "Сегодня";
        else if (daysBetween == 1) return String.format("%d день назад", daysBetween);
        else if (daysBetween < 5) return String.format("%d дня назад", daysBetween);

        return String.format("%d дней назад", daysBetween);
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "none";
    }

    @Override
    public String toString() {
        return String.format(
                "Post=[%d, %s]", getId(), getPostTitle()
        );
    }
}
