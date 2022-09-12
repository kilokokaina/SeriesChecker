package com.serieschecker.SeriesChecker.repos;

import com.serieschecker.SeriesChecker.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String userName);
}
