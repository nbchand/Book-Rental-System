package com.nbchand.brs.controller.book;

import com.nbchand.brs.dto.book.BookDto;
import com.nbchand.brs.service.author.AuthorService;
import com.nbchand.brs.service.book.BookService;
import com.nbchand.brs.service.category.CategoryService;
import com.nbchand.brs.service.date.DateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-27
 */
@Controller
@RequestMapping("/book")
public class BookController {

    private BookService bookService;

    private CategoryService categoryService;

    private AuthorService authorService;

    private DateService dateService;

    public BookController(BookService bookService, CategoryService categoryService, AuthorService authorService, DateService dateService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.dateService = dateService;
    }

    @GetMapping
    public String displayBookLanding(Model model){
        model.addAttribute("bookDtoList", bookService.findAllEntities());
        return "book/bookLanding";
    }

    @GetMapping("/add-book")
    public String displayBookForm(Model model){
        model.addAttribute("bookDto", BookDto.builder().id(null).build());
        model.addAttribute("allCategories", categoryService.findAllEntities());
        model.addAttribute("allAuthors", authorService.findAllEntities());
        model.addAttribute("dateToday", dateService.getTodayDateString());
        return "book/bookForm";
    }

    @PostMapping
    public String addBook(@ModelAttribute("bookDto") BookDto bookDto){
        return "redirect:book";
    }
}
