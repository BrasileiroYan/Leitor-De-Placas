package com.example.plateReader.Utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AppUserUtil {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String generateHash(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean verifyUser(String rawPassword, String passwordHash){
        return passwordEncoder.matches(rawPassword, passwordHash);
    }
}
