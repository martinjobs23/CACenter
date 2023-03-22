package com.ceit.desktop.utils;


import com.ceit.desktop.entity.Dn;

/**
 * 网关掉命令行方式生成指定目录下的 私钥、请求文件
 */

public class GenCertReq {

    //私钥名称
    public String keyName;

    //请求文件名称
    public String reqName;

    //主题配置信息
    public String dn;

    private  String genPriKey = System.getProperty("gmssl.command") + " ecparam -genkey -name sm2p256v1 -out ";

    private  String genReq = System.getProperty("gmssl.command") + " req -new -key ";

    /**
     * 配置命令行
     * @param clientName
     */
    public GenCertReq(String clientName, String priKeyPath, String certReqPath) {
        setDn(new Dn(clientName).toString());
        setReqName(clientName + ".req");
        setKeyName(clientName + ".key");
        this.genPriKey = this.genPriKey + priKeyPath + this.keyName;
        this.genReq = this.genReq + priKeyPath + this.keyName + " -out " + certReqPath + this.reqName + " -subj " + this.dn;
    }

    public String getGenPriKey() {
        return genPriKey;
    }

    public void setGenPriKey(String genPriKey) {
        this.genPriKey = genPriKey;
    }

    public String getGenReq() {
        return genReq;
    }

    public void setGenReq(String genReq) {
        this.genReq = genReq;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    @Override
    public String toString() {
        return "genCertReq{" +
                "keyName='" + keyName + '\'' +
                ", reqName='" + reqName + '\'' +
                ", dn='" + dn + '\'' +
                ", genPriKey='" + genPriKey + '\'' +
                ", genReq='" + genReq + '\'' +
                '}';
    }

//    public static void main(String args[]) {
//        String tfName = "tf0012";
//        GenCertReq req = new GenCertReq(tfName,new Dn(tfName).toString());
//        System.out.println(req.toString());
//    }
}
