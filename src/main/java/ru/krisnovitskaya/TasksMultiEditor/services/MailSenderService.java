package ru.krisnovitskaya.TasksMultiEditor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender javaMailSender;

    @Value("spring.mail.username")
    private String username;
    @Value("spring.mail.host")
    private String host;

    public void sendMail(String to, String subject, String text){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(username.concat(host.replace("smtp.", "@")));
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(text);
        javaMailSender.send(mail);
    }
}
