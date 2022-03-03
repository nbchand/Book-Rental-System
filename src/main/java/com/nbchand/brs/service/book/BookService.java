package com.nbchand.brs.service.book;

import com.nbchand.brs.dto.book.BookDto;
import com.nbchand.brs.service.GenericCrudService;

import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-27
 */
public interface BookService extends GenericCrudService<BookDto> {
    public BookDto makeBookDtoComplete(BookDto bookDto);
    public List<BookDto> findAllBooksInStock();
}
