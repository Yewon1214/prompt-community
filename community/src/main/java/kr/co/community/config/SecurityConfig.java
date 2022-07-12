package kr.co.community.config;

import kr.co.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/posts/new/**", "post/**/edit").authenticated()
                .anyRequest().permitAll()
                .and()
                .csrf()
                .and()
                .formLogin()
                .loginPage("/member/login")
                .defaultSuccessUrl("/posts")
                .failureUrl("/member/login?error=1")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/member/logout")
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .rememberMe()
                .key("prompt!")
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(86400 * 30)
                .userDetailsService(memberService);
    }

}
