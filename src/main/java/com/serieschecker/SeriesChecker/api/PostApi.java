package com.serieschecker.SeriesChecker.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.serieschecker.SeriesChecker.models.PostModel;
import com.serieschecker.SeriesChecker.service.impl.PostServiceImpl;
import com.serieschecker.SeriesChecker.view.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/post")
public class PostApi {
    private final PostServiceImpl postService;

    @Autowired
    public PostApi(PostServiceImpl postService) {
        this.postService = postService;
    }

    @GetMapping
    @JsonView(PostView.IdTitleAuthor.class)
    public List<PostModel> listAll() {
        return postService.findAll();
    }

    @GetMapping("{id}")
    @JsonView(PostView.FullData.class)
    public PostModel findById(@PathVariable(value = "id") PostModel postModel) {
        return postModel;
    }

    @PostMapping
    public PostModel addTitleModel(@RequestBody PostModel postModel) {
        postService.save(postModel);
        return postModel;
    }

    // Пока не реализовано. Необходимо оперделить параметры, которые можно будет изменять
    @PutMapping("{id}")
    public PostModel updateTitleModel(@PathVariable(value = "id") PostModel postFromDB,
                                       @RequestBody PostModel postModel) {
        if (postFromDB == null) return null;

        postFromDB.setPostTitle(postModel.getPostTitle());
        postFromDB.setPostDescription(postModel.getPostDescription());
        postFromDB.setPostText(postModel.getPostText());

        postService.save(postFromDB);

        return postFromDB;
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable(value = "id") Long postId) {
        postService.deletePost(postId);
    }
}
