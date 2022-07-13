package kr.co.community.controller;

import kr.co.community.model.Comment;
import kr.co.community.model.Member;
import kr.co.community.model.Post;
import kr.co.community.service.CommentService;
import kr.co.community.service.MemberService;
import kr.co.community.service.PostService;
import kr.co.community.vo.CommentVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    @PostMapping("")
    public String create(Principal principal, Long id, @Valid @ModelAttribute CommentVo commentVo) throws Exception {

        Post post = postService.findById(id);
        Member currentMember = memberService.findByEmail(principal.getName());
        Comment comment = new Comment(commentVo);
        comment.setMember(currentMember);
        comment.setPost(post);

        commentService.saveComment(comment);
        return "redirect:/posts/"+post.getId();
    }

    @PutMapping("")
    public String update(Long id, @ModelAttribute CommentVo commentVo, Principal principal) throws Exception {
        Comment comment = commentService.findById(id);
        Member currentMember = memberService.findByEmail(principal.getName());
        if(!comment.isWriter(currentMember)){
            throw new Exception("수정 권한이 없습니다.");
        }

        comment.update(commentVo.getContent());
        commentService.saveComment(comment);

        return "redirect:/posts/" + comment.getPost().getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) throws Exception {
        Comment comment = commentService.findById(id);
        Member member = memberService.findByEmail(principal.getName());

        if(comment.getMember() != member) {
            throw new Exception("삭제 권한이 없습니다.");
        }

        Post post = postService.findById(comment.getPost().getId());

        commentService.deleteComment(comment);
        return "redirect:/posts/" + post.getId();
    }

}
