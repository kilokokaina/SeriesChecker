package com.serieschecker.SeriesChecker.controllers;

import com.serieschecker.SeriesChecker.models.Role;
import com.serieschecker.SeriesChecker.models.UserModel;
import com.serieschecker.SeriesChecker.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final UserServiceImpl userService;

    @Autowired
    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("title", "Админ панель");
        model.addAttribute("userList", userService.userRepository().findAll());
        return "admin";
    }

    @GetMapping("user/{id}/edit")
    public String userEdit(@PathVariable(value = "id")UserModel userModel, Model model) {
        model.addAttribute("user", userModel);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping("user/{id}/edit")
    public String userEditPost(@PathVariable(value = "id") UserModel userModel,
                               @RequestParam String username,
                               @RequestParam Map<String, String> form) {
        userModel.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        userModel.getRoleSet().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) userModel.getRoleSet().add(Role.valueOf(key));
        }

        userService.save(userModel);

        return "redirect:/admin";
    }
}
