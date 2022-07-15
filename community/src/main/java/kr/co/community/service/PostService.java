package kr.co.community.service;

import kr.co.community.model.Member;
import kr.co.community.model.Post;
import kr.co.community.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
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
    public Post findAll(Long id) {
        return postRepository.findByJoin(id);
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


    @Transactional
    public void deleteByMember(Long id) {
        postRepository.deleteAllByMemberId(id);
    }

    public int countByMemberId(Long id){
        return postRepository.countByMemberId(id);
    }

    public Map<String, Post> findPreviousPostById(Post post) {
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        Map<String, Post> resultMap = new HashMap<>();
        int index = posts.indexOf(post);

        if(index > 0){
            resultMap.put("previous", posts.get(index-1));
        }

        if(index<posts.size()-1){
            resultMap.put("next", posts.get(index+1));
        }

        return resultMap;
    }

}
