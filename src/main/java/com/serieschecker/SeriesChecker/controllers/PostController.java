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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("post")
public class PostController {
    @GetMapping("settings")
    public String postSettings(Model model) {
        return "postSettings";
    }

    @GetMapping("{id}")
    public String postPage(@PathVariable(value = "id") PostModel postModel,
                           @AuthenticationPrincipal UserModel user, Model model) {
        model.addAttribute("title", postModel.getPostTitle());
        model.addAttribute("post", postModel);

        return "postPage";
    }
}
