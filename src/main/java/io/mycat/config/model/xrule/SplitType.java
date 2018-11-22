package io.mycat.config.model.xrule;

/**
 * 分片规则类型
 */
public enum SplitType {
    RANGE(0, "RANGE"),

    LIST(1, "LIST"),

    HASH(2, "HASH");

    SplitType(Integer type, String name) {
        this.type = type;
        this.name = name;

    }

    private Integer type;
    private String name;
}