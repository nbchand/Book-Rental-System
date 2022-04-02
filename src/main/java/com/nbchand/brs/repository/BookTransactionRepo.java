package com.nbchand.brs.repository;

import com.nbchand.brs.entity.BookTransaction;
import com.nbchand.brs.enums.RentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-03-02
 */
public interface BookTransactionRepo extends JpaRepository<BookTransaction, Integer> {
    List<BookTransaction> findAllByRentType(RentType rentType);

    @Query(value = "SELECT code from tbl_book_transaction", nativeQuery = true)
    List<String> findAllTransactionCode();

    BookTransaction findBookTransactionByCode(String code);
}
