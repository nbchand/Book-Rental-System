package com.nbchand.brs.entity.category;

import lombok.*;

import javax.persistence.*;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-24
 */
@Entity
@Table(name = "tbl_category",
        uniqueConstraints =
        @UniqueConstraint(name = "UK_Category_Name", columnNames = {"name"})
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Category_SEQ_GEN")
    @SequenceGenerator(name = "Category_SEQ_GEN", sequenceName = "Category_SEQ", allocationSize = 1)
    private Integer id;

    private String name;

    private String description;
}
