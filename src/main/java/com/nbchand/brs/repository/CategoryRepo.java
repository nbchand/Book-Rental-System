package com.nbchand.brs.repository;

import com.nbchand.brs.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-26
 */
public interface CategoryRepo extends JpaRepository<Category, Integer> {
}
