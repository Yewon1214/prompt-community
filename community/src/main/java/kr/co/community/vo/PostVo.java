package kr.co.community.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class PostVo {
    private Long id;
    private String title;
    private String content;

    @Builder
    public PostVo(String title, String content){
        this.title = title;
        this.content = content;
    }
}
