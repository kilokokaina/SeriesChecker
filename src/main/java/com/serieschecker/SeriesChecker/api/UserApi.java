package com.serieschecker.SeriesChecker.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.serieschecker.SeriesChecker.models.UserModel;
import com.serieschecker.SeriesChecker.service.impl.UserServiceImpl;
import com.serieschecker.SeriesChecker.view.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserApi {
    private final UserServiceImpl userService;

    @Autowired
    public UserApi(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    @JsonView(UserView.IdUsernamePassword.class)
    public List<UserModel> listAll() {
        return userService.findAll();
    }

    @GetMapping("{id}")
    @JsonView(UserView.FullData.class)
    public UserModel findById(@PathVariable(value = "id") UserModel userModel) {
        return userModel;
    }

    @PostMapping
    public UserModel addUserModel(@RequestBody UserModel userModel) {
        userService.addUser(userModel);
        return userModel;
    }

    @PutMapping("{id}")
    public UserModel updateUserModel(@PathVariable(value = "id") UserModel userFromDB,
                                     @RequestBody UserModel userModel) {
        if (userFromDB == null) return null;

        userFromDB.setUsername(userModel.getUsername());
        userService.save(userFromDB);

        return userModel;
    }

    @DeleteMapping("{id}")
    public void deleteUserModel(@PathVariable(value = "id") Long userId) {
        userService.userRepository().deleteById(userId);
    }
}
