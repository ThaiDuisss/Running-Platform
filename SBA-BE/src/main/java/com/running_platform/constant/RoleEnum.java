package com.running_platform.constant;

public enum RoleEnum {
      USER, ADMIN;

      public RoleEnum getRoleEnum(String roleName) {
            for (RoleEnum role : RoleEnum.values()) {
                  if (role.name().equalsIgnoreCase(roleName)) {
                        return role;
                  }
            }
            throw new IllegalArgumentException("No enum constant with name " + roleName);
      }
}
