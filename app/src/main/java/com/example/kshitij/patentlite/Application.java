package com.example.kshitij.patentlite;

public class Application {
    private String name;
    private int claims;

    public Application(String name, int claims) {
        this.name = name;
        this.claims = claims;
    }

    public String getName() {
        return name;
    }

    public int getClaims() {
        return claims;
    }
}
