package com.rena.application.config.security;

import java.util.List;

public record UserInfo(String name, Integer code, List<String> authorities) {
}
