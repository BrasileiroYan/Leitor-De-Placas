package com.example.plateReader.Utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "test";
    private static final long EXPIRATION_TIME = 86400000;
}
