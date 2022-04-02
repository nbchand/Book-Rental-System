package com.nbchand.brs.controller.transaction;

import com.nbchand.brs.dto.BookTransactionDto;
import com.nbchand.brs.dto.ResponseDto;
import com.nbchand.brs.enums.RentType;
import com.nbchand.brs.service.book.BookService;
import com.nbchand.brs.service.bookTransaction.BookTransactionService;
import com.nbchand.brs.service.member.MemberService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ReturnBookController {

    private final BookTransactionService bookTransactionService;
    private final MemberService memberService;
    private final BookService bookService;

    /**
     * Display book return landing page
     * @param model
     * @return respective web-page
     */
    @GetMapping
    public String displayReturnLanding(Model model) {
        model.addAttribute("bookTransactionDtoList", bookTransactionService.findTransactionsByRentType(RentType.RETURN));
        return "return/returnLanding";
    }

    /**
     * Display book return form
     * @param model
     * @return respective web-page
     */
    @GetMapping("/create")
    public String displayReturnForm(Model model) {
        model.addAttribute("codeList", bookTransactionService.getAllTransactionCode());
        return "return/returnForm";
    }

    /**
     * Send transaction data to the ajax call on the basis of provided code
     * @param code unique code for the transaction
     * @return json data
     * @throws Exception
     */
    @PostMapping("/data")
    @ResponseBody
    public BookTransactionDto sendTransactionData(@RequestBody String code) throws Exception {
        ResponseDto responseDto = bookTransactionService.findTransactionByCode(code);
        if(responseDto.isStatus()) {
            return responseDto.getBookTransactionDto();
        }
        throw new Exception("Transaction not found");
    }

    /**
     * Create return transaction by updating the rented transaction
     * @param code
     * @param redirectAttributes
     * @return respective web-page
     */
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

    /**
     * Deletes the provided return transaction
     * @param id
     * @param redirectAttributes
     * @return
     */
    @DeleteMapping("/{id}")
    public String deleteReturnTransaction(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = bookTransactionService.deleteEntityById(id);
        redirectAttributes.addFlashAttribute("errorMessage", responseDto.getMessage());
        return "redirect:/return";
    }
}
