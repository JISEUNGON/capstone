package com.danram.server.service.oauth;

import com.danram.server.domain.Member;
import com.danram.server.dto.response.LoginResponseDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface GoogleLoginService {
    public LoginResponseDto generateTokens(Member member);
    public Member signUp();
    public Optional<Member> getUserWithAuthorities();
    public Optional<Member> getUserWithAuthorities(String name);
    public Long setId(Long id);
}
