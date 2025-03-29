package com.ssafy.Dandelion.domain.dandelion.entity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DandelionValue {
    NORMAL(1, "일반 민들레 가치"),
    GOLD(100, "황금 민들레 가치"); // 황금 민들레 1개 = 일반 민들레 100개

    private final int value;
    private final String description;

    public static int getValueByType(DandelionType type) {
        switch (type) {
            case NORMAL:
                return NORMAL.getValue();
            case GOLD:
                return GOLD.getValue();
            default:
                return 0;
        }
    }
}
