package com.serieschecker.SeriesChecker.service.impl;

import com.serieschecker.SeriesChecker.bot.AuthTelegramBot;
import com.serieschecker.SeriesChecker.dto.CredentialsDTO;
import com.serieschecker.SeriesChecker.models.Status2FAModel;
import com.serieschecker.SeriesChecker.models.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {
    private final UserServiceImpl userService;
    private final AuthTelegramBot authTelegramBot;
    private final HttpServletRequest httpRequest;
    private final StatusServiceImpl statusService;

    @Autowired
    public AuthenticationProviderImpl(UserServiceImpl userService, AuthTelegramBot authTelegramBot,
                                     HttpServletRequest httpRequest, StatusServiceImpl statusService) {
        this.authTelegramBot = authTelegramBot;
        this.statusService = statusService;
        this.userService = userService;
        this.httpRequest = httpRequest;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String enteredUsername = authentication.getName();
        String enteredPassword = (String) authentication.getCredentials();

        UserDetails userDetails = userService.loadUserByUsername(enteredUsername);

        if (userDetails == null) {
            log.error("User not found: " + enteredUsername);
            return null;
        }

        if (!enteredPassword.equals(userDetails.getPassword())) {
            log.error("Incorrect password! Username: " + enteredUsername);
            return null;
        }

        UserModel userModel = userService.findByUsername(enteredUsername);

        if (userModel.isStatus2FA() && userModel.getTelegramBotChatId() != null) {
            SendMessage sendMessage = new SendMessage();

            sendMessage.setChatId(userModel.getTelegramBotChatId());
            sendMessage.setText(String.format("""
                Попытка входа
                Пользователь: %s
                IP-адрес: %s\040
                """, enteredUsername, httpRequest.getRemoteAddr()
            ));

            authTelegramBot.setMessage(sendMessage);

            try {
                authTelegramBot.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            while (true) {
                Status2FAModel status2FAModel = statusService.findByChatId(userModel.getTelegramBotChatId());
                if (status2FAModel != null) {
                    switch (status2FAModel.getUserAnswer2FA()) {
                        case "true" -> {
                            LocalDateTime dateTime = LocalDateTime.now();

                            userModel.setLoginIp(httpRequest.getRemoteAddr());
                            userModel.setLoginDate(dateTime);

                            userService.save(userModel);

                            statusService.delete(status2FAModel.getStatusId());

                            return new UsernamePasswordAuthenticationToken(
                                    enteredUsername,
                                    enteredPassword,
                                    userDetails.getAuthorities()
                            );
                        }
                        case "false" -> {
                            statusService.delete(status2FAModel.getStatusId());
                            return null;
                        }
                    }
                }
            }
        }

        LocalDateTime dateTime = LocalDateTime.now();

        userModel.setLoginIp(httpRequest.getRemoteAddr());
        userModel.setLoginDate(dateTime);

        userService.save(userModel);

        return new UsernamePasswordAuthenticationToken(
                enteredUsername,
                enteredPassword,
                userDetails.getAuthorities()
        );
    }

    public String startSession(CredentialsDTO credentialsDTO) {
        String username = credentialsDTO.getUsername();
        String password = credentialsDTO.getPassword();

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        authentication = authenticate((authentication));

        if (authentication == null) return "false";

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authentication success: " + username);

        return "true";
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
