package com.ceit.desktop.entity;


/**
 * @author hello world
 * @since 2021-05-17
 */
public class Cert{

    private static final long serialVersionUID = 1L;


    /**
     * id号
     */
    private Integer id;

    /**
     * 证书序列号
     */
    private String certSn;

    /**
     * 证书请求文件CSR
     */
    private String certCsr;

    /**
     * 证书私钥
     */
    private String certKey;

    /**
     * 证书使用者（终端名字 申请证书时提供的产品编码）
     */
    private String certUser;

    /**
     * 证书的存放路径
     */
    private String certCst;

    /**
     * 包含公链的公钥证书路径
     */
    private String certBufp7;

    /**
     * 证书颁发者
     */
    private String certIssuer;

    /**
     * 有效期从
     */
    private String startTime;

    /**
     * 到有效期
     */
    private String endTime;

    public String getCertSn() {
        return certSn;
    }

    public void setCertSn(String certSn) {
        this.certSn = certSn;
    }
    public String getCertCsr() {
        return certCsr;
    }

    public void setCertCsr(String certCsr) {
        this.certCsr = certCsr;
    }
    public String getCertUser() {
        return certUser;
    }

    public void setCertUser(String certUser) {
        this.certUser = certUser;
    }
    public String getCertCst() {
        return certCst;
    }

    public void setCertCst(String certCst) {
        this.certCst = certCst;
    }
    public String getCertBufp7() {
        return certBufp7;
    }

    public void setCertBufp7(String certBufp7) {
        this.certBufp7 = certBufp7;
    }
    public String getCertIssuer() {
        return certIssuer;
    }

    public void setCertIssuer(String certIssuer) {
        this.certIssuer = certIssuer;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCertKey() {
        return certKey;
    }

    public void setCertKey(String certKey) {
        this.certKey = certKey;
    }

    @Override
    public String toString() {
        return "AqjrCert{" +
                "id=" + id +
                ", certSn='" + certSn + '\'' +
                ", certCsr='" + certCsr + '\'' +
                ", certKey='" + certKey + '\'' +
                ", certUser='" + certUser + '\'' +
                ", certCst='" + certCst + '\'' +
                ", certBufp7='" + certBufp7 + '\'' +
                ", certIssuer='" + certIssuer + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
