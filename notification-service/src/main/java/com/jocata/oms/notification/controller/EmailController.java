package com.jocata.oms.notification.controller;

import com.jocata.oms.bean.order.OrderBean;
import com.jocata.oms.notification.request.EmailRequest;
import com.jocata.oms.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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
            @RequestParam("order") OrderBean order,
            @RequestParam("file") MultipartFile mfile
    ) throws IOException {
        File file = File.createTempFile("temp", mfile.getOriginalFilename());
        return emailService.sendEmailWithAttachment(order, file);
    }

}
