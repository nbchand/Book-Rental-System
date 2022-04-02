package com.nbchand.brs.controller;

import com.nbchand.brs.dto.CategoryDto;
import com.nbchand.brs.dto.ResponseDto;
import com.nbchand.brs.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-26
 */
@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Displays category landing
     * @param model
     * @return respective web-page
     */
    @GetMapping
    public String displayCategoryLandingPage(Model model) {
        model.addAttribute("categoryDtoList", categoryService.findAllEntities());
        return "category/categoryLanding";
    }

    /**
     * Displays category form
     * @param model
     * @return respective web-page
     */
    @GetMapping("/add-category")
    public String displayCategoryForm(Model model) {
        model.addAttribute("categoryDto", CategoryDto.builder().id(null).build());
        return "category/categoryForm";
    }

    /**
     * Creates category
     * @param categoryDto
     * @param bindingResult
     * @param model
     * @param redirectAttributes
     * @return respective web-page
     */
    @PostMapping
    public String addCategory(@Valid @ModelAttribute("categoryDto") CategoryDto categoryDto,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryDto", categoryDto);
            return "category/categoryForm";
        }
        ResponseDto responseDto = categoryService.saveEntity(categoryDto);
        if (responseDto.isStatus()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category added successfully");
            return "redirect:/category";
        }
        model.addAttribute("errorMessage", responseDto.getMessage());
        return "category/categoryForm";
    }

    /**
     * Deletes category by its id
     * @param id
     * @param redirectAttributes
     * @return respective web-page
     */
    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable("id") Integer id,
                               RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = categoryService.deleteEntityById(id);
        redirectAttributes.addFlashAttribute("errorMessage", responseDto.getMessage());
        return "redirect:/category";
    }

    /**
     * Display category form for editing
     * @param id
     * @param model
     * @param redirectAttributes
     * @return respective web-page
     */
    @GetMapping("/edit/{id}")
    public String displayCategoryEditPage(@PathVariable("id") Integer id, Model model,
                                        RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = categoryService.findEntityById(id);
        if (responseDto.isStatus()) {
            model.addAttribute("categoryDto", responseDto.getCategoryDto());
            return "category/categoryForm";
        }
        redirectAttributes.addFlashAttribute("errorMessage", responseDto.getMessage());
        return "redirect:/category";
    }

    /**
     * Updates category after editing
     * @param id
     * @param categoryDto
     * @param bindingResult
     * @param model
     * @param redirectAttributes
     * @return respective web-page
     */
    @PutMapping("/{id}")
    public String updateCategory(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("categoryDto") CategoryDto categoryDto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        categoryDto.setId(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryDto", categoryDto);
            return "category/categoryForm";
        }
        ResponseDto responseDto = categoryService.saveEntity(categoryDto);
        if (responseDto.isStatus()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category updated successfully");
            return "redirect:/category";
        }
        model.addAttribute("errorMessage", responseDto.getMessage());
        model.addAttribute("categoryDto", categoryDto);
        return "category/categoryForm";
    }
}
