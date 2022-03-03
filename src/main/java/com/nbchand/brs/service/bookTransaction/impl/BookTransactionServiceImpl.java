package com.nbchand.brs.service.bookTransaction.impl;

import com.nbchand.brs.dto.bookTransaction.BookTransactionDto;
import com.nbchand.brs.dto.response.ResponseDto;
import com.nbchand.brs.entity.bookTransaction.BookTransaction;
import com.nbchand.brs.repository.book.BookRepo;
import com.nbchand.brs.repository.bookTransaction.BookTransactionRepo;
import com.nbchand.brs.repository.member.MemberRepo;
import com.nbchand.brs.service.bookTransaction.BookTransactionService;
import com.nbchand.brs.service.date.DateService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-03-02
 */
@Service
public class BookTransactionServiceImpl implements BookTransactionService {

    private BookTransactionRepo bookTransactionRepo;
    private BookRepo bookRepo;
    private MemberRepo memberRepo;
    private DateService dateService;

    public BookTransactionServiceImpl(BookTransactionRepo bookTransactionRepo, BookRepo bookRepo, MemberRepo memberRepo, DateService dateService) {
        this.bookTransactionRepo = bookTransactionRepo;
        this.bookRepo = bookRepo;
        this.memberRepo = memberRepo;
        this.dateService = dateService;
    }

    @Override
    public ResponseDto saveEntity(BookTransactionDto bookTransactionDto) {
        BookTransaction bookTransaction = BookTransaction.builder()
                .book(bookTransactionDto.getBook())
                .member(bookTransactionDto.getMember())
                .toDate(bookTransactionDto.getToDate())
                .fromDate(bookTransactionDto.getFromDate())
                .rentType(bookTransactionDto.getRentType())
                .code(bookTransactionDto.getCode())
                .build();
        if (bookTransaction.getId() != null) {
            bookTransaction.setId(bookTransaction.getId());
        }
        try {
            bookTransactionRepo.save(bookTransaction);
            return ResponseDto.builder()
                    .status(true)
                    .build();
        } catch (Exception exception) {
            return ResponseDto.builder()
                    .status(false)
                    .message("Transaction code is already taken")
                    .build();
        }
    }

    @Override
    public List<BookTransactionDto> findAllEntities() {
        List<BookTransaction> bookTransactions = bookTransactionRepo.findAll();
        List<BookTransactionDto> bookTransactionDtoList = bookTransactions.stream().map(bookTransaction ->
                        BookTransactionDto.builder()
                                .bookDto(bookTransaction.getBook())
                                .memberDto(bookTransaction.getMember())
                                .id(bookTransaction.getId())
                                .code(bookTransaction.getCode())
                                .fromDateString(dateService.getDateString(bookTransaction.getFromDate()))
                                .toDateString(dateService.getDateString(bookTransaction.getToDate()))
                                .noOfDays(dateService.findDifferenceInDays(bookTransaction.getToDate(), bookTransaction.getFromDate()))
                                .build())
                .collect(Collectors.toList());
        return bookTransactionDtoList;
    }

    @Override
    public ResponseDto findEntityById(Integer id) {
        try {
            BookTransaction bookTransaction = bookTransactionRepo.getById(id);
            BookTransactionDto bookTransactionDto = BookTransactionDto.builder()
                    .bookDto(bookTransaction.getBook())
                    .memberDto(bookTransaction.getMember())
                    .id(bookTransaction.getId())
                    .code(bookTransaction.getCode())
                    .fromDateString(dateService.getDateString(bookTransaction.getFromDate()))
                    .toDateString(dateService.getDateString(bookTransaction.getToDate()))
                    .noOfDays(dateService.findDifferenceInDays(bookTransaction.getToDate(), bookTransaction.getFromDate()))
                    .build();
            return ResponseDto.builder()
                    .status(true)
                    .bookTransactionDto(bookTransactionDto)
                    .build();
        } catch (Exception exception) {
            return ResponseDto.builder()
                    .status(false)
                    .message("Book transaction not found")
                    .build();
        }
    }

    @Override
    public ResponseDto deleteEntityById(Integer id) {
        return null;
    }

    @Override
    public ResponseDto makeBookTransactionDtoComplete(BookTransactionDto bookTransactionDto) {
        try {
            bookTransactionDto.setBook(bookRepo.getById(bookTransactionDto.getBookId()));
            bookTransactionDto.setMember(memberRepo.getById(bookTransactionDto.getMemberId()));
            bookTransactionDto.setToDate(dateService.findToDate(bookTransactionDto.getFromDate(), bookTransactionDto.getNoOfDays()));
            return ResponseDto.builder()
                    .status(true)
                    .bookTransactionDto(bookTransactionDto)
                    .build();
        } catch (Exception exception) {
            return ResponseDto.builder()
                    .status(false)
                    .message("Book transaction information invalid")
                    .build();
        }
    }
}
