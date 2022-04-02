package com.nbchand.brs.service.author.impl;

import com.nbchand.brs.dto.AuthorDto;
import com.nbchand.brs.dto.ResponseDto;
import com.nbchand.brs.entity.Author;
import com.nbchand.brs.repository.AuthorRepo;
import com.nbchand.brs.service.author.AuthorService;
import com.nbchand.brs.service.mailService.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-24
 */
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepo authorRepo;

    private final MailService mailService;

    /**
     * Takes AuthorDto, maps it into author and saves the author into database
     * @param authorDto
     * @return
     */
    @Override
    public ResponseDto saveEntity(AuthorDto authorDto) {
        Author author = Author.builder()
                .email(authorDto.getEmail())
                .mobileNumber(authorDto.getMobileNumber())
                .name(authorDto.getName())
                .build();
        //if the author was meant to be edited set its id
        if (authorDto.getId() != null) {
            author.setId(authorDto.getId());
        }
        //exception handling because jpa can throw exception when unique keys are duplicated
        try {
            //checks if author's email was changed or not
            Boolean areEmailSame = this.compareEmail(authorDto);
            //saves author
            authorRepo.save(author);
            //if author were created send mail to their email
            if(authorDto.getId() == null) {
                mailService.sendAuthorRegistrationMail(authorDto);
            }
            //if author were edited check whether their mail were changed or not
            //if changed send mail to their new mail
            else {
                if(!areEmailSame) {
                    mailService.sendAuthorRegistrationMail(authorDto);
                }
            }
            //return response with status as true
            return ResponseDto.builder()
                    .status(true)
                    .build();
        } catch (Exception exception) {
            //if duplicate mobile number is entered
            if (exception.getMessage().contains("mobile")) {
                return ResponseDto.builder()
                        .status(false)
                        .message("Mobile number already in use")
                        .build();
            }
            //if duplicate email address is entered
            else {
                return ResponseDto.builder()
                        .status(false)
                        .message("Email address already in use")
                        .build();
            }
        }
    }

    /**
     * Finds all the authors
     * @return
     */
    @Override
    public List<AuthorDto> findAllEntities() {
        List<Author> authors = authorRepo.findAll();
        List<AuthorDto> authorDtoList = AuthorDto.authorsToAuthorDtos(authors);
        return authorDtoList;
    }

    /**
     * Finds author by id
     * @param id author id
     * @return AuthorDto
     */
    @Override
    public ResponseDto findEntityById(Integer id) {
        try {
            Author author = authorRepo.getById(id);
            AuthorDto authorDto = new AuthorDto(author);
            return ResponseDto.builder()
                    .status(true)
                    .authorDto(authorDto)
                    .build();
        } catch (Exception e) {
            return ResponseDto.builder()
                    .status(false)
                    .message("Author not found")
                    .build();
        }

    }

    /**
     * Deletes author
     * @param id author id
     * @return response with status and message
     */
    @Override
    public ResponseDto deleteEntityById(Integer id) {
        try {
            authorRepo.deleteById(id);
            return ResponseDto.builder()
                    .status(true)
                    .message("Author deleted successfully")
                    .build();
        } catch (Exception exception) {
            return ResponseDto.builder()
                    .status(false)
                    .message("Author not found")
                    .build();
        }
    }

    /**
     * Checks if author's mail was changed or not
     * @param authorDto Author detail
     * @return Returns true if email was not changed otherwise false
     */
    public Boolean compareEmail(AuthorDto authorDto) {
        //if author is being created for the first time it will just return true
        if(authorDto.getId()==null){
            return true;
        }
        //gets current email
        String currentEmail = authorDto.getEmail();
        //gets previous email from the database
        String prevEmail = authorRepo.getById(authorDto.getId()).getEmail();
        return currentEmail.equals(prevEmail);
    }
}
