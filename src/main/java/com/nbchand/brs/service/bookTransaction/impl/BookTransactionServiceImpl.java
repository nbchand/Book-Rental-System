package com.nbchand.brs.service.bookTransaction.impl;

import com.nbchand.brs.dto.BookTransactionDto;
import com.nbchand.brs.dto.ResponseDto;
import com.nbchand.brs.entity.Book;
import com.nbchand.brs.entity.BookTransaction;
import com.nbchand.brs.enums.RentType;
import com.nbchand.brs.repository.BookRepo;
import com.nbchand.brs.repository.BookTransactionRepo;
import com.nbchand.brs.repository.MemberRepo;
import com.nbchand.brs.service.bookTransaction.BookTransactionService;
import com.nbchand.brs.service.date.DateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-03-02
 */
@Service
@RequiredArgsConstructor
public class BookTransactionServiceImpl implements BookTransactionService {

    private final BookTransactionRepo bookTransactionRepo;
    private final BookRepo bookRepo;
    private final MemberRepo memberRepo;
    private final DateService dateService;

    /**
     * Saves BookTransaction to the database
     * @param bookTransactionDto
     * @return
     */
    @Override
    public ResponseDto saveEntity(BookTransactionDto bookTransactionDto) {
        BookTransaction bookTransaction = BookTransaction.builder()
                .id(bookTransactionDto.getId())
                .book(bookTransactionDto.getBook())
                .member(bookTransactionDto.getMember())
                .toDate(bookTransactionDto.getToDate())
                .fromDate(bookTransactionDto.getFromDate())
                .rentType(bookTransactionDto.getRentType())
                .code(bookTransactionDto.getCode())
                .build();

        if(bookTransactionDto.getRentType().equals(RentType.RETURN)) {
            bookTransaction.setReturnedDate(bookTransactionDto.getReturnedDate());
        }
        //exception handling for unique key violation
        try {
            Book currentBook = bookTransactionDto.getBook();
            //for saving
            //book id is only null when book is being rented for the first time
            if (bookTransactionDto.getId() == null) {
                //for rent transaction
                //if book is rented stock count is decreased by 1
                currentBook.setStockCount(currentBook.getStockCount() - 1);
            }
            //for editing
            else {
                //for rent transaction
                if (bookTransactionDto.getRentType().name().equals(RentType.RENT.name())) {
                    //if book was rented was edited, get the previous and current book and update their stock count
                    //if book was not changed stock count will remain constant
                    Book prevBook = bookTransactionRepo.getById(bookTransactionDto.getId()).getBook();
                    currentBook.setStockCount(currentBook.getStockCount() - 1);
                    prevBook.setStockCount(prevBook.getStockCount() + 1);
                    bookRepo.save(prevBook);
                }
                //for return transaction
                else if (bookTransactionDto.getRentType().name().equals(RentType.RETURN.name())) {
                    //if book is returned increase its stock count by 1
                    currentBook.setStockCount(currentBook.getStockCount() + 1);
                }
            }
                bookRepo.save(currentBook);
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

    /**
     * Returns all the present BookTransactions
     * @return
     */
    @Override
    public List<BookTransactionDto> findAllEntities() {
        List<BookTransaction> bookTransactions = bookTransactionRepo.findAll();
        List<BookTransactionDto> bookTransactionDtoList = BookTransactionDto.transactionsToTransactionDto(bookTransactions);
        return bookTransactionDtoList;
    }

    /**
     * Returns BookTransaction by id
     * @param id
     * @return
     */
    @Override
    public ResponseDto findEntityById(Integer id) {
        try {
            BookTransaction bookTransaction = bookTransactionRepo.getById(id);
            BookTransactionDto bookTransactionDto = new BookTransactionDto(bookTransaction);
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

    /**
     * Deletes BookTransaction by id
     * @param id
     * @return
     */
    @Override
    public ResponseDto deleteEntityById(Integer id) {
        try {
            bookTransactionRepo.deleteById(id);
            return ResponseDto.builder()
                    .status(true)
                    .message("Book transaction deleted successfully")
                    .build();
        } catch (Exception exception) {
            return ResponseDto.builder()
                    .status(false)
                    .message("Book transaction not found")
                    .build();
        }
    }

    /**
     * Makes BookTransactionDto taken from html complete, so that it can be mapped to BookTransaction
     * @param bookTransactionDto
     * @return
     */
    @Override
    public ResponseDto makeBookTransactionDtoComplete(BookTransactionDto bookTransactionDto) {
        try {
            Book book = bookRepo.getById(bookTransactionDto.getBookId());
            if (book.getStockCount() <= 0 && bookTransactionDto.getRentType().name().equals(RentType.RENT.name())) {
                return ResponseDto.builder()
                        .status(false)
                        .message("Book not in stock")
                        .build();
            }
            bookTransactionDto.setBook(book);
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

    /**
     * Find all the transactions by rent type
     * @param rentType
     * @return
     */
    @Override
    public List<BookTransactionDto> findTransactionsByRentType(RentType rentType) {
        List<BookTransaction> bookTransactions = bookTransactionRepo.findAllByRentType(rentType);
        List<BookTransactionDto> bookTransactionDtoList = BookTransactionDto.transactionsToTransactionDto(bookTransactions);
        return bookTransactionDtoList;
    }

    /**
     * Returns all the transaction code
     * @return
     */
    @Override
    public List<String> getAllTransactionCode() {
        return bookTransactionRepo.findAllTransactionCode();
    }

    /**
     * Finds the transaction by its code
     * @param code
     * @return
     */
    @Override
    public ResponseDto findTransactionByCode(String code) {
        try {
            BookTransaction bookTransaction = bookTransactionRepo.findBookTransactionByCode(code);
            BookTransactionDto bookTransactionDto = new BookTransactionDto(bookTransaction);
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
}
