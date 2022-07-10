package kr.co.community.controller;

import kr.co.community.vo.MemberVo;
import kr.co.community.model.Member;
import kr.co.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String login(){
        return "app/members/login";
    }

    @GetMapping("/new")
    public String create(Model model){
        model.addAttribute("memberVo", new MemberVo());
        return "app/members/new";
    }

    @PostMapping("")
    public String save(@Valid @ModelAttribute MemberVo memberVo, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "app/members/new";
        }
        Member member = memberVo.toEntity();

        memberService.saveMember(member);
        return "redirect:/member/login";
    }
}
