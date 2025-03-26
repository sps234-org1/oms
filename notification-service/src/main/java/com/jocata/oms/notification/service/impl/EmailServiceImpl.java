package com.jocata.oms.notification.service.impl;

import com.jocata.oms.bean.order.OrderBean;
import com.jocata.oms.notification.request.EmailRequest;
import com.jocata.oms.notification.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {

    Context context;

    @Autowired
    private SpringTemplateEngine templateEngine;

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

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

    @KafkaListener(topics = "order-topic", groupId = "notification-group")
    private void handleOrderEvent(OrderBean order) {

        logger.info("Received order event for order id: " + order.getCustomerDetails());
        context = new Context();
        context.setVariable("deliveryDate", LocalDate.now().plusDays(3));
        context.setVariable("order", order );
        logger.info("Context set successfully!");
        sendEmailWithAttachment(order, null);
    }

    public Object sendEmailWithAttachment(OrderBean order, MultipartFile file) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(order.getCustomerDetails().getEmail());
            helper.setSubject("Order Confirmation");
            if (context == null) {
                context = new Context();
                context.setVariable("name", "Customer");
                context.setVariable("orderId", "-1");
                context.setVariable("orders", null);
            }
            String emailBody = templateEngine.process("template1", context);
            helper.setText(emailBody, true);
            if (file != null && !file.isEmpty()) {
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }
            javaMailSender.send(mimeMessage);
            logger.info("Email sent successfully to {}", order.getCustomerDetails().getEmail());
        } catch (Exception e) {
            logger.error("Failed to send email", e);
        }
        return "Email sent successfully!";
    }


}
