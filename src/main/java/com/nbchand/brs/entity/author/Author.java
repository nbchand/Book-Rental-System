package com.nbchand.brs.entity.author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-24
 */
@Entity
@Table(name = "tbl_author")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Author_SEQ_GEN")
    @SequenceGenerator(name = "Author_SEQ_GEN", sequenceName = "Author_SEQ", allocationSize = 1)
    private Integer id;

    private String name;

    private String email;

    private String mobileNumber;
}
