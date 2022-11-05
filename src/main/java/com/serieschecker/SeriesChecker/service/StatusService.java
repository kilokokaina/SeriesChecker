package com.serieschecker.SeriesChecker.service;

import com.serieschecker.SeriesChecker.models.Status2FAModel;
import com.serieschecker.SeriesChecker.models.UserModel;

public interface StatusService {
    void save(Status2FAModel status2FAModel);
    Status2FAModel findByChatId(Long userChatId);
    void delete(Long statusId);
    boolean existByChatId(Long userChatId);
}
