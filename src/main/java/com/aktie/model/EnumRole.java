package com.aktie.model;

public enum EnumRole {

    CUSTOMER("USER", "Usuário"),
    ADMIN("ADMIN", "Administrador");

    private String key;

    private String description;

    private EnumRole(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

}
