package com.nbchand.brs.controller;

import com.nbchand.brs.dto.AuthorDto;
import com.nbchand.brs.dto.ResponseDto;
import com.nbchand.brs.service.author.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-23
 */
@Controller
@RequestMapping("/author")
public class AuthorController {

    private AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public String displayAuthorLandingPage(Model model) {
        model.addAttribute("authorDtoList", authorService.findAllEntities());
        return "author/authorLanding";
    }

    @GetMapping("/add-author")
    public String displayAuthorForm(Model model) {
        model.addAttribute("authorDto", AuthorDto.builder().id(null).build());
        return "author/authorForm";
    }

    @PostMapping
    public String addAuthor(@Valid @ModelAttribute("authorDto") AuthorDto authorDto,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authorDto", authorDto);
            return "author/authorForm";
        }
        ResponseDto responseDto = authorService.saveEntity(authorDto);
        if (responseDto.isStatus()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Author added successfully");
            return "redirect:/author";
        }
        model.addAttribute("errorMessage", responseDto.getMessage());
        return "author/authorForm";
    }

    @DeleteMapping("/{id}")
    public String deleteAuthor(@PathVariable("id") Integer id,
                               RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = authorService.deleteEntityById(id);
        redirectAttributes.addFlashAttribute("errorMessage", responseDto.getMessage());
        return "redirect:/author";
    }

    @GetMapping("/edit/{id}")
    public String displayAuthorEditPage(@PathVariable("id") Integer id, Model model,
                                        RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = authorService.findEntityById(id);
        if (responseDto.isStatus()) {
            model.addAttribute("authorDto", responseDto.getAuthorDto());
            return "author/authorForm";
        }
        redirectAttributes.addFlashAttribute("errorMessage", responseDto.getMessage());
        return "redirect:/author";
    }

    @PutMapping("/{id}")
    public String updateAuthor(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("authorDto") AuthorDto authorDto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        authorDto.setId(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("authorDto", authorDto);
            return "author/authorForm";
        }
        ResponseDto responseDto = authorService.saveEntity(authorDto);
        if (responseDto.isStatus()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Author updated successfully");
            return "redirect:/author";
        }
        model.addAttribute("errorMessage", responseDto.getMessage());
        model.addAttribute("authorDto", authorDto);
        return "author/authorForm";
    }
}
