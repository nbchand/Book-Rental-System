package com.nbchand.brs.dto.bookTransaction;

import com.nbchand.brs.component.FileStorageComponent;
import com.nbchand.brs.dto.book.BookDto;
import com.nbchand.brs.dto.member.MemberDto;
import com.nbchand.brs.entity.book.Book;
import com.nbchand.brs.entity.member.Member;
import com.nbchand.brs.enums.RentType;
import com.nbchand.brs.service.date.impl.DateServiceImpl;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-03-02
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookTransactionDto {

    private Integer id;

    @NotEmpty(message = "Book code can't be empty")
    @Pattern(regexp = "^\\w{1,10}", message = "Book code must be alphanumeric and at maximum upto 10 characters long")
    private String code;

    private String fromDateString;

    private Date fromDate;

    @NotNull(message = "No. of book renting days can't be null")
    @Min(value = 1, message = "No. of days can't be negative")
    private Integer noOfDays;

    private String toDateString;

    private Date toDate;

    @NotNull(message = "Please provide a book")
    private Integer bookId;

    private BookDto bookDto;

    private Book book;

    @NotNull(message = "Please provide a member")
    private Integer memberId;

    private MemberDto memberDto;

    private Member member;

    private RentType rentType;

    public static class BookTransactionDtoBuilder {
        FileStorageComponent fileStorageComponent = new FileStorageComponent();
        DateServiceImpl dateService = new DateServiceImpl();

        public BookTransactionDtoBuilder bookDto(Book book) {
            this.bookDto = BookDto.builder()
                    .id(book.getId())
                    .name(book.getName())
                    .numberOfPages(book.getNumberOfPages())
                    .categoryDto(book.getCategory())
                    .authorDtoList(book.getAuthors())
                    .categoryId(book.getCategory().getId())
                    .authorIds(book.getAuthors().stream().map(author ->
                            author.getId()).collect(Collectors.toList()))
                    .isbn(book.getIsbn())
                    .publishedDateString(dateService.getDateString(book.getPublishedDate()))
                    .stockCount(book.getStockCount())
                    .photoLocation(fileStorageComponent.returnFileAsBase64(book.getPhoto()))
                    .rating(book.getRating())
                    .build();
            return this;
        }

        public BookTransactionDtoBuilder memberDto(Member member) {
            this.memberDto = MemberDto.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .name(member.getName())
                    .mobileNumber(member.getMobileNumber())
                    .address(member.getAddress())
                    .build();
            return this;
        }
    }
}