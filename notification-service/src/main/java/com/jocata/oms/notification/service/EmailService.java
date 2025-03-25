package com.jocata.oms.notification.service;

import com.jocata.oms.notification.request.EmailRequest;

public interface EmailService {

    Object sendEmail(EmailRequest emailRequest);

    Object sendEmailWithAttachment(EmailRequest emailRequest);

}
