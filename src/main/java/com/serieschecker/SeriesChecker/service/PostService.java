package com.serieschecker.SeriesChecker.service;

import com.serieschecker.SeriesChecker.models.PostModel;
import com.serieschecker.SeriesChecker.models.UserModel;

import java.util.ArrayList;

public interface PostService {
    PostModel getByTitle(String postTitle);
    ArrayList<PostModel> findAll();
    ArrayList<PostModel> findAllSorted();
    void save(PostModel postModel);
    ArrayList<PostModel> searchPost(String searchString);
    ArrayList<PostModel> getPost(long innerPostId);
    boolean addPost(PostModel postModel, UserModel userModel);
    void editPost(long postId, String postTitle, String postDescription, String postText);
    void deletePost(long postId);
    boolean existsById(long id);
}
