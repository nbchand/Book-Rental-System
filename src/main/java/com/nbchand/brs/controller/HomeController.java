package com.nbchand.brs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-23
 */
@Controller
@RequestMapping("/")
public class HomeController {
    /**
     * Displays home-page
     * @return respective web-page
     */
    @GetMapping
    public String displayHome(){
        return "homePage";
    }
}
