package com.mygolfclub.utils.roles;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleTypes {
    EMPLOYEE("EMPLOYEE"),
    MODERATOR("MODERATOR"),
    ADMIN("ADMIN");

    private final String value;
}
