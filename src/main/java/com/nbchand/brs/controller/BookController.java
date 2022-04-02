package com.nbchand.brs.controller;

import com.nbchand.brs.dto.BookDto;
import com.nbchand.brs.dto.ResponseDto;
import com.nbchand.brs.service.author.AuthorService;
import com.nbchand.brs.service.book.BookService;
import com.nbchand.brs.service.category.CategoryService;
import com.nbchand.brs.service.date.DateService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final DateService dateService;

    /**
     * Displays book landing page
     * @param model
     * @return respective web-page
     */
    @GetMapping
    public String displayBookLanding(Model model){
        model.addAttribute("bookDtoList", bookService.findAllEntities());
        return "book/bookLanding";
    }

    /**
     * Displays book form
     * @param model
     * @return respective web-page
     */
    @GetMapping("/add-book")
    public String displayBookForm(Model model){
        model.addAttribute("allCategories", categoryService.findAllEntities());
        model.addAttribute("allAuthors", authorService.findAllEntities());
        model.addAttribute("dateToday", dateService.getTodayDateString());
        model.addAttribute("bookDto", BookDto.builder().id(null).build());
        return "book/bookForm";
    }

    /**
     * Creates book
     * @param bookDto
     * @param bindingResult
     * @param model
     * @param redirectAttributes
     * @return respective web-page
     */
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

    /**
     * Displays book form for editing
     * @param id
     * @param model
     * @param redirectAttributes
     * @return respective web-page
     */
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

    /**
     * Updates book after editing
     * @param id
     * @param bookDto
     * @param bindingResult
     * @param model
     * @param redirectAttributes
     * @return respective web-page
     */
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
        return "book/bookForm";
    }

    /**
     * Deletes the book by id
     * @param id
     * @param redirectAttributes
     * @return respective web-page
     */
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") Integer id,
                               RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = bookService.deleteEntityById(id);
        redirectAttributes.addFlashAttribute("errorMessage", responseDto.getMessage());
        return "redirect:/book";
    }

    /**
     * Displays the whole book information
     * @param id
     * @param model
     * @param redirectAttributes
     * @return respective web-page
     */
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
