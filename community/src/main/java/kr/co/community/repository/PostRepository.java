package kr.co.community.repository;

import kr.co.community.model.Member;
import kr.co.community.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);

    @Modifying
    @Query("UPDATE Post p SET p.viewCnt = p.viewCnt+1 WHERE p.id = :id")
    int updateView(Long id);

    Page<Post> findPostsByMember(Member member, Pageable pageable);

    @Query(value="SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.comments " +
            "WHERE p.id = :id")
    Post findByJoin(Long id);

     void deleteAllByMemberId(Long id);

}
