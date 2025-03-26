package com.jocata.oms.notification.service;

import com.jocata.oms.bean.order.OrderBean;
import com.jocata.oms.notification.request.EmailRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface EmailService {

    Object sendEmail(EmailRequest emailRequest);

    Object sendEmailWithAttachment(OrderBean order, File file);

}
