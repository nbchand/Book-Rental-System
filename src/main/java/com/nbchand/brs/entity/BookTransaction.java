package com.nbchand.brs.entity;

import com.nbchand.brs.enums.RentType;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-24
 */
@Entity
@Table(name = "tbl_book_transaction", uniqueConstraints = {
        @UniqueConstraint(name = "UK_Book_Transaction_Code" , columnNames = "code")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    private Date returnedDate;

    @Enumerated(value = EnumType.STRING)
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
