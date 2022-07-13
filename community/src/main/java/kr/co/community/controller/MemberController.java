package kr.co.community.controller;

import kr.co.community.model.Comment;
import kr.co.community.model.Pagination;
import kr.co.community.model.Post;
import kr.co.community.service.CommentService;
import kr.co.community.service.PostService;
import kr.co.community.vo.MemberVo;
import kr.co.community.model.Member;
import kr.co.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.procedure.internal.PostgresCallableStatementSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/login")
    public String login() {
        return "app/members/login";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("memberVo", new MemberVo());
        return "app/members/new";
    }

    @PostMapping("")
    public String save(Model model, @Valid @ModelAttribute MemberVo memberVo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "app/members/new";
        }

        Member member = memberVo.toEntity();

        if (memberService.checkEmailDuplication(member)) {
            model.addAttribute("error", "이미 존재하는 이메일입니다.");
            return "app/members/new";
        }

        memberService.saveMember(member);
        return "redirect:/member/login";
    }

    @GetMapping("/mypage")
    public String showMyPage(Model model, Principal principal) throws Exception {
        if (principal == null) {
            throw new Exception("접근 권한이 없습니다.");
        }
        Member member = memberService.findByEmail(principal.getName());

        model.addAttribute("member", member);
        return "app/mypage/index";
    }

    @GetMapping("/mypage/myposts")
    public String showMyPost(Model model, Principal principal, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        if (principal == null) {
            throw new Exception("접근 권한이 없습니다.");
        }
        Member member = memberService.findByEmail(principal.getName());

        Page<Post> postPage = postService.findByMember(member, pageable);
        Pagination pagination = new Pagination(pageable);
        pagination.setTotalElements(postPage.getTotalElements());
        pagination.setTotalPages(postPage.getTotalPages());

        String[] orderBy = String.valueOf(pageable.getSort()).split(":");
        model.addAttribute("postPage", postPage.getContent());
        model.addAttribute("orderBy", orderBy[0]);
        model.addAttribute("pagination", pagination);

        return "app/mypage/myposts";
    }

    @GetMapping("/mypage/mycomments")
    public String showMyComments(Model model, Principal principal, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
        if (principal == null) {
            throw new Exception("접근 권한이 없습니다.");
        }
        Member member = memberService.findByEmail(principal.getName());

        Page<Comment> commentPage = commentService.findByMember(member, pageable);
        Pagination pagination = new Pagination(pageable);
        pagination.setTotalPages(commentPage.getTotalPages());
        pagination.setTotalElements(commentPage.getTotalElements());

        model.addAttribute("commentPage", commentPage);
        model.addAttribute("pagination", pagination);

        return "app/mypage/mycomments";
    }

    @GetMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id, Model model, Principal principal) throws Exception {
        if (principal == null) {
            throw new Exception("접근 권한이 없습니다.");
        }
        Member member = memberService.findById(id);
        if(Objects.isNull(member)){
            throw new Exception("없는 멤버입니다.");
        }
        model.addAttribute("memberVo", member);
        return "app/members/edit";
    }

    @PutMapping("")
    public String update(Member member, Principal principal) throws Exception {
        Member currentMember = memberService.findByEmail(principal.getName());

        if(currentMember.getId() == member.getId()){
            throw new Exception("같은 유저가 아닙니다.");
        }

        currentMember.update(member.getPassword());
        memberService.saveMember(currentMember);

        return "app/mypage/index";
    }

    @GetMapping("/checkpwdview")
    public String checkPwdView(Principal principal, Model model) throws Exception {
        if (principal == null) {
            throw new Exception("잘못된 접근입니다.");
        }
        Member member = memberService.findByEmail(principal.getName());
        model.addAttribute("id", member.getId());
        return "app/members/checkpwd";
    }

    @GetMapping("/checkpwd")
    @ResponseBody
    public boolean checkPwd(@RequestParam String checkPassword, Principal principal, Model model) throws Exception {
        if (principal == null) {
            throw new Exception("잘못된 접근입니다.");
        }
        Member member = memberService.findByEmail(principal.getName());

        return memberService.checkPassword(member, checkPassword);
    }

}
