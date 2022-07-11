package kr.co.community.service;

import kr.co.community.model.Member;
import kr.co.community.model.Post;
import kr.co.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService  {
    private final PostRepository postRepository;


    @Transactional
    public void savePost(Post post) {
        postRepository.save(post);
    }

    public Post findById(Long id){
        Optional<Post> postOptional = postRepository.findById(id);
        return postOptional.orElse(null);
    }

    public Page<Post> findAll(Pageable pageable){
        return postRepository.findAll(pageable);
    }

    public Page<Post> findByMember(Member member, Pageable pageable) {
        return postRepository.findPostsByMember(member, pageable);
    }

    @Transactional
    public void updateView(Long id) {
        postRepository.updateView(id);
    }

    @Transactional
    public void deleteById(Long id){
        postRepository.deleteById(id);
    }

}
