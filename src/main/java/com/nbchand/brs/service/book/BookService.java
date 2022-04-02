package com.nbchand.brs.service.book;

import com.nbchand.brs.dto.BookDto;
import com.nbchand.brs.service.GenericCrudService;

import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-27
 */
public interface BookService extends GenericCrudService<BookDto> {
    BookDto makeBookDtoComplete(BookDto bookDto);
    List<BookDto> findAllBooksInStock();
}
