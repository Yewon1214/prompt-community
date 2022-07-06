package kr.co.community.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentVo {
    private long id;
    private String content;
    private long postId;

}
