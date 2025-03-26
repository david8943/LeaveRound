package com.ssafy.Dandelion.global.auth.user;

import com.ssafy.Dandelion.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ssafy.Dandelion.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    //private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // return userRepository.findByEmail(email)
        //     .map(this::createUserDetails)
        //     .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        return null;
    }

    private UserDetails createUserDetails(User user) {
        return CustomUserDetails.builder()
                .userId(user.getUserId().longValue())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .userKey(user.getUserKey())
                .build();
    }
}
