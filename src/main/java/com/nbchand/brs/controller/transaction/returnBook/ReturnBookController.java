package com.nbchand.brs.controller.transaction.returnBook;

import com.nbchand.brs.dto.bookTransaction.BookTransactionDto;
import com.nbchand.brs.dto.response.ResponseDto;
import com.nbchand.brs.enums.RentType;
import com.nbchand.brs.service.book.BookService;
import com.nbchand.brs.service.bookTransaction.BookTransactionService;
import com.nbchand.brs.service.member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
    public String displayReturnForm(Model model) {
        model.addAttribute("codeList", bookTransactionService.getAllTransactionCode());
        return "return/returnForm";
    }

    @PostMapping("/data")
    @ResponseBody
    public BookTransactionDto sendTransactionData(@RequestBody String code) throws Exception {
        ResponseDto responseDto = bookTransactionService.findTransactionByCode(code);
        if(responseDto.isStatus()) {
            return responseDto.getBookTransactionDto();
        }
        throw new Exception("Transaction not found");
    }

    @PostMapping
    public String addReturnTransaction(@ModelAttribute("code") String code, RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = bookTransactionService.findTransactionByCode(code);
        System.out.println(responseDto.getBookTransactionDto().getMember().getName());
        if(responseDto.isStatus()) {
            BookTransactionDto bookTransactionDto = responseDto.getBookTransactionDto();
            bookTransactionDto.setRentType(RentType.RETURN);
            bookTransactionService.saveEntity(bookTransactionDto);
            redirectAttributes.addFlashAttribute("errorMessage", "Book returned successfully");
            return "redirect:/return";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid transaction code");
        return "redirect:/return/create";
    }
}
