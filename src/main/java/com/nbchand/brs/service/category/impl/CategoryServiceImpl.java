package com.nbchand.brs.service.category.impl;

import com.nbchand.brs.dto.CategoryDto;
import com.nbchand.brs.dto.ResponseDto;
import com.nbchand.brs.entity.Category;
import com.nbchand.brs.repository.CategoryRepo;
import com.nbchand.brs.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-26
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    /**
     * Saves category to the database
     * @param categoryDto
     * @return saving response
     */
    @Override
    public ResponseDto saveEntity(CategoryDto categoryDto) {
        Category category = Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .description(categoryDto.getDescription())
                .build();

        try {
            categoryRepo.save(category);
            return ResponseDto.builder()
                    .status(true)
                    .build();
        }
        //category may not be saved if category name is already present
        catch (Exception exception) {
            return ResponseDto.builder()
                    .status(false)
                    .message("Category name already taken")
                    .build();
        }
    }

    /**
     * Finds all the categories form the database
     * @return
     */
    @Override
    public List<CategoryDto> findAllEntities() {
        List<Category> categories = categoryRepo.findAll();
        List<CategoryDto> categoryDtoList = CategoryDto.CategoriesToCategoryDtos(categories);
        return categoryDtoList;
    }

    /**
     * Finds a single category by id
     * @param id
     * @return response with category or error message
     */
    @Override
    public ResponseDto findEntityById(Integer id) {
        try {
            Category category = categoryRepo.getById(id);
            CategoryDto categoryDto = new CategoryDto(category);
            return ResponseDto.builder()
                    .status(true)
                    .categoryDto(categoryDto)
                    .build();
        } catch (Exception e) {
            return ResponseDto.builder()
                    .status(false)
                    .message("Category not found")
                    .build();
        }    }

    /**
     * Deletes category from the database on the basis of id
     * @param id
     * @return response
     */
    @Override
    public ResponseDto deleteEntityById(Integer id) {
        try {
            categoryRepo.deleteById(id);
            return ResponseDto.builder()
                    .status(true)
                    .message("Category deleted successfully")
                    .build();
        } catch (Exception exception) {
            return ResponseDto.builder()
                    .status(false)
                    .message("Category not found")
                    .build();
        }    }
}
