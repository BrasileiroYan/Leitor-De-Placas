package com.example.plateReader.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendActivationLink(String toUsername, String subject, String token) {
        SimpleMailMessage message = new SimpleMailMessage();

        String text = """
            Olá,
        
            Sua conta no sistema da PRF foi criada com sucesso.
        
            Para ativar sua conta, clique no link abaixo:
        
            -> Link de ativação: %s
        
            [ATENÇÃO] 
            Este link é válido por 24 horas e só pode ser utilizado uma vez.
            Após acessar o link, você poderá definir uma nova senha de acesso.
        
            Data e hora da criação: %s
        
            Se você não solicitou essa conta ou acredita que isso foi um erro, entre em contato com o administrador do sistema.
        
            Atenciosamente,
            Equipe de Suporte
        """.formatted(
                        "https://sua-app.com/activate?token=" + token,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));


        message.setFrom("suporteleitorprf@gmail.com");
        message.setTo(toUsername);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
