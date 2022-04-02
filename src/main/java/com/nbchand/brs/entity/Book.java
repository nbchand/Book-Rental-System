package com.nbchand.brs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-24
 */
@Entity
@Table(name = "tbl_book", uniqueConstraints = {
        @UniqueConstraint(name = "UK_Book_Name", columnNames = {"name"}),
        @UniqueConstraint(name = "UK_Book_Isbn", columnNames = {"isbn"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "Book_SEQ_GEN")
    @SequenceGenerator(name = "Book_SEQ_GEN",
            sequenceName = "Book_SEQ",
            allocationSize = 1)
    private Integer id;

    private String name;

    private Integer numberOfPages;

    private String isbn;

    private Double rating;

    private Integer stockCount;

    private Date publishedDate;

    private String photo;

    @ManyToOne
    @JoinColumn(name = "category_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "FK_Book_Photo"))
    private Category category;

    @ManyToMany
    @JoinTable(name = "tbl_book_author",
                joinColumns = @JoinColumn(name = "book_id"),
                inverseJoinColumns = @JoinColumn(name = "author_id"),
                foreignKey = @ForeignKey(name = "FK_Book"),
                inverseForeignKey = @ForeignKey(name = "FK_Author"))
    private List<Author> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    List<BookTransaction> bookTransactions;
}
