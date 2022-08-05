package kr.co.community.service;

import kr.co.community.model.*;
import kr.co.community.repository.LikeRepository;
import kr.co.community.repository.PostRepository;
import kr.co.community.specification.PostSpecification;
import kr.co.community.vo.PostVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final MemberService memberService;
    private final FileService fileService;


    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    @Transactional
    public void save(Post post, PostVo postVo) throws Exception {
        postRepository.save(post);

        if (postVo.getFs() != null && !postVo.hasFile()) {
            fileService.savePostFile(postVo.getFs(), postVo.getDeleteFileIds(), postVo.getsaveImgIds(), post);
        }
    }

    @Transactional
    public boolean saveLike(Long postId, Long memberId) {
        LikeEntity findLikeEntity = likeRepository.findByPost_IdAndMember_Id(postId, memberId);
        if(Objects.isNull(findLikeEntity)){
            Member member = memberService.findById(memberId);
            Post post = this.findById(postId);

            LikeEntity likeEntity = LikeEntity.toLike(member, post);
            likeRepository.save(likeEntity);
            postRepository.plusLike(postId);
            return true;
        }else{
            likeRepository.deleteByPost_IdAndMember_Id(postId, memberId);
            postRepository.minusLike(postId);
            return false;
        }
    }

    public Post findById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        return postOptional.orElse(null);
    }

    public Map<String, Post> findPreviousPostById(Post post) {
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        Map<String, Post> resultMap = new HashMap<>();
        int index = posts.indexOf(post);

        if (index > 0) {
            resultMap.put("previous", posts.get(index - 1));
        }

        if (index < posts.size() - 1) {
            resultMap.put("next", posts.get(index + 1));
        }

        return resultMap;
    }

    public Post findAll(Long id) {
        return postRepository.findByJoin(id);
    }

    public boolean findLike(Long postId, Long memberId) {
        LikeEntity likeEntity = likeRepository.findByPost_IdAndMember_Id(postId, memberId);
        return Objects.isNull(likeEntity) ? false : true;
    }

    public Page<Post> findByMember(Member member, Pageable pageable) {
        return postRepository.findPostsByMember(member, pageable);
    }

    public Page<Post> findBySearchParam(SearchParam searchParam, Pageable pageable) {
        Specification specification = Specification.where(PostSpecification.search(searchParam));
        return postRepository.findAll(specification, pageable);
    }

    @Transactional
    public void deleteById(Long id) throws FileNotFoundException {

        List<File> files = fileService.findByPostId(id);
        if (files.size() > 0) {

            fileService.deleteByPostId(id);
            for (File file : files) {
                boolean flag = fileService.deleteFile(file);
                if (!flag) {
                    throw new FileNotFoundException("파일을 찾을 수 없습니다.");
                } else {
                    fileService.flush();
                }
            }
        }
        postRepository.deleteById(id);
    }

    @Transactional
    public void deleteByMember(Long id) {
        postRepository.deleteAllByMemberId(id);
    }

    @Transactional
    public void updateView(Long id) {
        postRepository.updateView(id);
    }


    public int countByMemberId(Long id) {
        return postRepository.countByMemberId(id);
    }

}
