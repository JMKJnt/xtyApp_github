package com.example.aiden.xtapp.entity.base;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by chenly on 2017/11/23.
 */
@Table(name = "Area")
public class Area {
    @Column(name = "AREAID", isId = true)
    private String AREAID;
    @Column(name = "NATIONEN")
    private String NATIONEN;
    @Column(name = "DISTRICTEN")
    private String DISTRICTEN;
    @Column(name = "NATIONCN")
    private String NATIONCN;
    @Column(name = "NAMECN")
    private String NAMECN;
    @Column(name = "NAMEEN")
    private String NAMEEN;
    @Column(name = "PROVCN")
    private String PROVCN;
    @Column(name = "DISTRICTCN")
    private String DISTRICTCN;
    @Column(name = "PROVEN")
    private String PROVEN;

    public String getAREAID() {
        return AREAID;
    }

    public void setAREAID(String AREAID) {
        this.AREAID = AREAID;
    }

    public String getNATIONEN() {
        return NATIONEN;
    }

    public void setNATIONEN(String NATIONEN) {
        this.NATIONEN = NATIONEN;
    }

    public String getDISTRICTEN() {
        return DISTRICTEN;
    }

    public void setDISTRICTEN(String DISTRICTEN) {
        this.DISTRICTEN = DISTRICTEN;
    }

    public String getNATIONCN() {
        return NATIONCN;
    }

    public void setNATIONCN(String NATIONCN) {
        this.NATIONCN = NATIONCN;
    }

    public String getNAMECN() {
        return NAMECN;
    }

    public void setNAMECN(String NAMECN) {
        this.NAMECN = NAMECN;
    }

    public String getNAMEEN() {
        return NAMEEN;
    }

    public void setNAMEEN(String NAMEEN) {
        this.NAMEEN = NAMEEN;
    }

    public String getPROVCN() {
        return PROVCN;
    }

    public void setPROVCN(String PROVCN) {
        this.PROVCN = PROVCN;
    }

    public String getDISTRICTCN() {
        return DISTRICTCN;
    }

    public void setDISTRICTCN(String DISTRICTCN) {
        this.DISTRICTCN = DISTRICTCN;
    }

    public String getPROVEN() {
        return PROVEN;
    }

    public void setPROVEN(String PROVEN) {
        this.PROVEN = PROVEN;
    }
}
