package com.ssafy.Dandelion.global.auth.filter;

import com.ssafy.Dandelion.global.auth.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        logger.info("JwtAuthenticationFilter.doFilterInternal");
        String token = jwtTokenProvider.extractAccessToken(request);

        jwtTokenProvider.validateToken(token);

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private Map<HttpMethod, List<String>> methodUrlPatterns = new HashMap<HttpMethod, List<String>>() {{
        put(
                HttpMethod.GET, Arrays.asList(
                )
        );
        put(
                HttpMethod.POST, Arrays.asList(
                )
        );
        put(
                HttpMethod.PUT, Arrays.asList(
                )
        );
    }};

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        HttpMethod currentMethod = HttpMethod.valueOf(request.getMethod());

        if (methodUrlPatterns.containsKey(currentMethod)) {
            List<String> urlPatterns = methodUrlPatterns.get(currentMethod);
            for (String pattern : urlPatterns) {
                if (new AntPathRequestMatcher(pattern).matches(request)) {
                    return true;
                }
            }
        }
        return false;
    }
}
