package com.nbchand.brs.repository.author;

import com.nbchand.brs.entity.author.Author;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-24
 */
public interface AuthorRepo extends JpaRepository<Author, Integer> {
}
