package kr.co.community.controller;

import kr.co.community.model.Comment;
import kr.co.community.model.Pagination;
import kr.co.community.model.Post;
import kr.co.community.model.helper.CurrentUser;
import kr.co.community.service.CommentService;
import kr.co.community.service.PostService;
import kr.co.community.vo.MemberVo;
import kr.co.community.model.Member;
import kr.co.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public String showMyPage(Model model, @CurrentUser Member currentMember) throws Exception {
        int commentCnt = commentService.countByMemberId(currentMember.getId());
        int postCnt = postService.countByMemberId(currentMember.getId());
        model.addAttribute("member", currentMember);
        model.addAttribute("commentCnt", commentCnt);
        model.addAttribute("postCnt", postCnt);
        return "app/mypage/index";
    }

    @GetMapping("/mypage/myposts")
    public String showMyPost(Model model, @CurrentUser Member currentMember, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {

        Page<Post> postPage = postService.findByMember(currentMember, pageable);
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
    public String showMyComments(Model model, @CurrentUser Member currentMember, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {

        Page<Comment> commentPage = commentService.findByMember(currentMember, pageable);
        Pagination pagination = new Pagination(pageable);
        pagination.setTotalPages(commentPage.getTotalPages());
        pagination.setTotalElements(commentPage.getTotalElements());

        model.addAttribute("commentPage", commentPage);
        model.addAttribute("pagination", pagination);

        return "app/mypage/mycomments";
    }

    @GetMapping("/edit/{id}")
    public String update(@PathVariable("id") Long id, Model model, @CurrentUser Member currentMember) throws Exception {
        if(currentMember == null){
            throw new Exception("로그인되어 있지 않습니다.");
        }

        Member member = memberService.findById(id);
        if(member == null){
            throw new Exception("수정할 수 없습니다.");
        }
        if(member.getId() != currentMember.getId()){
            throw new Exception("수정할 수 없습니다.");
        }

        model.addAttribute("memberVo", member);
        return "app/members/edit";
    }

    @PutMapping("")
    @ResponseBody
    public boolean update(@RequestBody Member member, @CurrentUser Member currentMember) {

        if(currentMember.getId() != member.getId()){
            return false;
        }

        memberService.passwordUpdate(member.getPassword(), currentMember);

        return true;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public boolean delete(@PathVariable Long id, @CurrentUser Member currentMember){
        if(currentMember == null){
            return false;
        }

        if(memberService.findById(id)==null){
            return false;
        }

        if(currentMember.getId() != id){
            return false;
        }

        commentService.deleteByMember(id);
        postService.deleteByMember(id);
        memberService.deleteById(id);
        return true;
    }

    @GetMapping("/checkpwdview")
    public String checkPwdView(@CurrentUser Member currentMember, Model model) throws Exception {
        if (currentMember == null) {
            throw new Exception("잘못된 접근입니다.");
        }

        model.addAttribute("id", currentMember.getId());
        return "app/members/checkpwd";
    }

    @GetMapping("/check-pw")
    @ResponseBody
    public boolean checkPwd(@RequestParam String checkPassword, @CurrentUser Member currentMember) throws Exception {
        if (currentMember == null) {
            return false;
        }

        return memberService.checkPassword(currentMember, checkPassword);
    }

}
