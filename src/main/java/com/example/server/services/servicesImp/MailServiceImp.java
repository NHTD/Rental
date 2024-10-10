package com.example.server.services.servicesImp;

import com.example.server.services.MailService;
import com.example.server.utils.VelocityUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailServiceImp implements MailService {

    @Value("${spring.mail.username}")
    String username;

    @Value("${spring.mail.template}")
    String template;

    final JavaMailSender mailSender;

    @Override
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, true);

        mailSender.send(mimeMessage);
    }

    @Override
    public void sendTemplateEmail(String to, String subject, Map<String, Object> params, String fileName) throws IOException, MessagingException {
        VelocityContext context = new VelocityContext();
        for(Map.Entry<String, Object> entry : params.entrySet()) {
            context.put(entry.getKey(), entry.getValue());
        }

        Resource resource = new ClassPathResource(template);
        File file = resource.getFile();
        String folder = file.getPath();

//        String filePath = file.getPath() + File.separator + fileName;
        String content = VelocityUtil.generateTemplate(folder, fileName, context);
        sendHtmlMail(to, subject, content);
    }
}
