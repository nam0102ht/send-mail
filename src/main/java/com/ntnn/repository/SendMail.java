package com.ntnn.repository;

import com.sendgrid.*;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.io.IOException;

public class SendMail {
    private static Logger log = LoggerFactory.getLogger(SendMail.class);
    private static final SendMail INSTANCE =  new SendMail();

    public static SendMail getInstance(){
        return INSTANCE;
    }

    public void sendMail(String from, String subject, String to, String body) {
        Email emailFrom = new Email("nam0108ht@gmail.com");
        Email emailTo = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(emailFrom, subject, emailTo, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            log.error(ex);
        }
    }
}
