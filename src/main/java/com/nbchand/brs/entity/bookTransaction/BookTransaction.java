package com.nbchand.brs.entity.bookTransaction;

import com.nbchand.brs.entity.book.Book;
import com.nbchand.brs.entity.member.Member;
import com.nbchand.brs.enums.RentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-24
 */
@Entity
@Table(name = "tbl_book_transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "Member_SEQ_GEN")
    @SequenceGenerator(name = "Member_SEQ_GEN",
            sequenceName = "Member_SEQ",
            allocationSize = 1)
    private Integer id;

    private String code;

    private Date fromDate;

    private Date toDate;

    private Boolean rentStatus;

    private Boolean activeClosed;

    private RentType rentType;

    @ManyToOne
    @JoinColumn(name = "book_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Book_Transaction_Book"))
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Book_Transaction_Member"))
    private Member member;
}
