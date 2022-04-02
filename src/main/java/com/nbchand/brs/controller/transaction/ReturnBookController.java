package com.nbchand.brs.controller.transaction;

import com.nbchand.brs.dto.BookTransactionDto;
import com.nbchand.brs.dto.ResponseDto;
import com.nbchand.brs.enums.RentType;
import com.nbchand.brs.service.book.BookService;
import com.nbchand.brs.service.bookTransaction.BookTransactionService;
import com.nbchand.brs.service.member.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

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
        if(responseDto.isStatus()) {
            BookTransactionDto bookTransactionDto = responseDto.getBookTransactionDto();
            bookTransactionDto.setRentType(RentType.RETURN);
            bookTransactionDto.setReturnedDate(new Date());
            bookTransactionService.saveEntity(bookTransactionDto);
            redirectAttributes.addFlashAttribute("errorMessage", "Book returned successfully");
            return "redirect:/return";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid transaction code");
        return "redirect:/return/create";
    }

    @DeleteMapping("/{id}")
    public String deleteReturnTransaction(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = bookTransactionService.deleteEntityById(id);
        redirectAttributes.addFlashAttribute("errorMessage", responseDto.getMessage());
        return "redirect:/return";
    }
}
