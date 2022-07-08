package kr.co.community.vo;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class PostVo {
    private Long id;

    @NotEmpty(message = "제목은 빈칸일 수 없습니다.")
    private String title;

    @NotEmpty(message = "내용은 빈칸일 수 없습니다.")
    private String content;

    @Builder
    public PostVo(String title, String content){
        this.title = title;
        this.content = content;
    }
}
