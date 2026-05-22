package com.bugtracker.server;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashTest {
    public static void main(String[] args) throws Exception {
        String password = "admin123";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        System.out.println("Правильный хеш для 'admin123': " + hexString.toString());
    }
}