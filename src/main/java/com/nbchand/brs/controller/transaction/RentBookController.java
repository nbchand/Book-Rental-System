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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
@RequiredArgsConstructor
public class RentBookController {

    private final BookTransactionService bookTransactionService;
    private final MemberService memberService;
    private final BookService bookService;

    /**
     * Displays landing page for book renting
     * @param model
     * @return respective web-page
     */
    @GetMapping
    public String displayRentLanding(Model model) {
        model.addAttribute("bookTransactionDtoList", bookTransactionService.findTransactionsByRentType(RentType.RENT));
        return "rent/rentLanding";
    }

    /**
     * Displays book renting form
     * @param model
     * @return respective web-page
     */
    @GetMapping("/create")
    public String displayRentForm(Model model) {
        model.addAttribute("memberDtoList", memberService.findAllEntities());
        model.addAttribute("bookDtoList", bookService.findAllBooksInStock());
        model.addAttribute("bookTransactionDto", BookTransactionDto.builder().id(null).build());
        return "rent/rentForm";
    }

    /**
     * Creates book renting transaction
     * @param bookTransactionDto
     * @param bindingResult
     * @param model
     * @param redirectAttributes
     * @return respective web-page
     */
    @PostMapping
    public String addRent(@Valid @ModelAttribute BookTransactionDto bookTransactionDto,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("memberDtoList", memberService.findAllEntities());
            model.addAttribute("bookDtoList", bookService.findAllBooksInStock());
            model.addAttribute("bookTransactionDto", bookTransactionDto);
            return "rent/rentForm";
        }
        bookTransactionDto.setFromDate(new Date());
        bookTransactionDto.setRentType(RentType.RENT);
        ResponseDto completeResponse = bookTransactionService.makeBookTransactionDtoComplete(bookTransactionDto);
        if (completeResponse.isStatus()) {
            ResponseDto saveResponse = bookTransactionService.saveEntity(completeResponse.getBookTransactionDto());
            if (saveResponse.isStatus()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Book transaction added successfully");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", saveResponse.getMessage());
            }
            return "redirect:/rent";
        }
        redirectAttributes.addFlashAttribute("errorMessage", completeResponse.getMessage());
        return "redirect:/rent";
    }

    /**
     * Displays transaction form for editing purpose
     * @param id
     * @param model
     * @param redirectAttributes
     * @return respective web-page
     */
    @GetMapping("/edit/{id}")
    public String displayEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        ResponseDto transactionResponse = bookTransactionService.findEntityById(id);
        if (transactionResponse.isStatus()) {
            model.addAttribute("memberDtoList", memberService.findAllEntities());
            model.addAttribute("bookDtoList", bookService.findAllBooksInStock());
            model.addAttribute("bookTransactionDto", transactionResponse.getBookTransactionDto());
            return "rent/rentForm";
        }
        redirectAttributes.addFlashAttribute("errorMessage", transactionResponse.getMessage());
        return "redirect:/rent";
    }

    /**
     * Updates the book rent transaction
     * @param id
     * @param bookTransactionDto
     * @param bindingResult
     * @param model
     * @param redirectAttributes
     * @return
     */
    @PutMapping("/{id}")
    public String updateRent(@PathVariable("id") Integer id,
                             @Valid @ModelAttribute BookTransactionDto bookTransactionDto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = bookTransactionService.findEntityById(id);
        if(!responseDto.isStatus() || responseDto.getBookTransactionDto().getRentType().equals(RentType.RETURN)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Book transaction not found");
            return "redirect:/rent";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("memberDtoList", memberService.findAllEntities());
            model.addAttribute("bookDtoList", bookService.findAllBooksInStock());
            model.addAttribute("bookTransactionDto", bookTransactionDto);
            return "rent/rentForm";
        }
        bookTransactionDto.setFromDate(responseDto.getBookTransactionDto().getFromDate());
        bookTransactionDto.setRentType(RentType.RENT);
        bookTransactionDto.setId(id);
        ResponseDto completeResponse = bookTransactionService.makeBookTransactionDtoComplete(bookTransactionDto);
        if (completeResponse.isStatus()) {
            ResponseDto saveResponse = bookTransactionService.saveEntity(completeResponse.getBookTransactionDto());
            if (saveResponse.isStatus()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Book transaction updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", saveResponse.getMessage());
            }
            return "redirect:/rent";
        }
        redirectAttributes.addFlashAttribute("errorMessage", completeResponse.getMessage());
        return "redirect:/rent";
    }

    /**
     * Deletes the transaction of provided id
     * @param id
     * @param redirectAttributes
     * @return respective web-page
     */
    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = bookTransactionService.deleteEntityById(id);
        redirectAttributes.addFlashAttribute("errorMessage", responseDto.getMessage());
        return "redirect:/rent";
    }
}
