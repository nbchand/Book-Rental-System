package com.nbchand.brs.repository.bookTransaction;

import com.nbchand.brs.entity.bookTransaction.BookTransaction;
import com.nbchand.brs.enums.RentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-03-02
 */
public interface BookTransactionRepo extends JpaRepository<BookTransaction, Integer> {
    List<BookTransaction> findAllByRentType(RentType rentType);
}
