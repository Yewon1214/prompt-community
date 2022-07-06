package kr.co.community.model;

import kr.co.community.vo.PostVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length=1000)
    private String content;

    private int view_cnt;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Post(String title, String content, Member member){
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public boolean isWriter(Member currentMember) {
        return this.member.getUsername().equals(currentMember.getUsername());
    }

    public void update(PostVo postVo) {
        this.title=postVo.getTitle();
        this.content=postVo.getContent();
    }
}
