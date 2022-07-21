package kr.co.community.repository;

import kr.co.community.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByPostId(Long id);

    File findFileByOrOriginalName(String originalName);

    long deleteByPostId(long id);

}
