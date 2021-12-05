package io.monitoring.model.dict;

import java.util.Arrays;
import java.util.Optional;

public enum RoleType {
    ROLE_ADMIN,
    ROLE_USER;

    public static Optional<RoleType> fromString(String roleName) {
        return Arrays.stream(RoleType.values()).filter(roleType -> roleType.name().equals(roleName)).findFirst();
    }
}
