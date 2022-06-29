package kr.co.community.controller;

import kr.co.community.model.Member;
import kr.co.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    

    @GetMapping("/login")
    public String login(){
        return "app/members/login";
    }


    @PostMapping
    public String create(@ModelAttribute Member member){
        memberService.saveMember(member);
        return "redirect:/";
    }
}
