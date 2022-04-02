package com.nbchand.brs.dto;

import com.nbchand.brs.entity.Author;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-24
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {

    private Integer id;

    @NotEmpty(message = "Author name must not be empty")
    @Size(max = 80, message = "Author name can only be upto 80 characters")
    private String name;

    @NotEmpty(message = "Author email must not be empty")
    @Email(message = "Author email invalid")
    private String email;

    @NotEmpty(message = "Author mobile number must not be empty")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be of 10 digits")
    private String mobileNumber;

    public AuthorDto(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.email = author.getEmail();
        this.mobileNumber = author.getMobileNumber();
    }

    public static List<AuthorDto> authorsToAuthorDtos(List<Author> authorList) {
        List<AuthorDto> authorDtoList = new ArrayList<>();
        for(Author author: authorList) {
            authorDtoList.add(new AuthorDto(author));
        }
        return authorDtoList;
    }
}