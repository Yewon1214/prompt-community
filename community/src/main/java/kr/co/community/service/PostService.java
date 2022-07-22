package kr.co.community.service;

import kr.co.community.model.File;
import kr.co.community.model.Member;
import kr.co.community.model.Post;
import kr.co.community.model.SearchParam;
import kr.co.community.repository.PostRepository;
import kr.co.community.specification.PostSpecification;
import kr.co.community.vo.PostVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final FileService fileService;


    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    @Transactional
    public void save(Post post, PostVo postVo) throws Exception {
        postRepository.save(post);
        if (postVo.getFiles().size() > 0) {
            fileService.savePostFile(postVo.getFiles(), postVo.getDeleteFileIds(), post);
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

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post findAll(Long id) {
        return postRepository.findByJoin(id);
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
        Post post = this.findById(id);
        if(post.getContent().contains("<img")){
            String[] deletePath = post.getContent().split("upload")[1].split("\"");
            fileService.deleteImage(deletePath[0]);
        }
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
