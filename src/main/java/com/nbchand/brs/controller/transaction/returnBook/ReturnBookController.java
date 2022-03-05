package com.nbchand.brs.controller.transaction.returnBook;

import com.nbchand.brs.dto.bookTransaction.BookTransactionDto;
import com.nbchand.brs.enums.RentType;
import com.nbchand.brs.service.book.BookService;
import com.nbchand.brs.service.bookTransaction.BookTransactionService;
import com.nbchand.brs.service.member.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-03-05
 */
@Controller
@RequestMapping("/return")
public class ReturnBookController {

    private BookTransactionService bookTransactionService;
    private MemberService memberService;
    private BookService bookService;

    public ReturnBookController(BookTransactionService bookTransactionService, MemberService memberService, BookService bookService) {
        this.bookTransactionService = bookTransactionService;
        this.memberService = memberService;
        this.bookService = bookService;
    }

    @GetMapping
    public String displayReturnLanding(Model model) {
        model.addAttribute("bookTransactionDtoList", bookTransactionService.findTransactionsByRentType(RentType.RETURN));
        return "return/returnLanding";
    }

    @GetMapping("/create")
    public String displayRentForm(Model model) {
        model.addAttribute("memberDtoList", memberService.findAllEntities());
        model.addAttribute("bookDtoList", bookService.findAllBooksInStock());
        model.addAttribute("bookTransactionDto", BookTransactionDto.builder().id(null).build());
        return "rent/rentForm";
    }
}
