package com.nbchand.brs.dto;

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
public class ResponseDto {
    private boolean status;
    private String message;
    private AuthorDto authorDto;
    private CategoryDto categoryDto;
    private MemberDto memberDto;
    private BookDto bookDto;
    private BookTransactionDto bookTransactionDto;
}
