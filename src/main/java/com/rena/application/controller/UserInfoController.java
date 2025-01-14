package com.rena.application.controller;

import com.rena.application.config.security.UserInfo;
import com.rena.application.config.security.UserInfoService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;

@BrowserCallable
@RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoService userInfoService;

    @AnonymousAllowed
    @Nonnull
    public UserInfo getUserInfo() {
        return userInfoService.getUserInfo();
    }
}
