package com.ssafy.Dandelion.domain.user.repository;

import com.ssafy.Dandelion.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // 이메일 중복 체크(회원가입)
    boolean existsByEmail(String email);

    // 이메일로 사용자 검색하여 인증(로그인)
    Optional<User> findByEmail(String email);

}
