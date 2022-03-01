package com.nbchand.brs.controller.book;

import com.nbchand.brs.dto.book.BookDto;
import com.nbchand.brs.dto.response.ResponseDto;
import com.nbchand.brs.service.author.AuthorService;
import com.nbchand.brs.service.book.BookService;
import com.nbchand.brs.service.category.CategoryService;
import com.nbchand.brs.service.date.DateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
        model.addAttribute("allCategories", categoryService.findAllEntities());
        model.addAttribute("allAuthors", authorService.findAllEntities());
        model.addAttribute("dateToday", dateService.getTodayDateString());
        model.addAttribute("bookDto", BookDto.builder().id(null).build());
        return "book/bookForm";
    }

    @PostMapping
    public String addBook(@Valid @ModelAttribute("bookDto") BookDto bookDto,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allCategories", categoryService.findAllEntities());
            model.addAttribute("allAuthors", authorService.findAllEntities());
            model.addAttribute("dateToday", dateService.getTodayDateString());
            model.addAttribute("bookDto", bookDto);
            return "book/bookForm";
        }
        BookDto completeBookDto = bookService.makeBookDtoComplete(bookDto);
        ResponseDto responseDto = bookService.saveEntity(completeBookDto);
        if (responseDto.isStatus()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Book added successfully");
            return "redirect:/book";
        }
        model.addAttribute("errorMessage", responseDto.getMessage());
        model.addAttribute("allCategories", categoryService.findAllEntities());
        model.addAttribute("allAuthors", authorService.findAllEntities());
        model.addAttribute("dateToday", dateService.getTodayDateString());
        model.addAttribute("bookDto", bookDto);
        return "book/bookForm";
    }

    @GetMapping("/edit/{id}")
    public String displayBookEditPage(@PathVariable("id") Integer id, Model model,
                                        RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = bookService.findEntityById(id);
        if (responseDto.isStatus()) {
            model.addAttribute("allCategories", categoryService.findAllEntities());
            model.addAttribute("allAuthors", authorService.findAllEntities());
            model.addAttribute("dateToday", dateService.getTodayDateString());
            model.addAttribute("bookDto", responseDto.getBookDto());
            return "book/bookForm";
        }
        redirectAttributes.addFlashAttribute("errorMessage", responseDto.getMessage());
        return "redirect:/author";
    }

    @PutMapping("/{id}")
    public String updateBook(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("bookDto") BookDto bookDto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        bookDto.setId(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("allCategories", categoryService.findAllEntities());
            model.addAttribute("allAuthors", authorService.findAllEntities());
            model.addAttribute("dateToday", dateService.getTodayDateString());
            model.addAttribute("bookDto", bookDto);
            return "book/bookForm";
        }
        BookDto completeBookDto = bookService.makeBookDtoComplete(bookDto);
        ResponseDto responseDto = bookService.saveEntity(completeBookDto);
        if (responseDto.isStatus()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Book updated successfully");
            return "redirect:/book";
        }
        model.addAttribute("errorMessage", responseDto.getMessage());
        model.addAttribute("allCategories", categoryService.findAllEntities());
        model.addAttribute("allAuthors", authorService.findAllEntities());
        model.addAttribute("dateToday", dateService.getTodayDateString());
        model.addAttribute("bookDto", bookDto);
        return "author/authorForm";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") Integer id,
                               RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = bookService.deleteEntityById(id);
        redirectAttributes.addFlashAttribute("errorMessage", responseDto.getMessage());
        return "redirect:/book";
    }

    @GetMapping("/{id}")
    public String displayBookDescription(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        ResponseDto responseDto = bookService.findEntityById(id);
        if(responseDto.isStatus()){
            model.addAttribute("bookDto", responseDto.getBookDto());
            return "book/bookDescription";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Book not found");
        return "redirect:/book";
    }
}
