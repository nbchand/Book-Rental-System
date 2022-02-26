package com.nbchand.brs.entity.member;

import lombok.*;

import javax.persistence.*;
/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-24
 */
@Entity
@Table(name = "tbl_member", uniqueConstraints = {
        @UniqueConstraint(name = "UK_Member_Email", columnNames = {"email"}),
        @UniqueConstraint(name = "UK_Member_Mobile", columnNames = {"mobileNumber"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
