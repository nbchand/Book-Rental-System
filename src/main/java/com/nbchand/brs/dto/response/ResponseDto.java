package com.nbchand.brs.dto.response;

import com.nbchand.brs.dto.author.AuthorDto;
import com.nbchand.brs.dto.book.BookDto;
import com.nbchand.brs.dto.bookTransaction.BookTransactionDto;
import com.nbchand.brs.dto.category.CategoryDto;
import com.nbchand.brs.dto.member.MemberDto;
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
    private MemberDto memberDto;
    private BookDto bookDto;
    private BookTransactionDto bookTransactionDto;
}
