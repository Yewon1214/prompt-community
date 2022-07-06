package kr.co.community.controller;

import kr.co.community.model.Comment;
import kr.co.community.model.Member;
import kr.co.community.model.Post;
import kr.co.community.service.CommentService;
import kr.co.community.service.MemberService;
import kr.co.community.service.PostService;
import kr.co.community.vo.CommentVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    @PostMapping("")
    public String create(@ModelAttribute CommentVo commentvo, Principal principal){
        Post post = postService.findById(commentvo.getPostId());
        Member currentMember = memberService.findByEmail(principal.getName());
        Comment comment = new Comment(commentvo);
        comment.setMember(currentMember);
        comment.setPost(post);

        commentService.save(comment);
        return "redirect:/posts/"+post.getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) throws Exception {
        Comment comment = commentService.findById(id);
        Post post = postService.findById(comment.getPost().getId());
        Member currentMember = memberService.findByEmail(principal.getName());

        if(!comment.isWriter(currentMember)){
            throw new Exception("삭제 권한이 없습니다.");
        }

        commentService.delete(comment);
        return "redirect:/posts/" + post.getId();
    }

}
