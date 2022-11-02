package com.serieschecker.SeriesChecker.service.impl;

import com.serieschecker.SeriesChecker.models.Role;
import com.serieschecker.SeriesChecker.models.UserModel;
import com.serieschecker.SeriesChecker.repos.UserRepository;
import com.serieschecker.SeriesChecker.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public record UserServiceImpl(UserRepository userRepository) implements UserDetailsService, UserService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Override
    public void save(UserModel userModel) {
        userRepository.save(userModel);
    }

    @Override
    public boolean addUser(UserModel userModel) {
        UserModel userFromDB = userRepository.findByUsername(userModel.getUsername());

        if (userFromDB != null) return false;

        userModel.setActive(true);
        userModel.setRoleSet(Collections.singleton(Role.USER));
        userRepository.save(userModel);

        return true;
    }

    public List<UserModel> findByRole(String userRole) {
        return userRepository.findByRole(userRole);
    }
}
