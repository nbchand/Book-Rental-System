package com.nbchand.brs.dto.member;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-26
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberDto {
    private Integer id;

    @NotEmpty(message = "Member name must not be empty")
    @Size(max = 80, message = "Member name can only be upto 80 characters")
    private String name;

    @NotEmpty(message = "Member email must not be empty")
    @Email(message = "Member email invalid")
    private String email;

    @NotEmpty(message = "Member mobile number must not be empty")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be of 10 digits")
    private String mobileNumber;

    @NotEmpty(message = "Member address must not be empty")
    @Size(max = 80, message = "Member address can only be upto 80 characters")
    private String address;
}
