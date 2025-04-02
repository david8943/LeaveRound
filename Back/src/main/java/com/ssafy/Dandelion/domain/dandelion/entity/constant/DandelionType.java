package com.ssafy.Dandelion.domain.dandelion.entity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DandelionType {
    NORMAL("일반 민들레"),
    GOLD("황금 민들레");

    private final String description;
}
