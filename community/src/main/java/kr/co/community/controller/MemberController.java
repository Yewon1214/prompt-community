package kr.co.community.controller;

import kr.co.community.vo.RegisterVo;
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
    public String register(Model model){
        model.addAttribute("registerDto", new RegisterVo());
        return "app/members/new";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute RegisterVo registerVo, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "app/members/new";
        }
        Member member = registerVo.toEntity();

        memberService.saveMember(member);
        return "redirect:/member/login";
    }
}
