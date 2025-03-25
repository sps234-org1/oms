package com.jocata.oms.notification.service.impl;

import com.jocata.oms.notification.request.EmailRequest;
import com.jocata.oms.notification.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public Object sendEmail(EmailRequest emailRequest) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailRequest.getTo());
            message.setSubject(emailRequest.getSubject());
            message.setText(emailRequest.getBody());
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Email sent successfully!";
    }

    public Object sendEmailWithAttachment(EmailRequest emailRequest) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(emailRequest.getTo());
            helper.setSubject(emailRequest.getSubject());

            String templatePath = "D:\\Jocata\\w\\w1\\y25\\m02\\o\\d25\\OrderMgmtSystem\\notification-service\\src\\main\\resources\\template\\template1.html";
            String htmlContent = readHtmlFile(templatePath);
            if (htmlContent == null) {
                helper.setText(emailRequest.getBody());
            } else {
                helper.setText(htmlContent, true);
            }

            String absolutePath = "D:\\Jocata\\w\\w1\\y25\\m02\\o\\d25\\OrderMgmtSystem\\notification-service\\src\\main\\resources\\t1.pdf";
            FileSystemResource file = new FileSystemResource(absolutePath);
            helper.addAttachment("t1.pdf", file);

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Email with attachment sent successfully!";
    }

    private String readHtmlFile(String path) {

        try {
            String htmlContent = new String(Files.readAllBytes(Paths.get(path)));
            htmlContent = htmlContent.replace("{{name}}", "Vasu");
            htmlContent = htmlContent.replace("{{orderId}}", "1");
            htmlContent = htmlContent.replace("{{productName}}", "Product 1");
            htmlContent = htmlContent.replace("{{price}}", "999");
            htmlContent = htmlContent.replace("{{date}}", LocalDate.now().toString());
            return htmlContent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
