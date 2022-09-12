package com.serieschecker.SeriesChecker.service.impl;

import com.serieschecker.SeriesChecker.models.PostModel;
import com.serieschecker.SeriesChecker.models.UserModel;
import com.serieschecker.SeriesChecker.repos.PostRepository;
import com.serieschecker.SeriesChecker.service.PostService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public record PostServiceImpl(PostRepository postRepository) implements PostService {
    @Override
    public PostModel getByTitle(String postTitle) {
        return postRepository.findByPostTitle(postTitle);
    }

    @Override
    public ArrayList<PostModel> findAll() {
        return (ArrayList<PostModel>) postRepository.findAll();
    }

    @Override
    public ArrayList<PostModel> findAllSorted() { return postRepository.dateSortedPost(); }

    @Override
    public void save(PostModel postModel) {
        postRepository.save(postModel);
    }

    @Override
    public ArrayList<PostModel> searchPost(String searchString) {
        List<PostModel> searchList = postRepository.findAll();
        ArrayList<PostModel> outputPost = new ArrayList<>();

        for (PostModel post : searchList) {
            if (post.getPostTitle().toLowerCase().contains(searchString)) {
                outputPost.add(post);
            }
        }

        return outputPost;
    }

    @Override
    public ArrayList<PostModel> getPost(long innerPostId) {
        Optional<PostModel> postModel = postRepository.findById(innerPostId);

        ArrayList<PostModel> postFields = new ArrayList<>();
        postModel.ifPresent(postFields::add);

        return postFields;
    }

    @Override
    public boolean addPost(PostModel postModel, UserModel userModel) {
        if (postRepository.findByPostTitle(postModel.getPostTitle()) == null) {
            postModel.setAuthor(userModel);
            postRepository.save(postModel);

            return true;
        }
        return false;
    }

    @Override
    public void editPost(long postId, String postTitle,
                            String postDescription, String postText) {
        PostModel postModel = postRepository.findById(postId).orElseThrow();
        postModel.setPostTitle(postTitle);
        postModel.setPostDescription(postDescription);
        postModel.setPostText(postText);
        
        postRepository.save(postModel);
    }

    @Override
    public void deletePost(long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public boolean existsById(long id) {
        return !postRepository.existsById(id);
    }
}
