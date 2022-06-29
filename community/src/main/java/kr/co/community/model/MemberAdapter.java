package kr.co.community.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class MemberAdapter extends User {
    private Member member;

    public MemberAdapter(Member member){
        super(member.getEmail(), member.getPassword(), authorities(member.getRoles()));
        this.member = member;
    }


    public static Collection<? extends GrantedAuthority> authorities(Set<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthor().authority()))
                .collect(Collectors.toSet());
    }
}
