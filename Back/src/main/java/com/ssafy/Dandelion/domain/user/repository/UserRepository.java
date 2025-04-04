package com.ssafy.Dandelion.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ssafy.Dandelion.domain.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	// 이메일 중복 체크(회원가입)
	boolean existsByEmail(String email);

	// 이메일로 사용자 검색하여 인증(로그인)
	Optional<User> findByEmail(String email);

	@Query(value = "SELECT u.userKey FROM User u WHERE u.userId = :userId")
	Optional<String> findUserKeyByUserId(Integer userId);
}
