package kr.co.community.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fileName;

    private String originalName;

    private String relativePath;

    private String extension;

    private Long size;

    private String fileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Builder
    public File(String fileName, String originalName, String relativePath, String extension, Long size, String fileType){
        this.fileName = fileName;
        this.originalName = originalName;
        this.relativePath = relativePath;
        this.extension = extension;
        this.size = size;
        this.fileType = fileType;
    }
}
