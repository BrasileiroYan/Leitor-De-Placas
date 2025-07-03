package br.com.prf.leitordeplacas.service;

import br.com.prf.leitordeplacas.service.exception.EmailSendingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeHelper.setFrom("suporteleitorprf@gmail.com");
            mimeHelper.setTo(toUsername);
            mimeHelper.setSubject("[PRF] Ativação de conta – Acesse o link para seu primeiro acesso");

            String htmlContent = """
                    <html>
                    <body>
                        <p>Olá,</p>
                        <p>Sua conta no sistema da PRF foi criada com sucesso.</p>
                        <p>Para ativar sua conta, clique no link abaixo:</p>
                        <p><a href="%s">Ativar Minha Conta PRF</a></p>
                        <p>[ATENÇÃO]</p>
                        <p>Este link é válido por 24 horas e só pode ser utilizado uma vez.</p>
                        <p>Após acessar o link, você poderá definir uma nova senha de acesso.</p>
                        <p>Data e hora da criação: %s</p>
                        <p>Se você não solicitou essa conta ou acredita que isso foi um erro, entre em contato com o administrador do sistema.</p>
                        <p>Atenciosamente,</p>
                        <p>Equipe de Suporte do Sistema de Leitura de Placas PRF</p>
                    </body>
                    </html>
                    """.formatted(frontendBaseUrl + "/activate-account?token=" + token,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

            mimeHelper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
                throw new EmailSendingException("Não foi possível enviar o email de ativação para " + toUsername);
        }
    }

    public void sendPasswordResetLink(String toUsername, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeHelper.setFrom("suporteleitorprf@gmail.com");
            mimeHelper.setTo(toUsername);
            mimeHelper.setSubject("[PRF] Redefinição de Senha para o Sistema de Leitura de Placas");

            String htmlContent = """
                <html>
                <body>
                    <p>Olá,</p>
                    <p>Recebemos uma solicitação de redefinição de senha para sua conta no sistema de Leitura de Placas da PRF.</p>
                    <p>Para redefinir sua senha, clique no link abaixo:</p>
                    <p><a href="%s">Redefinir Minha Senha</a></p>
                    <p>Este link é válido por 1 hora a partir do momento em que este e-mail foi enviado. Após este período, o link expirará e você precisará solicitar uma nova redefinição de senha.</p>
                    <p>[ATENÇÃO]</p>
                    <p>Se você não solicitou esta redefinição de senha, por favor, ignore este e-mail. Sua senha atual permanecerá inalterada.</p>
                    <p>Por motivos de segurança, não compartilhe este link com ninguém.</p>
                    <p>Atenciosamente,</p>
                    <p>Equipe de Suporte do Sistema de Leitura de Placas PRF</p>
                </body>
                </html>
                """.formatted(frontendBaseUrl + "/reset-password?token=" + token);

            mimeHelper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.info("Falha ao enviar email de redefinição de senha para {}: {}", toUsername, e.getMessage());
        }
    }

    public void sendAccountLockedAlert(String toUsername, Long lockedTime) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeHelper.setFrom("suporteleitorprf@gmail.com");
            mimeHelper.setTo(toUsername);
            mimeHelper.setSubject("[PRF] Alerta de Segurança - Sua conta foi temporariamente bloqueada");

            String htmlContent = """
               <html>
               <body>
                   <p>Olá,</p>
                   <p>Detectamos múltiplas tentativas de login falhas em sua conta no Sistema de Leitura de Placas da PRF.</p>
                   <p>Por motivos de segurança, sua conta foi temporariamente bloqueada por %d minutos.</p>
                   <p>O que fazer agora:</p>
                   <p></p>
                   <p>- Se foi você: Por favor, aguarde %d minutos e tente fazer login novamente. Se você esqueceu sua senha, utilize a opção "Esqueci Minha Senha" na tela de login para redefini-la.</p>
                   <p></p>
                   <p>- Se não foi você: Isso pode indicar que outra pessoa está tentando acessar sua conta. Recomendamos fortemente que, assim que sua conta for desbloqueada, você acesse o sistema e troque sua senha imediatamente para uma nova e forte.</p>
                   <p></p>
                   <p>A segurança da sua conta é nossa prioridade.</p>
                   <p></p>
                   <p>Atenciosamente,</p>
                   <p></p>
                   <p>Equipe de Suporte do Sistema de Leitura de Placas PRF</p>
               </body>
               </html>
               """.formatted(lockedTime, lockedTime);

            mimeHelper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.info("Falha ao enviar email de alerta para {}: {}", toUsername, e.getMessage());
            throw new EmailSendingException("Não foi possível enviar o email de alerta de bloqueio de conta para " + toUsername);
        }
    }
}
