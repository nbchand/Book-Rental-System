package com.nbchand.brs.controller.author;

import com.nbchand.brs.dto.author.AuthorDto;
import com.nbchand.brs.service.author.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String displayAuthorLandingPage(Model model){
        model.addAttribute("authorDtoList", authorService.findAllEntities());
        return "author/authorLanding";
    }

    @GetMapping("/add-author")
    public String displayAuthorForm(Model model){
        model.addAttribute("authorDto", new AuthorDto());
        return "author/authorForm";
    }

    @PostMapping
    public String addAuthor(@Valid @ModelAttribute("authorDto") AuthorDto authorDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "redirect:/author";
        }
        authorService.saveEntity(authorDto);
        return "redirect:/author";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteAuthor(@PathVariable("id") Integer id){
        System.out.println("Hello");
        AuthorDto authorDto = authorService.findEntityById(id);
        if(authorDto == null){
            return "failed";
        }
        authorService.deleteEntityById(id);
        return "success";
    }
}
