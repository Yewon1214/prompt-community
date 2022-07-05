package kr.co.community.controller;

import kr.co.community.dto.PostDto;
import kr.co.community.handler.GlobalExceptionHandler;
import kr.co.community.model.Member;
import kr.co.community.model.Post;
import kr.co.community.service.MemberService;
import kr.co.community.service.PostService;
import kr.co.community.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final GlobalExceptionHandler globalExceptionHandler;

    @GetMapping("")
    public String index(Model model){

        List<Post> postList = postService.findAll();
        model.addAttribute("postList", postList);
        return "app/posts/index";
    }

    @GetMapping("/new")
    public String create(Model model){
        model.addAttribute("postVo", new PostVo());
        return "app/posts/new";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model){
        Post post = postService.findById(id);
        postService.updateView(id);
        model.addAttribute("post", post);
        return "app/posts/show";
    }

    @PostMapping("/")
    public String create(@ModelAttribute PostVo postVo, Principal principal){
        Member currentMember = memberService.findByEmail(principal.getName());
        Post post = Post.builder().title(postVo.getTitle())
                    .content(postVo.getContent())
                    .member(currentMember).build();
        postService.save(post);
        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id")Long id, Model model, Principal principal) throws Exception {
        Post post = postService.findById(id);
        Member currentMember = memberService.findByEmail(principal.getName());
        if(!post.isWriter(currentMember)){
            throw new Exception("수정 권한이 없습니다.");
        }
        model.addAttribute("postVo", post);
        return "app/posts/new";
    }

    @PutMapping("/{id}/edit")
    public String update(@PathVariable("id")Long id, @ModelAttribute PostVo postVo, Principal principal) throws Exception {
        Post postForUpdate = postService.findById(id);
        Member currentMember = memberService.findByEmail(principal.getName());
        if(!postForUpdate.isWriter(currentMember)) {
            throw new Exception("수정 권한이 없습니다.");
        }
        postForUpdate.update(postVo);
        postService.save(postForUpdate);
        return "redirect:/posts";
    }

    @DeleteMapping("/{id}")
    public String delete (@PathVariable("id") Long id, Model model, Principal principal) throws Exception {
        Post post = postService.findById(id);
        Member currentMember = memberService.findByEmail(principal.getName());
        if(!post.isWriter(currentMember)){
            throw new Exception("삭제 권한이 없습니다.");
        }
        postService.delete(id);
        return "redirect:/posts";
    }
}
