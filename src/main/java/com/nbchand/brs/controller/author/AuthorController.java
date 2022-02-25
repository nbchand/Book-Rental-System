package com.nbchand.brs.controller.author;

import com.nbchand.brs.dto.author.AuthorDto;
import com.nbchand.brs.entity.author.Author;
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
        authorService.saveEntity(authorDto);
        redirectAttributes.addFlashAttribute("errorMessage", "Author added successfully");
        return "redirect:/author";
    }

    @DeleteMapping("/{id}")
    public String deleteAuthor(@PathVariable("id") Integer id,
                               RedirectAttributes redirectAttributes) {
        try {
            AuthorDto authorDto = authorService.findEntityById(id);
            authorService.deleteEntityById(id);
            redirectAttributes.addFlashAttribute("errorMessage", "Author deleted successfully");
            return "redirect:/author";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Author could not be deleted");
            return "redirect:/author";
        }
    }

    @GetMapping("/edit/{id}")
    public String displayAuthorEditPage(@PathVariable("id") Integer id, Model model,
                                        RedirectAttributes redirectAttributes) {
        try {
            AuthorDto authorDto = authorService.findEntityById(id);
            model.addAttribute("authorDto", authorDto);
            return "author/authorForm";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Author not found");
            return "redirect:/author";
        }
    }

    @PutMapping("/{id}")
    public String updateAuthor(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("authorDto") AuthorDto authorDto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            //just to check whether author with given id is present or not
            AuthorDto authorDto1 = authorService.findEntityById(id);
            authorDto.setId(id);
            if(bindingResult.hasErrors()){
                model.addAttribute("authorDto", authorDto);
                return "author/authorForm";
            }
            authorService.saveEntity(authorDto);
            redirectAttributes.addFlashAttribute("errorMessage", "Author updated successfully");
            return "redirect:/author";
        } catch (Exception exception) {
            //if author is not present this code executes
            redirectAttributes.addFlashAttribute("errorMessage", "Author not found");
            return "redirect:/author";
        }
    }
}
