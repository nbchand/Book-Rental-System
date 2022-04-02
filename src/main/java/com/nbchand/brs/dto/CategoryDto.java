package com.nbchand.brs.dto;

import com.nbchand.brs.entity.Category;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-26
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private Integer id;

    @NotEmpty(message = "Category name must not be empty")
    @Size(max = 80, message = "Category name can only be upto 80 characters")
    private String name;

    @NotEmpty(message = "Category description must not be empty")
    @Size(max = 250, message = "Category description can only be upto 250 characters")
    private String description;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
    }

    public static List<CategoryDto> CategoriesToCategoryDtos(List<Category> categoryList) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for(Category category: categoryList) {
            categoryDtos.add(new CategoryDto(category));
        }
        return categoryDtos;
    }
}
