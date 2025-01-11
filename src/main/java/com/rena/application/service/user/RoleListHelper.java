package com.rena.application.service.user;

import java.util.List;

public class RoleListHelper {
    public static List<String> getRolesAdmin() {
        return List.of(new String[]{"ROLE_Инженер МОЕ", "ROLE_Инженер TEF", "ROLE_Оператор"});
    }

    public static List<String> getRolesEngineer() {
        return List.of(new String[]{"ROLE_Оператор"});
    }
}
