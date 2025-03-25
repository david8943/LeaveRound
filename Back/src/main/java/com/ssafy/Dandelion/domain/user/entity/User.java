package com.ssafy.Dandelion.domain.user.entity;

import com.ssafy.Dandelion.global.audit.BaseTimeEntityWithUpdatedAt;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseTimeEntityWithUpdatedAt {

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
