package kr.co.community.repository;

import kr.co.community.model.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    LikeEntity findByPost_IdAndMember_Id(Long postId, Long memberId);
    void deleteByPost_IdAndMember_Id(Long postId, Long memberId);
}
