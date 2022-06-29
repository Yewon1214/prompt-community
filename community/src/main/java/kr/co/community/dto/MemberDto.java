package kr.co.community.dto;

import kr.co.community.model.Member;
import lombok.Getter;

@Getter
public class MemberDto {
    private long id;
    private String email;
    private String username;

    public MemberDto(Member member){
        this.id = member.getId();
        this.email = member.getEmail();
        this.username = member.getUsername();
    }
}
