package com.nbchand.brs.repository.book;

import com.nbchand.brs.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-27
 */
public interface BookRepo extends JpaRepository<Book, Integer> {
}
