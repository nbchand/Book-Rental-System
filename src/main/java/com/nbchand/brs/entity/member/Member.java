package com.nbchand.brs.entity.member;

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
@Table(name = "tbl_member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "Member_SEQ_GEN")
    @SequenceGenerator(name = "Member_SEQ_GEN",
            sequenceName = "Member_SEQ",
            allocationSize = 1)
    private Integer id;

    private String name;

    private String email;

    private String mobileNumber;

    private String address;
}
