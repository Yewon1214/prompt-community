package kr.co.community.service;

import kr.co.community.dto.PostDto;
import kr.co.community.model.Member;
import kr.co.community.model.Post;
import kr.co.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService  {
    private final PostRepository postRepository;


    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public Post findById(Long id){
        Optional<Post> postOptional = postRepository.findById(id);
        return postOptional.orElse(null);
    }

    public void delete(Long id){
        postRepository.deleteById(id);
    }

    @Transactional
    public int updateView(Long id) {
        return postRepository.updateView(id);
    }
}
