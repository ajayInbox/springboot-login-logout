package com.started.entity;

public enum Role {

    ADMIN("1"),
    REGULAR("2");

    private String code;

    private Role(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
