package com.ceit.desktop.entity;

public class CertUpdate {

    //流水号
    private String transId;

    //产品编码
    private String serviceCode;

    //证书Base64编码
    private String cst;

    //证书序列号
    private String certSn;

    //原证书所在容器生成的新的证书请求
    private String newP10;

    //老证书对csr（newP10）签名后的base64字符串
    private String pkcs7;

    //备用字段
    private String raId;

    public CertUpdate() {
    }

    public CertUpdate(String transId, String serviceCode, String cst, String certSn, String newP10, String pkcs7, String raId) {
        this.transId = transId;
        this.serviceCode = serviceCode;
        this.cst = cst;
        this.certSn = certSn;
        this.newP10 = newP10;
        this.pkcs7 = pkcs7;
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

    public String getCst() {
        return cst;
    }

    public void setCst(String cst) {
        this.cst = cst;
    }

    public String getCertSn() {
        return certSn;
    }

    public void setCertSn(String certSn) {
        this.certSn = certSn;
    }

    public String getNewP10() {
        return newP10;
    }

    public void setNewP10(String newP10) {
        this.newP10 = newP10;
    }

    public String getPkcs7() {
        return pkcs7;
    }

    public void setPkcs7(String pkcs7) {
        this.pkcs7 = pkcs7;
    }

    public String getRaId() {
        return raId;
    }

    public void setRaid(String raId) {
        this.raId = raId;
    }

    @Override
    public String toString() {
        return "CertUpdate{" +
                "transId='" + transId + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", cst='" + cst + '\'' +
                ", certSn='" + certSn + '\'' +
                ", newP10='" + newP10 + '\'' +
                ", pkcs7='" + pkcs7 + '\'' +
                ", raId='" + raId + '\'' +
                '}';
    }
}
