package com.nbchand.brs.dto.author;

import lombok.*;

import javax.validation.constraints.NotEmpty;

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
    @NotEmpty(message = "Name of the author must not be empty")
    private String name;
    @NotEmpty(message = "Email of the author must not be empty")
    private String email;
    @NotEmpty(message = "Mobile Number of the author must not be empty")
    private String mobileNumber;
}