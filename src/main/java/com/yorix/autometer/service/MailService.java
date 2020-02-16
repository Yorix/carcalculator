package com.yorix.autometer.service;

import com.yorix.autometer.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MailService {
    private final JavaMailSender sender;
    private String receiver;

    @Autowired
    public MailService(JavaMailSender sender, AppProperties properties) {
        this.sender = sender;
        this.receiver = properties.getMailTo();
    }

    public void send(File file) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(receiver);
        helper.setSubject("autometer_db: ".concat(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YY.MM.dd HH:mm:ss"))));
        helper.setText("");
        helper.addAttachment(file.getName(), file);
        sender.send(message);
    }
}