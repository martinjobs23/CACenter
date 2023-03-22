package com.ceit.desktop.entity;

public class CertSignVerify {
    String transId;
    String serviceCode;
    String reqDoc;
    String signValue;
    String signCert;

    public CertSignVerify() {
    }

    public CertSignVerify(String transId, String serviceCode, String reqDoc, String signValue, String signCert) {
        this.transId = transId;
        this.serviceCode = serviceCode;
        this.reqDoc = reqDoc;
        this.signValue = signValue;
        this.signCert = signCert;
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

    public String getReqDoc() {
        return reqDoc;
    }

    public void setReqDoc(String reqDoc) {
        this.reqDoc = reqDoc;
    }

    public String getSignValue() {
        return signValue;
    }

    public void setSignValue(String signValue) {
        this.signValue = signValue;
    }

    public String getSignCert() {
        return signCert;
    }

    public void setSignCert(String signCert) {
        this.signCert = signCert;
    }

    @Override
    public String toString() {
        return "CertSignVerify{" +
                "transId='" + transId + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", reqDoc='" + reqDoc + '\'' +
                ", signValue='" + signValue + '\'' +
                ", signCert='" + signCert + '\'' +
                '}';
    }
}
