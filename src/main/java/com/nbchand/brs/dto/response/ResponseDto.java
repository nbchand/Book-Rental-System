package com.nbchand.brs.dto.response;

import com.nbchand.brs.dto.author.AuthorDto;
import com.nbchand.brs.dto.category.CategoryDto;
import lombok.*;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-26
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponseDto {
    private boolean status;
    private String message;
    private AuthorDto authorDto;
    private CategoryDto categoryDto;
}
