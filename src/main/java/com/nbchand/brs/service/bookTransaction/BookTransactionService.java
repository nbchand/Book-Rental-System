package com.nbchand.brs.service.bookTransaction;

import com.nbchand.brs.dto.bookTransaction.BookTransactionDto;
import com.nbchand.brs.dto.response.ResponseDto;
import com.nbchand.brs.service.GenericCrudService;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-03-02
 */
public interface BookTransactionService extends GenericCrudService<BookTransactionDto> {
    ResponseDto makeBookTransactionDtoComplete(BookTransactionDto bookTransactionDto);
}
