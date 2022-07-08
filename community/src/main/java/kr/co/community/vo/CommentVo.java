package kr.co.community.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CommentVo {
    private long id;
    @NotEmpty(message = "댓글의 내용은 빈칸일 수 없습니다.")
    @Size(max = 500, message = "댓글의 길이는 500자입니다.")
    private String content;
    private long postId;

}
