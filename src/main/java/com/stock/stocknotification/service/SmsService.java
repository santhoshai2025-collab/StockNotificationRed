package com.stock.stocknotification.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.fromNumber}")
    private String fromNumber;

    @Value("${twilio.toNumber}")
    private String toNumber;

    public void sendSms(String body) {
        logger.info("Initializing Twilio client with account SID: {}", accountSid != null ? "present" : "missing");

        try {
            Twilio.init(accountSid, authToken);
            logger.info("Sending SMS from {} to {} with body: {}", fromNumber, toNumber, body);

            Message message = Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromNumber),
                    body
            ).create();

            logger.info("SMS sent successfully. SID: {}", message.getSid());
        } catch (Exception e) {
            logger.error("Failed to send SMS: {}", e.getMessage(), e);
            throw e; // rethrow so controller/scheduler can handle it
        }
    }
}
