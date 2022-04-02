package com.nbchand.brs.service.book.impl;

import com.nbchand.brs.component.FileStorageComponent;
import com.nbchand.brs.dto.BookDto;
import com.nbchand.brs.dto.ResponseDto;
import com.nbchand.brs.entity.Book;
import com.nbchand.brs.repository.AuthorRepo;
import com.nbchand.brs.repository.BookRepo;
import com.nbchand.brs.repository.CategoryRepo;
import com.nbchand.brs.service.book.BookService;
import com.nbchand.brs.service.date.DateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepo bookRepo;

    private final FileStorageComponent fileStorageComponent;

    private final CategoryRepo categoryRepo;

    private final AuthorRepo authorRepo;

    public final DateService dateService;

    /**
     * Saves book to the database
     * @param bookDto
     * @return
     */
    @Override
    public ResponseDto saveEntity(BookDto bookDto) {
        //check if user is editing valid book or not
        if (bookDto.getId() != null ) {
            try {
                Book demoBook = bookRepo.findById(bookDto.getId()).orElseThrow(RuntimeException::new);
            }
            catch (RuntimeException runtimeException) {
                return ResponseDto.builder()
                        .status(false)
                        .message("Book not found")
                        .build();
            }
        }
        //build book from bookDto
        Book book = Book.builder()
                .id(bookDto.getId())
                .name(bookDto.getName())
                .isbn(bookDto.getIsbn())
                .numberOfPages(bookDto.getNumberOfPages())
                .authors(bookDto.getAuthors())
                .category(bookDto.getCategory())
                .publishedDate(bookDto.getPublishedDate())
                .stockCount(bookDto.getStockCount())
                .rating(bookDto.getRating())
                .build();

        ResponseDto responseDto = fileStorageComponent.storeFile(bookDto);
        //if file was not stored return status as false and send error message
        if(responseDto != null && !responseDto.isStatus()) {
            return ResponseDto.builder()
                    .status(false)
                    .message(responseDto.getMessage())
                    .build();
        }
        //executes if
        //1) user uploaded image and that image was stored
        //2) user created book for the first time and didn't upload any image so dummy image was returned
        if(responseDto != null) {
            book.setPhoto(responseDto.getMessage());
        }

        //if book was edited and user didn't upload any image
        if(responseDto == null) {
            book.setPhoto(bookRepo.getById(book.getId()).getPhoto());
        }
        try{
            Boolean isPhotoChanged = false;
            String prevPhoto = null;
            //extract previous book photo and find out whether it was changed or not when book was edited
            if(book.getId() != null) {
                Book prevBook = bookRepo.getById(book.getId());
                isPhotoChanged = fileStorageComponent.isPhotoChanged(book, prevBook);
                prevPhoto = prevBook.getPhoto();
            }
            //save book
            bookRepo.save(book);
            //if book with new updated photo was saved, delete the older photo
            if(isPhotoChanged) {
                fileStorageComponent.deletePhoto(prevPhoto);
            }
            //send success response
            return ResponseDto.builder()
                    .status(true)
                    .build();
        }
        catch (Exception exception) {
            //if book was not saved delete the uploaded photo which was stored
            if(responseDto != null && !(fileStorageComponent.isImageDummy(responseDto.getMessage()))) {
                fileStorageComponent.deletePhoto(responseDto.getMessage());
            }
            //executes if duplicate book isbn was entered
            if (exception.getMessage().contains("isbn")) {
                return ResponseDto.builder()
                        .status(false)
                        .message("Book ISBN already in use")
                        .build();
            }
            //executes if duplicate book name was entered
            else {

                return ResponseDto.builder()
                        .status(false)
                        .message("Book name already in use")
                        .build();
            }
        }
    }

    /**
     * Get all the books in the database
     * @return
     */
    @Override
    public List<BookDto> findAllEntities() {
        List<Book> books = bookRepo.findAll();
        List<BookDto> bookDtoList = BookDto.booksToBookDtos(books);
        return bookDtoList;
    }

    /**
     * Get a single book by id
     * @param id book id
     * @return ResponseDto with status and message/BookDto
     */
    @Override
    public ResponseDto findEntityById(Integer id) {
        try {
            Book book = bookRepo.getById(id);
            BookDto bookDto = new BookDto(book);
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

    /**
     * Delete book from the database and its photo from memory
     * @param id book id
     * @return ResponseDto
     */
    @Override
    public ResponseDto deleteEntityById(Integer id) {
        try {
            Book book = bookRepo.getById(id);
            if(!(fileStorageComponent.isImageDummy(book.getPhoto()))) {
                fileStorageComponent.deletePhoto(book.getPhoto());
            }
            bookRepo.deleteById(id);
            return ResponseDto.builder()
                    .status(true)
                    .message("Book deleted successfully")
                    .build();
        }
        //if book id was invalid
        catch (Exception exception) {
            return ResponseDto.builder()
                    .status(false)
                    .message("Book not found")
                    .build();
        }
    }

    /**
     * Makes BookDto taken from html complete, so that it can be mapped to Book
     * @param bookDto incomplete BookDto
     * @return a complete BookDto
     */
    @Override
    public BookDto makeBookDtoComplete(BookDto bookDto) {
        //set category, authors and published date
        bookDto.setCategory(categoryRepo.getById(bookDto.getCategoryId()));
        bookDto.setAuthors(authorRepo.findAllById(bookDto.getAuthorIds()));
        try {
            bookDto.setPublishedDate(new SimpleDateFormat("yyyy-MM-dd").parse(bookDto.getPublishedDateString()));
        }
        //if date published date can't be parsed set today's date as published date
        catch (ParseException exception) {
            bookDto.setPublishedDate(new Date());
        }
        return bookDto;
    }

    /**
     * Returns the list of books which have stockCount more than 0
     * @return BookDto list
     */
    @Override
    public List<BookDto> findAllBooksInStock() {
        List<Book> books = bookRepo.findAllByStockCountIsGreaterThan(0);
        List<BookDto> bookDtoList = BookDto.booksToBookDtos(books);
        return bookDtoList;
    }
}