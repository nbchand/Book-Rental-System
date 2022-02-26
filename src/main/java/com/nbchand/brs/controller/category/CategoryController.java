package com.nbchand.brs.controller.category;

import com.nbchand.brs.dto.category.CategoryDto;
import com.nbchand.brs.dto.response.ResponseDto;
import com.nbchand.brs.service.category.CategoryService;
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
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String displayCategoryLandingPage(Model model) {
        model.addAttribute("categoryDtoList", categoryService.findAllEntities());
        return "category/categoryLanding";
    }

    @GetMapping("/add-category")
    public String displayCategoryForm(Model model) {
        model.addAttribute("categoryDto", CategoryDto.builder().id(null).build());
        return "category/categoryForm";
    }

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

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable("id") Integer id,
                               RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = categoryService.deleteEntityById(id);
        redirectAttributes.addFlashAttribute("errorMessage", responseDto.getMessage());
        return "redirect:/category";
    }

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
