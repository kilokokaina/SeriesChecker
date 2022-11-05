package com.serieschecker.SeriesChecker.service;

import com.serieschecker.SeriesChecker.models.UserModel;

public interface UserService {
    void save(UserModel userModel);
    boolean addUser(UserModel userModel);
    UserModel findByChatId(Long chatId);
    UserModel findByUsername(String username);
}
