package com.nbchand.brs.controller;

import com.nbchand.brs.dto.MemberDto;
import com.nbchand.brs.dto.ResponseDto;
import com.nbchand.brs.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-26
 */
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * Displays member landing
     * @param model
     * @return respective view
     */
    @GetMapping
    public String displayMemberLandingPage(Model model) {
        model.addAttribute("memberDtoList", memberService.findAllEntities());
        return "member/memberLanding";
    }

    /**
     * Displays memeber form
     * @param model
     * @return respective view
     */
    @GetMapping("/add-member")
    public String displayMemberForm(Model model) {
        model.addAttribute("memberDto", MemberDto.builder().id(null).build());
        return "member/memberForm";
    }

    /**
     * Creates member
     * @param memberDto
     * @param bindingResult
     * @param model
     * @param redirectAttributes
     * @return respective view
     */
    @PostMapping
    public String addMember(@Valid @ModelAttribute("memberDto") MemberDto memberDto,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("memberDto", memberDto);
            return "member/memberForm";
        }
        ResponseDto responseDto = memberService.saveEntity(memberDto);
        if (responseDto.isStatus()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Member added successfully");
            return "redirect:/member";
        }
        model.addAttribute("errorMessage", responseDto.getMessage());
        return "member/memberForm";
    }

    /**
     * Deletes member by id
     * @param id
     * @param redirectAttributes
     * @return respective view
     */
    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable("id") Integer id,
                               RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = memberService.deleteEntityById(id);
        redirectAttributes.addFlashAttribute("errorMessage", responseDto.getMessage());
        return "redirect:/member";
    }

    /**
     * Displays member form for editing
     * @param id
     * @param model
     * @param redirectAttributes
     * @return respective view
     */
    @GetMapping("/edit/{id}")
    public String displayMemberEditPage(@PathVariable("id") Integer id, Model model,
                                        RedirectAttributes redirectAttributes) {
        ResponseDto responseDto = memberService.findEntityById(id);
        if (responseDto.isStatus()) {
            model.addAttribute("memberDto", responseDto.getMemberDto());
            return "member/memberForm";
        }
        redirectAttributes.addFlashAttribute("errorMessage", responseDto.getMessage());
        return "redirect:/member";
    }

    /**
     * Updates member after editing
     * @param id
     * @param memberDto
     * @param bindingResult
     * @param model
     * @param redirectAttributes
     * @return respective view
     */
    @PutMapping("/{id}")
    public String updateMember(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("memberDto") MemberDto memberDto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        memberDto.setId(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("memberDto", memberDto);
            return "member/memberForm";
        }
        ResponseDto responseDto = memberService.saveEntity(memberDto);
        if (responseDto.isStatus()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Member updated successfully");
            return "redirect:/member";
        }
        model.addAttribute("errorMessage", responseDto.getMessage());
        model.addAttribute("memberDto", memberDto);
        return "member/memberForm";
    }
}
