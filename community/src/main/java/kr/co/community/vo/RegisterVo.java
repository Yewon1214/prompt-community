package kr.co.community.vo;

import kr.co.community.model.Member;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterVo {

    @NotBlank
    @Size(min = 2, max = 20)
    private String username;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;


    public Member toEntity(){
        return Member.builder().username(username)
                .email(email)
                .password(password)
                .build();
    }
}
