package com.serieschecker.SeriesChecker.controllers;

import com.serieschecker.SeriesChecker.models.PostModel;
import com.serieschecker.SeriesChecker.models.TitleModel;
import com.serieschecker.SeriesChecker.models.UserModel;
import com.serieschecker.SeriesChecker.service.impl.PostServiceImpl;
import com.serieschecker.SeriesChecker.service.impl.TitleServiceImpl;
import com.serieschecker.SeriesChecker.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
@RequestMapping("user")
public class UserController {
    private final PostServiceImpl postService;
    private final UserServiceImpl userService;
    private final TitleServiceImpl titleService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(PostServiceImpl postService, UserServiceImpl userService,
                          TitleServiceImpl titleService) {
        this.titleService = titleService;
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping()
    public String userPage(Authentication authentication, Model model) {
        UserModel userModel = userService.findByUsername(authentication.getName());
        ArrayList<PostModel> postList = postService.postRepository().findByAuthor(userModel);

        model.addAttribute("posts", postList);
        model.addAttribute("title", "Личный кабинет");

        return "user";
    }

    @GetMapping("settings")
    public String userSettings() {
        return "userSettings";
    }

    @GetMapping("addpost")
    public String addPost(Model model) {
        model.addAttribute("title", "Добавление записи");
        return "addPost";
    }

    @PostMapping("addpost")
    public String addPostMethod(Authentication authentication,
                                PostModel postModel) {
        UserModel userModel = userService.findByUsername(authentication.getName());
        if (!postService.addPost(postModel, userModel)) log.info("Item already exists");
        return "redirect:/user";
    }

    @GetMapping("/posts/{id}/edit")
    public String postEdit(Authentication authentication,
                           @PathVariable(value = "id") PostModel postModel, Model model) {
        UserModel userModel = userService.findByUsername(authentication.getName());
        if (!Objects.equals(userModel.getUsername(), postModel.getAuthorName()))
            return "redirect:/user";
        model.addAttribute("post", postModel);

        return "postEdit";
    }

    @PostMapping("/posts/{id}/edit")
    public String postEditSave(@PathVariable(value = "id") long postId,
                               @RequestParam String postTitle,
                               @RequestParam String postDescription,
                               @RequestParam String postText) {
        postService.editPost(postId, postTitle, postDescription, postText);
        return "redirect:/user";
    }

    @GetMapping("/posts/{id}/delete")
    public String postDelete(Authentication authentication,
                             @PathVariable(value = "id") long postId) {
        UserModel userModel = userService.findByUsername(authentication.getName());
        if (postService.existsById(postId)) return "redirect:/user";
        if (!Objects.equals(userModel.getUsername(), postService.getPost(postId).get(0).getAuthorName()))
            return "redirect:/user";

        postService.deletePost(postId);

        return "redirect:/user";
    }

    @GetMapping("test/{role}")
    public @ResponseBody List<Object> getUserByRole(@PathVariable(value = "role") String userRole) {
        class OutputDetails {
            public final String username;
            public final String password;

            public OutputDetails(String username, String password) {
                this.username = username;
                this.password = password;
            }
        }

        List<Object> testList = new ArrayList<>();
        List<UserModel> userList = userService.findByRole(userRole);

        userList.forEach(item -> {
            OutputDetails tempUser = new OutputDetails(item.getUsername(), item.getPassword());
            testList.add(tempUser);
        });

        return testList;
    }

    @GetMapping("test")
    public @ResponseBody List<TitleModel> getAllTitleByGenre(
            @RequestParam(value = "genre") String[] genre) {
        Set<String> genreSet = Set.of(genre);
        return titleService.findAllByGenre(genreSet);
    }

    @GetMapping("test/one")
    public @ResponseBody List<TitleModel> getTitleByGenre(
            @RequestParam(value = "genre") String genre) {
        return titleService.findByGenre(genre);
    }
}
