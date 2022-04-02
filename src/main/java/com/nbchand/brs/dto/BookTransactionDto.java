package com.nbchand.brs.dto;

import com.nbchand.brs.entity.Book;
import com.nbchand.brs.entity.BookTransaction;
import com.nbchand.brs.entity.Member;
import com.nbchand.brs.enums.RentType;
import com.nbchand.brs.service.date.impl.DateServiceImpl;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    private Date returnedDate;

    private String returnedDateString;

    @NotNull(message = "Please provide a book")
    private Integer bookId;

    private BookDto bookDto;

    private Book book;

    @NotNull(message = "Please provide a member")
    private Integer memberId;

    private MemberDto memberDto;

    private Member member;

    private RentType rentType;

    public BookTransactionDto(BookTransaction bookTransaction) {
        DateServiceImpl dateService = new DateServiceImpl();
        this.id = bookTransaction.getId();
        this.code = bookTransaction.getCode();
        this.fromDate = bookTransaction.getFromDate();
        this.toDate = bookTransaction.getToDate();
        this.fromDateString = dateService.getDateString(bookTransaction.getFromDate());
        this.toDateString = dateService.getDateString(bookTransaction.getToDate());
        this.bookDto = new BookDto(bookTransaction.getBook());
        this.memberDto = new MemberDto(bookTransaction.getMember());
        this.rentType = bookTransaction.getRentType();
        this.noOfDays = dateService.findDifferenceInDays(bookTransaction.getToDate(), bookTransaction.getFromDate());
        this.book = bookTransaction.getBook();
        this.member = bookTransaction.getMember();
        this.memberId = memberDto.getId();
        this.bookId = bookDto.getId();
        if(bookTransaction.getRentType().equals(RentType.RETURN)) {
            this.returnedDateString = dateService.getDateString(bookTransaction.getReturnedDate());
        }
    }

    public static List<BookTransactionDto> transactionsToTransactionDto(List<BookTransaction> bookTransactions) {
        List<BookTransactionDto> bookTransactionDtoList = new ArrayList<>();
        for(BookTransaction bookTransaction: bookTransactions) {
            bookTransactionDtoList.add(new BookTransactionDto(bookTransaction));
        }
        return bookTransactionDtoList;
    }
}