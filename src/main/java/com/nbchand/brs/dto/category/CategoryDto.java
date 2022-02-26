package com.nbchand.brs.dto.category;

import lombok.*;

import javax.validation.constraints.NotEmpty;

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
@ToString
public class CategoryDto {

    private Integer id;

    @NotEmpty(message = "Category name must not be empty")
    private String name;

    @NotEmpty(message = "Category description must not be empty")
    private String description;
}
