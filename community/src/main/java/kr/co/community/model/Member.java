package kr.co.community.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    @OneToMany(fetch=FetchType.EAGER, mappedBy = "member")
    private Set<Role> roles = new HashSet<>();

    @Builder
    public Member(String email, String username, String password){
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
