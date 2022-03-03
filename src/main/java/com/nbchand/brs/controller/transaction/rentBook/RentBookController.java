package com.nbchand.brs.controller.transaction.rentBook;

import com.nbchand.brs.dto.bookTransaction.BookTransactionDto;
import com.nbchand.brs.dto.response.ResponseDto;
import com.nbchand.brs.enums.RentType;
import com.nbchand.brs.service.book.BookService;
import com.nbchand.brs.service.bookTransaction.BookTransactionService;
import com.nbchand.brs.service.member.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-03-02
 */
@Controller
@RequestMapping("/rent")
public class RentBookController {

    private BookTransactionService bookTransactionService;
    private MemberService memberService;
    private BookService bookService;

    public RentBookController(BookTransactionService bookTransactionService, MemberService memberService, BookService bookService) {
        this.bookTransactionService = bookTransactionService;
        this.memberService = memberService;
        this.bookService = bookService;
    }

    @GetMapping
    public String displayRentLanding(Model model) {
        model.addAttribute("bookTransactionDtoList", bookTransactionService.findAllEntities());
        return "rent/rentLanding";
    }

    @GetMapping("/create")
    public String displayRentForm(Model model) {
        model.addAttribute("memberDtoList", memberService.findAllEntities());
        model.addAttribute("bookDtoList", bookService.findAllEntities());
        model.addAttribute("bookTransactionDto", BookTransactionDto.builder().id(null).build());
        return "rent/rentForm";
    }

    @PostMapping
    public String addRent(@Valid @ModelAttribute BookTransactionDto bookTransactionDto,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("memberDtoList", memberService.findAllEntities());
            model.addAttribute("bookDtoList", bookService.findAllEntities());
            model.addAttribute("bookTransactionDto", bookTransactionDto);
            return "rent/rentForm";
        }
        bookTransactionDto.setFromDate(new Date());
        bookTransactionDto.setRentType(RentType.RENT);
        ResponseDto completeResponse = bookTransactionService.makeBookTransactionDtoComplete(bookTransactionDto);
        if(completeResponse.isStatus()){
            ResponseDto saveResponse = bookTransactionService.saveEntity(completeResponse.getBookTransactionDto());
            if(saveResponse.isStatus()){
                redirectAttributes.addFlashAttribute("errorMessage", "Book transaction added successfully");
            }
            else{
                redirectAttributes.addFlashAttribute("errorMessage", saveResponse.getMessage());
                return "redirect:/rent";
            }
        }
        redirectAttributes.addFlashAttribute("errorMessage", completeResponse.getMessage());
        return "redirect:/rent";
    }
}