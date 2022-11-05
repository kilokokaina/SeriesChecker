package com.serieschecker.SeriesChecker.repos;

import com.serieschecker.SeriesChecker.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String userName);
    @Query(value = "SELECT * FROM user_model AS usr, user_role AS rl " +
            "WHERE usr.user_id = rl.user_id AND rl.role_set = :role", nativeQuery = true)
    List<UserModel> findByRole(@Param("role") String userRole);
    UserModel findByTelegramBotChatId(Long chatId);

}
