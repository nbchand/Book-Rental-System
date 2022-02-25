package com.nbchand.brs.dto.author;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

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
@ToString
public class AuthorDto {

    private Integer id;

    @NotEmpty(message = "Author name must not be empty")
    private String name;

    @NotEmpty(message = "Author email must not be empty")
    @Email(message = "Author email invalid")
    private String email;

    @NotEmpty(message = "Author mobile number must not be empty")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be of 10 digits")
    private String mobileNumber;
}