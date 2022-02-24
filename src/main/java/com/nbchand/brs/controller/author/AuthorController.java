package com.nbchand.brs.controller.author;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-23
 */
@Controller
@RequestMapping("/author")
public class AuthorController {
    @GetMapping
    public String displayAuthorLandingPage(){
        return "author/authorLanding";
    }
    @GetMapping("/add-author")
    public String displayAuthorForm(){
        return "author/authorForm";
    }

}
