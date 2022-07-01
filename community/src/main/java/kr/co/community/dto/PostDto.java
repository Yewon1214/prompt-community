package kr.co.community.dto;

import kr.co.community.model.Post;
import lombok.Getter;

@Getter
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private MemberDto member;
    private String createdAt;
    private String updatedAt;

    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getTitle();
        this.member = new MemberDto(post.getMember());
        this.createdAt = String.valueOf(post.getCreatedAt());
        this.updatedAt = String.valueOf(post.getUpdatedAt());
    }
}
