package com.serieschecker.SeriesChecker.controllers;

import com.serieschecker.SeriesChecker.models.PostModel;
import com.serieschecker.SeriesChecker.models.UserModel;
import com.serieschecker.SeriesChecker.service.impl.PostServiceImpl;
import com.serieschecker.SeriesChecker.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("user")
public class UserController {
    private final PostServiceImpl postService;
    private final UserServiceImpl userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(PostServiceImpl postService, UserServiceImpl userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping()
    public String userPage(@AuthenticationPrincipal UserModel userModel, Model model) {
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
    public String addPostMethod(@AuthenticationPrincipal UserModel userModel,
                                PostModel postModel) {
        if (!postService.addPost(postModel, userModel)) log.info("Item already exists");
        return "redirect:/user";
    }

    @GetMapping("/posts/{id}/edit")
    public String postEdit(@AuthenticationPrincipal UserModel userModel,
                           @PathVariable(value = "id") PostModel postModel, Model model) {
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
    public String postDelete(@AuthenticationPrincipal UserModel userModel,
                             @PathVariable(value = "id") long postId) {
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
}
