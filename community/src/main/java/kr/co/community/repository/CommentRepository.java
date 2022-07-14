package kr.co.community.repository;

import kr.co.community.model.Comment;
import kr.co.community.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findCommentsByMember(Member member, Pageable pageable);

    void deleteCommentsByMember(Member member);

    void deleteAllByMemberId(Long id);
}
