package com.serieschecker.SeriesChecker.controllers;

import com.serieschecker.SeriesChecker.models.PostModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("post")
public class PostController {
    @GetMapping("{id}")
    public String postPage(@PathVariable(value = "id") PostModel postModel, Model model) {
        model.addAttribute("title", postModel.getPostTitle());
        model.addAttribute("post", postModel);

        return "postPage";
    }
}
