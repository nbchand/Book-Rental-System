package com.nbchand.brs.service.author.impl;

import com.nbchand.brs.dto.author.AuthorDto;
import com.nbchand.brs.dto.response.ResponseDto;
import com.nbchand.brs.entity.author.Author;
import com.nbchand.brs.repository.author.AuthorRepo;
import com.nbchand.brs.service.author.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-24
 */
@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepo authorRepo;

    public AuthorServiceImpl(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    @Override
    public ResponseDto saveEntity(AuthorDto authorDto) {
        Author author = Author.builder()
                .email(authorDto.getEmail())
                .mobileNumber(authorDto.getMobileNumber())
                .name(authorDto.getName())
                .build();
        if (authorDto.getId() != null) {
            author.setId(authorDto.getId());
        }

        try {
            authorRepo.save(author);
            return new ResponseDto(true, null);
        } catch (Exception exception) {
            if (exception.getMessage().contains("mobile")) {
                return new ResponseDto(false, "Mobile number already in use");
            } else {
                return new ResponseDto(false, "Email address already in use");
            }
        }
    }

    @Override
    public List<AuthorDto> findAllEntities() {
        List<Author> authors = authorRepo.findAll();
        List<AuthorDto> authorDtoList = authors.stream().map(author ->
                AuthorDto.builder()
                        .email(author.getEmail())
                        .mobileNumber(author.getMobileNumber())
                        .name(author.getName())
                        .id(author.getId())
                        .build()
        ).collect(Collectors.toList());

        return authorDtoList;
    }

    @Override
    public AuthorDto findEntityById(Integer id) {
        Author author = authorRepo.getById(id);
        AuthorDto authorDto = AuthorDto.builder()
                .email(author.getEmail())
                .name(author.getName())
                .mobileNumber(author.getMobileNumber())
                .id(author.getId())
                .build();
        return authorDto;
    }

    @Override
    public void deleteEntityById(Integer id) {
        authorRepo.deleteById(id);
    }
}
