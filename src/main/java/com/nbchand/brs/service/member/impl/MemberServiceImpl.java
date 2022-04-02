package com.nbchand.brs.service.member.impl;

import com.nbchand.brs.dto.MemberDto;
import com.nbchand.brs.dto.ResponseDto;
import com.nbchand.brs.entity.Member;
import com.nbchand.brs.repository.MemberRepo;
import com.nbchand.brs.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-26
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepo memberRepo;

    @Override
    public ResponseDto saveEntity(MemberDto memberDto) {
        Member member = Member.builder()
                .email(memberDto.getEmail())
                .mobileNumber(memberDto.getMobileNumber())
                .name(memberDto.getName())
                .address(memberDto.getAddress())
                .build();

        if (memberDto.getId() != null) {
            member.setId(memberDto.getId());
        }

        try {
            memberRepo.save(member);
            return ResponseDto.builder()
                    .status(true)
                    .build();
        } catch (Exception exception) {
            if (exception.getMessage().contains("mobile")) {
                return ResponseDto.builder()
                        .status(false)
                        .message("Mobile number already in use")
                        .build();
            } else {
                return ResponseDto.builder()
                        .status(false)
                        .message("Email address already in use")
                        .build();
            }
        }
    }

    @Override
    public List<MemberDto> findAllEntities() {
        List<Member> members = memberRepo.findAll();
        List<MemberDto> memberDtoList = MemberDto.membersToMemberDtos(members);

        return memberDtoList;
    }

    @Override
    public ResponseDto findEntityById(Integer id) {
        try {
            Member member = memberRepo.getById(id);
            MemberDto memberDto = new MemberDto(member);
            return ResponseDto.builder()
                    .status(true)
                    .memberDto(memberDto)
                    .build();
        } catch (Exception e) {
            return ResponseDto.builder()
                    .status(false)
                    .message("Member not found")
                    .build();
        }    }

    @Override
    public ResponseDto deleteEntityById(Integer id) {
        try {
            memberRepo.deleteById(id);
            return ResponseDto.builder()
                    .status(true)
                    .message("Member deleted successfully")
                    .build();
        } catch (Exception exception) {
            return ResponseDto.builder()
                    .status(false)
                    .message("Member not found")
                    .build();
        }    }
}
