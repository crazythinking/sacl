package net.engining.sacl.online2.config;

import net.engining.pg.support.enums.BaseEnum;

/**
 * @author yangxing
 */
public enum AuditStates implements BaseEnum<String> {
    /** 待处理 */	P("P","待处理"),
    /** 处理中 */	B("B","处理中"),
    /** 处理完成 */	F("F","处理完成");

    private final String value;

    private final String label;

    AuditStates(String value, String label){
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
