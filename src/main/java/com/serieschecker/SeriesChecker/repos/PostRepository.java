package com.serieschecker.SeriesChecker.repos;

import com.serieschecker.SeriesChecker.models.PostModel;
import com.serieschecker.SeriesChecker.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface PostRepository extends JpaRepository<PostModel, Long> {
    @Query(value = "SELECT * FROM post_model ORDER BY post_title", nativeQuery = true)
    Iterable<PostModel> findSortedPost();

    @Query(value = "SELECT * FROM post_model ORDER BY post_date DESC", nativeQuery = true)
    ArrayList<PostModel> dateSortedPost();

    PostModel findByPostTitle(String postTitle);

    ArrayList<PostModel> findByAuthor(UserModel userModel);
}