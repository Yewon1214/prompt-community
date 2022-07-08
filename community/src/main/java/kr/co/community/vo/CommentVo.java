package kr.co.community.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CommentVo {
    private long id;
    @Size(min=1, message = "댓글의 내용은 빈칸일 수 없습니다.")
    private String content;
    private long postId;

}
