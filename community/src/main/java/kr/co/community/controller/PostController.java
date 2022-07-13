package kr.co.community.controller;

import kr.co.community.handler.GlobalExceptionHandler;
import kr.co.community.model.Comment;
import kr.co.community.model.Member;
import kr.co.community.model.Pagination;
import kr.co.community.model.Post;
import kr.co.community.service.CommentService;
import kr.co.community.service.MemberService;
import kr.co.community.service.PostService;
import kr.co.community.vo.CommentVo;
import kr.co.community.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private  final CommentService commentService;

    @GetMapping("")
    public String index(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Post> postPage = postService.findAll(pageable);
        Pagination pagination = new Pagination(postPage.getPageable());
        pagination.setTotalPages(postPage.getTotalPages());
        pagination.setTotalElements(postPage.getTotalElements());

        String[] orderBy = String.valueOf(pageable.getSort()).split(":");
        model.addAttribute("postPage", postPage.getContent());
        model.addAttribute("orderBy", orderBy[0]);
        model.addAttribute("pagination", pagination);

        return "app/posts/index";
    }

    @GetMapping("/new")
    public String create(Model model){
        model.addAttribute("postVo", new PostVo());
        return "app/posts/new";
    }

    @PostMapping("")
    public String save(Principal principal, @Valid @ModelAttribute PostVo postVo, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return "app/posts/new";
        }

        Member currentMember = memberService.findByEmail(principal.getName());
        Post post = Post.builder().title(postVo.getTitle())
                .content(postVo.getContent())
                .member(currentMember).build();
        postService.savePost(post);
        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) throws Exception {
        Post post = postService.findById(id);
        if(Objects.isNull(post)){
            throw new Exception("게시글이 없습니다.");
        }
        List<Comment> comments = post.getComments();

        model.addAttribute("comments", comments);

        postService.updateView(id);
        model.addAttribute("post", post);
        model.addAttribute("commentVo", new CommentVo());
        return "app/posts/show";
    }

    @GetMapping("/{id}/edit")
    public String update(Principal principal, @PathVariable("id")Long id, Model model) throws Exception {
        Post post = postService.findById(id);
        if(Objects.isNull(post)){
            throw new Exception("게시글이 없습니다.");
        }
        Member member = memberService.findByEmail(principal.getName());
        if(!post.isWriter(member)){
            throw new Exception("수정 권한이 없습니다");
        }
        model.addAttribute("postVo", post);
        return "app/posts/new";
    }

    @PutMapping("")
    public String update(Long id, Principal principal, @Valid @ModelAttribute PostVo postVo, BindingResult bindingResult) throws Exception {
        if(bindingResult.hasErrors()){
            return "app/posts/new";
        }

        Post postForUpdate = postService.findById(id);

        Member member = memberService.findByEmail(principal.getName());
        if(!postForUpdate.isWriter(member)){
            throw new Exception("수정 권한이 없습니다");
        }

        postForUpdate.update(postVo);
        postService.savePost(postForUpdate);
        return "redirect:/posts/"+ id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) throws Exception {
        Post post = postService.findById(id);
        Member member = memberService.findByEmail(principal.getName());
        if(!post.isWriter(member)){
            throw new Exception("삭제 권한이 없습니다.");
        }
        postService.deleteById(id);
        return "redirect:/posts";
    }
}
