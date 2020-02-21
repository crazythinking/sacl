package net.engining.sacl.online2.controller;

import net.engining.pg.web.bean.BaseResponseBean;

import java.math.BigDecimal;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2019-12-26 10:32
 * @since :
 **/
public class Foo extends BaseResponseBean {
    String f1;
    BigDecimal f2;

    public String getF1() {
        return f1;
    }

    public void setF1(String f1) {
        this.f1 = f1;
    }

    public BigDecimal getF2() {
        return f2;
    }

    public void setF2(BigDecimal f2) {
        this.f2 = f2;
    }
}
