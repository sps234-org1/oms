package com.jocata.oms.notification.controller;

import com.jocata.oms.notification.request.EmailRequest;
import com.jocata.oms.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping("/send")
    Object sendEmail(@RequestBody EmailRequest emailRequest) {
        return emailService.sendEmail(emailRequest);
    }

    @PostMapping("/send/attachment")
    Object sendEmailWithAttachment(
            @RequestBody EmailRequest emailRequest) {
        return emailService.sendEmailWithAttachment(emailRequest);
    }

}
