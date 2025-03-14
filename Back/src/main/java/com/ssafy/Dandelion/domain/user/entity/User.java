package com.ssafy.Dandelion.domain.user.entity;

import org.checkerframework.common.aliasing.qual.Unique;

import com.ssafy.Dandelion.global.audit.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@Column(nullable = false, length = 20)
	private String name;

	@Unique
	@Column(nullable = false, length = 200)
	private String email;

	@Column(nullable = false, length = 200)
	private String password;

	@Column(nullable = false)
	private String userKey;

	@Column(nullable = false, columnDefinition = "int default 0")
	private int dandelionCount;

	@Column(nullable = false, columnDefinition = "int default 0")
	private int dandelionUseCount;

	@Column(nullable = false, columnDefinition = "int default 0")
	private int goldDandelionCount;

	@Column(nullable = false, columnDefinition = "int default 0")
	private int goldDandelionUseCount;
}
