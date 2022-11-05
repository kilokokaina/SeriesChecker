package com.serieschecker.SeriesChecker.service.impl;

import com.serieschecker.SeriesChecker.models.Status2FAModel;
import com.serieschecker.SeriesChecker.repos.StatusRepository;
import com.serieschecker.SeriesChecker.service.StatusService;
import org.springframework.stereotype.Service;

@Service
public record StatusServiceImpl(StatusRepository statusRepository) implements StatusService {
    @Override
    public void save(Status2FAModel status2FAModel) {
        statusRepository.save(status2FAModel);
    }

    @Override
    public Status2FAModel findByChatId(Long userChatId) {
        return statusRepository.findByUserChatId(userChatId);
    }

    @Override
    public void delete(Long statusId) {
        statusRepository.deleteById(statusId);
    }

    @Override
    public boolean existByChatId(Long userChatId) {
        Status2FAModel status2FAModel = findByChatId(userChatId);
        return status2FAModel == null;
    }
}
