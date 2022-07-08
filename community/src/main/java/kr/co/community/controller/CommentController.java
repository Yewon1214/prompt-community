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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import javax.validation.Valid;
import java.security.Principal;

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

        commentService.save(comment);
        return "redirect:/posts/"+post.getId();
    }

    @PutMapping("/{id}/edit")
    public String update(@PathVariable("id")Long id, @ModelAttribute CommentVo commentVo){
        Comment comment = commentService.findById(id);
        comment.update(commentVo.getContent());
        commentService.save(comment);

        return "redirect:/posts/" + comment.getPost().getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) throws Exception {
        Comment comment = commentService.findById(id);
        Post post = postService.findById(comment.getPost().getId());

        commentService.delete(comment);
        return "redirect:/posts/" + post.getId();
    }

}
