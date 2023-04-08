package com.danram.server.jwt;

import com.danram.server.domain.Authority;
import com.danram.server.domain.Member;
import com.danram.server.dto.response.MemberIdDto;
import com.danram.server.service.member.MemberService;
import com.danram.server.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JwtCustomFilter extends OncePerRequestFilter {
    @Value("${external.jwt.secret}")
    private String secretKey;
    private MemberService memberService;

    public JwtCustomFilter(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // bearer이 아니면 오류
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "JWT Token does not begin with Bearer String");
            return;
        }

        // Token 꺼내기
        String token = authorizationHeader.split(" ")[1];

        // Token 검증
        if (!JwtUtil.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "JWT Token is not valid");

            return;
        }

        // Token 만료 체크
        if (JwtUtil.isExpired(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token is expired");
            return;
        }

        // 유저 식별
        MemberIdDto memberIdDto = JwtUtil.getMemberId(token);

        Optional<Member> member = memberService.findMemberByUserId(memberIdDto.getId());

        if (member.isEmpty()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "JWT Token is not valid");
            filterChain.doFilter(request, response);
            return;
        }

        //권한 검사
        Member result = member.get();
        String role;

        if(result.hasRole("ADMIN")) {
            role = "ROLE_ADMIN";
        }
        else if(result.hasRole("USER")) {
            role = "ROLE_USER";
        }
        else {
            log.info("error occurs");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "User has not permission");

            return;
        }

        log.warn("role: {}", role);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member, null, List.of(new SimpleGrantedAuthority(role)));

        // UserDetail을 통해 인증된 사용자 정보를 SecurityContext에 저장
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}