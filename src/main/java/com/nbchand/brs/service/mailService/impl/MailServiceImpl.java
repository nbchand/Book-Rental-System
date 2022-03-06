package com.nbchand.brs.service.mailService.impl;

import com.nbchand.brs.dto.author.AuthorDto;
import com.nbchand.brs.service.mailService.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-03-06
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private JavaMailSender mailSender;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendAuthorRegistrationMail(AuthorDto author) {
        String toAddress = author.getEmail();
        String fromAddress = "brswicc@gmail.com";
        String senderName = "BRS TEAM";
        String subject = "Addition as author";
        String content = "Dear " + author.getName() + ",<br>"
                + "You have been added as an author on our system.<br>"
                + "Thank you,<br>"
                + "Book Rental System Team";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception exception) {
            log.error("Sorry mail can't be sent to author");
        }
    }
}
