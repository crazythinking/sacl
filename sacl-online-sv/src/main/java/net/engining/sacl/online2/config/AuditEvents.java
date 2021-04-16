package net.engining.sacl.online2.config;

import net.engining.pg.support.enums.BaseEnum;

/**
 * @author yangxing
 */
public enum AuditEvents implements BaseEnum<String> {

    /** 开始处理 */
    DOING("P","开始处理"),

    /**
     * 完成处理
     */
    COMPLETE("B", "完成处理");

    private final String value;

    private final String label;

    AuditEvents(String value, String label){
        this.value = value;
        this.label = label;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
