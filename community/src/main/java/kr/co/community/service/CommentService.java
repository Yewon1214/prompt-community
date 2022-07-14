package kr.co.community.service;

import kr.co.community.model.Comment;
import kr.co.community.model.Member;
import kr.co.community.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment findById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.orElse(null);
    }

    @Transactional
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    public Page<Comment> findByMember(Member member, Pageable pageable) {
        return commentRepository.findCommentsByMember(member, pageable);
    }

    @Transactional
    public void deleteByMember(Long id) {
        commentRepository.deleteAllByMemberId(id);
    }
}
