package br.com.prf.leitordeplacas.Utils;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordGenerator {

    public static String generateStrongPassword(int length) {

        // Tipos disponiveis de caractere
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String symbols = "@$!%*?&._-";

        String all = upper + lower + digits + symbols;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Garante que haja pelo menos um caractere de cada tipo
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(symbols.charAt(random.nextInt(symbols.length())));

        for(int i = 4; i < length; i++) {
            password.append(all.charAt(random.nextInt(all.length())));
        }

        // Embaralha a senha
        List<Character> characters = password
                .chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(characters, random);

        // Junta os caracteres embaralhados em uma String
        String randomGeneratedPassword = characters.stream().map(String::valueOf).collect(Collectors.joining());

        return randomGeneratedPassword;
    }
}
