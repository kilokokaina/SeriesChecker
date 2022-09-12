package com.serieschecker.SeriesChecker.controllers;

import com.serieschecker.SeriesChecker.models.PostModel;
import com.serieschecker.SeriesChecker.models.UserModel;
import com.serieschecker.SeriesChecker.service.impl.PostServiceImpl;
import com.serieschecker.SeriesChecker.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Controller
public class MainController {
    private final UserServiceImpl userService;
    private final PostServiceImpl postService;

    @Autowired
    public MainController(UserServiceImpl userService, PostServiceImpl postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping()
    public String homePage(Model model) {
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @GetMapping("post")
    public String postPage(@RequestParam(value = "search", required = false, defaultValue = "") String searchValue,
                       Model model) {
        if (!Objects.equals(searchValue, "")) {
            log.info("Search parameter (/post): " + searchValue);

            model.addAttribute("posts", postService.searchPost(searchValue.toLowerCase()));
            model.addAttribute("header", String.format("Найдено по запросу %s", searchValue));
            model.addAttribute("title", "Поиск");

            return "post";
        }

        ArrayList<PostModel> postList = postService.findAllSorted();

        model.addAttribute("posts", postList);
        model.addAttribute("title", "Статьи");

        return "post";
    }

    @GetMapping("search")
    public RedirectView searchFunction(@RequestParam String search,
                                       RedirectAttributes redirectAttributes) {
        log.info("Search parameter (/search): " + search);
        log.info("Result string: " + String.format("redirect:/post?search=%s", search));

        redirectAttributes.addAttribute("search", search);

        return new RedirectView("post");
    }

    @GetMapping("about")
    public String aboutPage(Model model, HttpServletRequest request) {
        model.addAttribute("title", "О нас");
        return "about";
    }

    @GetMapping("registration")
    public String registryUserPage() {
        return "registration";
    }

    @PostMapping("registration")
    public String addUser(UserModel userModel, Model model) {
        if (!userService.addUser(userModel)) {
            model.addAttribute("message", "Пользователь уже зарегистрирован");
            return "registration";
        }

        return "redirect:/login";
    }
}
