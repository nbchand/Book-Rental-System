package com.nbchand.brs.service.book.impl;

import com.nbchand.brs.component.FileStorageComponent;
import com.nbchand.brs.dto.book.BookDto;
import com.nbchand.brs.dto.response.ResponseDto;
import com.nbchand.brs.entity.book.Book;
import com.nbchand.brs.repository.author.AuthorRepo;
import com.nbchand.brs.repository.book.BookRepo;
import com.nbchand.brs.repository.category.CategoryRepo;
import com.nbchand.brs.service.book.BookService;
import com.nbchand.brs.service.date.DateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-27
 */
@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private BookRepo bookRepo;

    private FileStorageComponent fileStorageComponent;

    private CategoryRepo categoryRepo;

    private AuthorRepo authorRepo;

    public DateService dateService;

    public BookServiceImpl(BookRepo bookRepo,
                           FileStorageComponent fileStorageComponent,
                           CategoryRepo categoryRepo,
                           AuthorRepo authorRepo,
                           DateService dateService) {
        this.bookRepo = bookRepo;
        this.fileStorageComponent = fileStorageComponent;
        this.categoryRepo = categoryRepo;
        this.authorRepo = authorRepo;
        this.dateService = dateService;
    }

    @Override
    public ResponseDto saveEntity(BookDto bookDto) {
        ResponseDto fileSavingResponse = fileStorageComponent.storeFile(bookDto.getPhoto());
        if (fileSavingResponse.isStatus()) {
            Book ourBook = null;
            File oldPhoto = null;
            Book book = Book.builder()
                    .name(bookDto.getName())
                    .isbn(bookDto.getIsbn())
                    .numberOfPages(bookDto.getNumberOfPages())
                    .authors(bookDto.getAuthors())
                    .category(bookDto.getCategory())
                    .publishedDate(bookDto.getPublishedDate())
                    .stockCount(bookDto.getStockCount())
                    .photo(fileSavingResponse.getMessage())
                    .rating(bookDto.getRating())
                    .build();
            if (bookDto.getId() != null) {
                ourBook = bookRepo.getById(bookDto.getId());
                oldPhoto = new File(ourBook.getPhoto());
                book.setId(bookDto.getId());
            }
            try {
                bookRepo.save(book);
                if (bookDto.getId() != null) {
                    log.info("Older photo deletion: " + oldPhoto.delete());
                }
                return ResponseDto.builder()
                        .status(true)
                        .build();
            } catch (Exception exception) {
                File uploadedFile = new File(fileSavingResponse.getMessage());
                log.info("Uploaded file deleted: " + uploadedFile.delete());
                if (exception.getMessage().contains("isbn")) {
                    return ResponseDto.builder()
                            .status(false)
                            .message("Book ISBN already in use")
                            .build();
                } else {

                    return ResponseDto.builder()
                            .status(false)
                            .message("Book name already in use")
                            .build();
                }
            }
        }
        return ResponseDto.builder()
                .status(false)
                .message(fileSavingResponse.getMessage())
                .build();
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
                        .publishedDateString(dateService.getDateString(book.getPublishedDate()))
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
                    .categoryId(book.getCategory().getId())
                    .authorIds(book.getAuthors().stream().map(author ->
                            author.getId()).collect(Collectors.toList()))
                    .isbn(book.getIsbn())
                    .publishedDateString(dateService.getDateString(book.getPublishedDate()))
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
        try {
            Book book = bookRepo.getById(id);
            File photo = new File(book.getPhoto());
            log.info("Book Deletion: " + photo.delete());
            bookRepo.deleteById(id);
            return ResponseDto.builder()
                    .status(true)
                    .message("Book deleted successfully")
                    .build();
        } catch (Exception exception) {
            return ResponseDto.builder()
                    .status(false)
                    .message("Book not found")
                    .build();
        }
    }

    @Override
    public BookDto makeBookDtoComplete(BookDto bookDto) {
        bookDto.setCategory(categoryRepo.getById(bookDto.getCategoryId()));
        bookDto.setAuthors(authorRepo.findAllById(bookDto.getAuthorIds()));
        try {
            bookDto.setPublishedDate(new SimpleDateFormat("yyyy-MM-dd").parse(bookDto.getPublishedDateString()));
        } catch (ParseException exception) {
            bookDto.setPublishedDate(new Date());
        }
        return bookDto;
    }
}