package com.ceit.desktop.entity;

/**
 * @ClassName CertStateQuery
 * @Description 证书有效性检验的实体类
 * @Author hello world
 * @DATE 2022/6/7  17:40
 **/
public class CertStateQuery {
    //流水号
    private String transId ;
    //产品编码
    private String serviceCode;
    //证书请求
    private String cert;
    //备用字段
    private String provinceID;

    public CertStateQuery() {
    }

    public CertStateQuery(String transId, String serviceCode, String cert, String provinceID) {
        this.transId = transId;
        this.serviceCode = serviceCode;
        this.cert = cert;
        this.provinceID = provinceID;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(String provinceID) {
        this.provinceID = provinceID;
    }

    @Override
    public String toString() {
        return "CertStateQuery{" +
                "transId='" + transId + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", cert='" + cert + '\'' +
                ", provinceID='" + provinceID + '\'' +
                '}';
    }
}
