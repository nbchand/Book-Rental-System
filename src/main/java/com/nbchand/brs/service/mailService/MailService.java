package com.nbchand.brs.service.mailService;

import com.nbchand.brs.dto.author.AuthorDto;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-03-06
 */
public interface MailService {
    void sendAuthorRegistrationMail(AuthorDto author);
}
