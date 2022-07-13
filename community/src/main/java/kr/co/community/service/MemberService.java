package kr.co.community.service;

import kr.co.community.model.Member;
import kr.co.community.model.MemberAdapter;
import kr.co.community.model.Role;
import kr.co.community.model.enums.Author;
import kr.co.community.repository.MemberRepository;
import kr.co.community.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void Initialize(){
        Member admin = memberRepository.findByEmail("admin@admin.com");
        if(Objects.isNull(admin)){
            admin = Member.builder().email("admin@admin.com")
                    .password(bCryptPasswordEncoder.encode("admin"))
                    .username("admin").build();
            memberRepository.save(admin);
            Role adminRole = Role.builder()
                    .author(Author.ADMIN)
                    .member(admin).build();
            roleRepository.save(adminRole);
        }

        Member testUser = memberRepository.findByEmail("test@test.com");
        if(Objects.isNull(testUser)){
            testUser = Member.builder().email("test@test.com")
                    .password(bCryptPasswordEncoder.encode("test"))
                    .username("test").build();
            memberRepository.save(testUser);
            Role testRole = Role.builder()
                    .author(Author.MEMBER)
                    .member(testUser).build();
            roleRepository.save(testRole);
        }
    }

    @Transactional
    public void saveMember(Member vo){
        Member member = Member.builder()
                        .email(vo.getEmail())
                                .password(bCryptPasswordEncoder.encode(vo.getPassword()))
                                        .username(vo.getUsername()).build();
        memberRepository.save(member);
        Role role = Role.builder()
                .author(Author.MEMBER)
                .member(member).build();
        roleRepository.save(role);
        member.getRoles().add(role);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username);

        if(Objects.isNull(member)){

            throw new UsernameNotFoundException("Member not found");
        }
        return new MemberAdapter(member);
    }

    @Transactional
    public boolean checkEmailDuplication(Member member) {
        return memberRepository.existsByEmail(member.getEmail());
    }

    public boolean checkPassword(Member member, String checkPassword) {
        String realPassword = member.getPassword();
        return bCryptPasswordEncoder.matches(checkPassword, realPassword);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public void passwordUpdate(String password, Member member) {
        member.update(bCryptPasswordEncoder.encode(password));
        memberRepository.save(member);
    }
}
