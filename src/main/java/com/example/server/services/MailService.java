package com.example.server.services;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.Map;

public interface MailService {
     void sendHtmlMail(String to, String subject, String content) throws MessagingException;
     void sendTemplateEmail(String to, String subject, Map<String, Object> params, String fileName) throws IOException, MessagingException;
}
