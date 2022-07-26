package kr.co.community.repository;

import kr.co.community.model.Member;
import kr.co.community.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Optional<Post> findById(Long id);

    Page<Post> findPostsByMember(Member member, Pageable pageable);

    @Query(value="SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.comments " +
            "WHERE p.id = :id")
    Post findByJoin(Long id);

    Page<Post> findAll(Pageable pageable);

    @Modifying
    @Query("UPDATE Post p SET p.viewCnt = p.viewCnt+1 WHERE p.id = :id")
    int updateView(Long id);

    void deleteAllByMemberId(Long id);

    @Query(value = "SELECT COUNT(p.id) FROM Post p " +
             "WHERE p.member.id = :id")
    int countByMemberId(Long id);

    @Modifying
    @Query(value="UPDATE Post p SET p.likeCnt = p.likeCnt+1 WHERE p.id =:id")
    void plusLike(Long id);

    @Modifying
    @Query(value = "UPDATE Post p SET p.likeCnt = p.likeCnt-1 WHERE p.id = :id")
    void minusLike(Long id);
}
