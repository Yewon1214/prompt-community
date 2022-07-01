package kr.co.community.service;

import kr.co.community.model.Post;
import kr.co.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService  {
    private final PostRepository postRepository;


    public void save(Post post) {

    }
}
