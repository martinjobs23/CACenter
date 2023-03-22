package com.ceit.desktop.entity;

public class CertRevoke {

    //流水号
    private String transId;

    //产品编码
    private String serviceCode;

    //证书序列号
    private String certSn;

    //吊销原因
    private String revokeReason;

    //备用字段
    private String raId;

    public CertRevoke() {
    }

    public CertRevoke(String transId, String serviceCode, String certSn, String revokeReason, String raId) {
        this.transId = transId;
        this.serviceCode = serviceCode;
        this.certSn = certSn;
        this.revokeReason = revokeReason;
        this.raId = raId;
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

    public String getCertSn() {
        return certSn;
    }

    public void setCertSn(String certSn) {
        this.certSn = certSn;
    }

    public String getRevokeReason() {
        return revokeReason;
    }

    public void setRevokeReason(String revokeReason) {
        this.revokeReason = revokeReason;
    }

    public String getRaId() {
        return raId;
    }

    public void setRaId(String raId) {
        this.raId = raId;
    }

    @Override
    public String toString() {
        return "CertRevoke{" +
                "transId='" + transId + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", certSn='" + certSn + '\'' +
                ", revokeReason='" + revokeReason + '\'' +
                ", raId='" + raId + '\'' +
                '}';
    }
}
