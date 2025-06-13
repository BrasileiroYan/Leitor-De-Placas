package com.example.plateReader.service;

import com.example.plateReader.service.exception.EmailSendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendActivationLink(String toUsername, String token) {
        SimpleMailMessage message = new SimpleMailMessage();

        String text = """
                Olá,
                
                Sua conta no sistema da PRF foi criada com sucesso.
                
                Para ativar sua conta, clique no link abaixo:
                
                -> Link de ativação: %s
                
                [ATENÇÃO]\s
                Este link é válido por 24 horas e só pode ser utilizado uma vez.
                Após acessar o link, você poderá definir uma nova senha de acesso.

                Data e hora da criação: %s

                Se você não solicitou essa conta ou acredita que isso foi um erro, entre em contato com o administrador do sistema.

                Atenciosamente,
                Equipe de Suporte do Sistema de Leitura de Placas PRF
                
                """.formatted(frontendBaseUrl + "/activate-account?token=" + token,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        message.setFrom("suporteleitorprf@gmail.com");
        message.setTo(toUsername);
        message.setSubject("[PRF] Ativação de conta – Acesse o link para seu primeiro acesso");
        message.setText(text);

        try {
            mailSender.send(message);
            logger.info("Email de ativação enviado para: {}", toUsername);
        } catch (MailException e) {
            logger.info("Falha ao enviar email de ativação para {}: {}", toUsername, e.getMessage());
            throw new EmailSendingException("Não foi possível enviar o email de ativação para " + toUsername);
        }
    }

    public void sendPasswordResetLink(String toUsername, String token) {
        SimpleMailMessage message = new SimpleMailMessage();

        String text = """
                Olá,
                
                Recebemos uma solicitação de redefinição de senha para sua conta no sistema de Leitura de Placas da PRF.
                
                Para redefinir sua senha, clique no link abaixo:
                
                %s
                
                Este link é válido por 1 hora a partir do momento em que este e-mail foi enviado. Após este período, o link expirará e você precisará solicitar uma nova redefinição de senha.
                
                [ATENÇÃO]\s
                Se você não solicitou esta redefinição de senha, por favor, ignore este e-mail. Sua senha atual permanecerá inalterada.
                Por motivos de segurança, não compartilhe este link com ninguém.
                
                Atenciosamente,
                Equipe de Suporte do Sistema de Leitura de Placas PRF
                
                """.formatted(frontendBaseUrl + "/reset-password?token=" + token);

        message.setFrom("suporteleitorprf@gmail.com");
        message.setTo(toUsername);
        message.setSubject("[PRF] Redefinição de Senha para o Sistema de Leitura de Placas");
        message.setText(text);

        try {
            mailSender.send(message);
            logger.info("Email de redefinição de senha enviado (caso usuário exista) para: {}", toUsername);
        } catch (MailException e) {
            logger.info("Falha ao enviar email de redefinição de senha para {}: {}", toUsername, e.getMessage());

        }
    }
}
