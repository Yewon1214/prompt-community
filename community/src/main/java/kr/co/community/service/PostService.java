package kr.co.community.service;

import kr.co.community.model.Post;
import kr.co.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService  {
    private final PostRepository postRepository;


    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    public Post findById(Long id){
        Optional<Post> postOptional = postRepository.findById(id);
        return postOptional.orElse(null);
    }

    public Page<Post> findAll(Pageable pageable){
        return postRepository.findAll(pageable);
    }

    @Transactional
    public void deleteById(Long id){
        postRepository.deleteById(id);
    }

    @Transactional
    public int updateView(Long id) {
        return postRepository.updateView(id);
    }
}
