package com.rena.application.service.settings.user;

import java.util.List;

public class RoleListHelper {
    public static List<String> getRolesAdmin() {
        return List.of(new String[]{"ROLE_Бригадир", "ROLE_Мастер/Технолог", "ROLE_Оператор"});
    }

    public static List<String> getRolesEngineer() {
        return List.of(new String[]{"ROLE_Оператор"});
    }
}
