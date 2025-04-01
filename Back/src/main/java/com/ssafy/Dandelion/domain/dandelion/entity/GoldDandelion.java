package com.ssafy.Dandelion.domain.dandelion.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ssafy.Dandelion.global.audit.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(indexes = @Index(name = "idx_user_id", columnList = "userId"),
	name = "gold_dandelions")
public class GoldDandelion extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer goldDandelionId;

	@Column
	private Integer userId;

	@Column(nullable = false, precision = 11, scale = 6)
	private BigDecimal latitude;

	@Column(nullable = false, precision = 11, scale = 6)
	private BigDecimal longitude;

	@Column
	private LocalDateTime acquiredAt;
}
