package com.nbchand.brs.service.book.impl;

import com.nbchand.brs.dto.book.BookDto;
import com.nbchand.brs.dto.response.ResponseDto;
import com.nbchand.brs.entity.book.Book;
import com.nbchand.brs.repository.book.BookRepo;
import com.nbchand.brs.service.book.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-27
 */
@Service
public class BookServiceImpl implements BookService {

    private BookRepo bookRepo;

    public BookServiceImpl(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    public ResponseDto saveEntity(BookDto bookDto) {
        return null;
    }

    @Override
    public List<BookDto> findAllEntities() {
        List<Book> books = bookRepo.findAll();
        List<BookDto> bookDtoList = books.stream().map(book ->
                BookDto.builder()
                        .id(book.getId())
                        .name(book.getName())
                        .numberOfPages(book.getNumberOfPages())
                        .categoryDto(book.getCategory())
                        .authorDtoList(book.getAuthors())
                        .isbn(book.getIsbn())
                        .publishedDate(book.getPublishedDate())
                        .stockCount(book.getStockCount())
                        .photoLocation(book.getPhoto())
                        .rating(book.getRating())
                        .build()
        ).collect(Collectors.toList());
        return bookDtoList;
    }

    @Override
    public ResponseDto findEntityById(Integer id) {
        try {
            Book book = bookRepo.getById(id);
            BookDto bookDto = BookDto.builder()
                    .id(book.getId())
                    .name(book.getName())
                    .numberOfPages(book.getNumberOfPages())
                    .categoryDto(book.getCategory())
                    .authorDtoList(book.getAuthors())
                    .isbn(book.getIsbn())
                    .publishedDate(book.getPublishedDate())
                    .stockCount(book.getStockCount())
                    .photoLocation(book.getPhoto())
                    .rating(book.getRating())
                    .build();
            return ResponseDto.builder()
                    .bookDto(bookDto)
                    .status(true)
                    .build();
        } catch (Exception exception) {
            return ResponseDto.builder()
                    .status(false)
                    .message("Book not found")
                    .build();
        }
    }

    @Override
    public ResponseDto deleteEntityById(Integer id) {
        return null;
    }
}