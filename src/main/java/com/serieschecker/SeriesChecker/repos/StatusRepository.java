package com.serieschecker.SeriesChecker.repos;

import com.serieschecker.SeriesChecker.models.Status2FAModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status2FAModel, Long> {
    Status2FAModel findByUserChatId(Long userCharId);
}
