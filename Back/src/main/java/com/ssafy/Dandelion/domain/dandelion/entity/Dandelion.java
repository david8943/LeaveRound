package com.ssafy.Dandelion.domain.dandelion.entity;

import java.math.BigDecimal;

import com.ssafy.Dandelion.global.audit.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(indexes = @Index(name = "idx_user_id", columnList = "userId"),
	name = "dandelions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dandelion extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer dandelionId;

	@Column(nullable = true)
	private Integer userId;

	@Column(nullable = false, precision = 11, scale = 6)
	private BigDecimal latitude;

	@Column(nullable = false, precision = 11, scale = 6)
	private BigDecimal longitude;
}
