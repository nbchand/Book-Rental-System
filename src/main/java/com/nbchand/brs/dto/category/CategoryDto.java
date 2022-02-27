package com.nbchand.brs.dto.category;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
}
