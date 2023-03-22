package com.ceit.desktop.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hello world
 * @since 2021-07-17
 */
public class CertReader {

    static {
        Security.removeProvider("SunEC");
    }


    /**
     * 根据内容解析证书
     *
     */
        public static HashMap<String, String> analysisCerByContent(String content) {
        CertificateFactory cf;
        HashMap<String, String> map = new HashMap<>();

        try {
            // 获取工厂实例
            cf = CertificateFactory.getInstance("X.509");
            // 读入证书

            // 生成证书
            Certificate c = cf.generateCertificate(new BufferedInputStream(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))));
            X509Certificate t=(X509Certificate)c;

//            map.put("certSn", t.getSerialNumber().toString(16));

            String DN = t.getIssuerDN().toString().split(",")[0].substring(3);
            map.put("issuer", DN);

            Date date1 = t.getNotBefore();
            Date date2 = t.getNotAfter();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime localDateTime1 = date1.toInstant().atZone(zoneId).toLocalDateTime();
            LocalDateTime localDateTime2 = date2.toInstant().atZone(zoneId).toLocalDateTime();
            DateTimeFormatter localDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            map.put("dateBefore", localDateTimeFormat.format(localDateTime1));
            map.put("dateAfter", localDateTimeFormat.format(localDateTime2));

        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 解析cer证书
     */
    public static Map<String, String> analysisCer(String  url) throws Exception {
        CertificateFactory cf;
        HashMap<String, String> map = new HashMap<>();

        try {
            // 获取工厂实例
            cf = CertificateFactory.getInstance("X.509");
            // 用文件流读入证书
            FileInputStream in=new FileInputStream(url);
            // 生成证书
            Certificate c=cf.generateCertificate(in);
            X509Certificate t=(X509Certificate)c;
            in.close();
            String s=c.toString();
            System.out.println("输出证书信息:\n"+s);
            System.out.println();
            System.out.println();
            System.out.println("版本号:"+t.getVersion());
            System.out.println("序列号:"+t.getSerialNumber().toString(16));
            map.put("certSn", t.getSerialNumber().toString(16));
            System.out.println("签发者："+t.getIssuerDN());

            String DN = t.getIssuerDN().toString().split(",")[0].substring(3);

            System.out.println("签发者："+DN);

            map.put("issuer", DN);
            System.out.println("有效起始日期："+t.getNotBefore());
            System.out.println("有效终止日期："+t.getNotAfter());
            Date date1 = t.getNotBefore();
            Date date2 = t.getNotAfter();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime localDateTime1 = date1.toInstant().atZone(zoneId).toLocalDateTime();
            LocalDateTime localDateTime2 = date2.toInstant().atZone(zoneId).toLocalDateTime();
            DateTimeFormatter localDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            map.put("dateBefore", localDateTimeFormat.format(localDateTime1));
            map.put("dateAfter", localDateTimeFormat.format(localDateTime2));


            System.out.println("主体名："+t.getSubjectDN());
            String cn = getCn(t.getSubjectDN().toString());
//            String clientName = cn.substring(0, cn.indexOf(","));
            System.out.println("名称兼容测试 ：" + cn);

            System.out.println("签名算法："+t.getSigAlgName());
            System.out.println("签名："+ Arrays.toString(t.getSignature()));
            PublicKey pk=t.getPublicKey();
            byte [] pkenc=pk.getEncoded();
            System.out.println("公钥:");
            for(int  i=0;i<pkenc.length;i++)System.out.print(pkenc[i]+",");
            System.out.println();
        } catch (CertificateException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;

    }

    /**
     * 目前有三种形式: 为了正确解析CN
     * 1、C=CN, ST= HN, L= ZZ, O= SGCC, OU= HNDL, CN= tf00012
     * 2、EMAILADDRESS=48F26224D606747D@ha.sgcc.com.cn, CN=20210813, OU=HNDL, O=SGCC, ST=HA, C=CN
     * 3、CN=yxzy8664
     */
    public static String getCn(String cn) {

        cn = cn.split("CN=")[1];
        try {
            int index = cn.indexOf(",");
//            if(index == -1) {
//                throw new StringIndexOutOfBoundsException("hh");
//            }
            cn = cn.substring(0, index);
            return cn;
        } catch (StringIndexOutOfBoundsException e) {
            //1 3
//            e.printStackTrace();
            return cn;
        }
    }

    public static void main(String[] args) throws Exception {
//        String test1 = "EMAILADDRESS=48F26224D606747D@ha.sgcc.com.cn, CN=20210813, OU=HNDL, O=SGCC, ST=HA, C=CN";
        String str1 = "EMAILADDRESS=48F26224D606747D@ha.sgcc.com.cn, CN=20210813, OU=HNDL, O=SGCC, ST=HA, C=CN";
        String test1 = str1.split("CN=")[1];
        String clientName = test1.substring(0, test1.indexOf(","));
        System.out.println(clientName);

        String test = "CN=yxzy7788";
        System.out.println(test.split("CN=")[1]);
//        analysisCer("src/main/resources/template/test.crt");
//        String content = "-----BEGIN CERTIFICATE-----\n" +
//                "MIIC7jCCApKgAwIBAgISIAllltHkeFC8lDSYN1Ght+qpMAwGCCqBHM9VAYN1BQAwPzELMAkGA1UEBhMCQ04xDTALBgNVBAoMBFNHQ0MxDTALBgNVBAsMBEVQUkkxEjAQBgNVBAMMCVNNMi1DRVNISTAeFw0yMTA0MzAwMTM0MTRaFw0yMTA3MjkwMTM0MTRaMFExDTALBgNVBAMMBDA1MjIxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJHRDELMAkGA1UEBwwCR1oxCzAJBgNVBAoMAlNHMQwwCgYDVQQLDANDU0cwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATlNFyH4RV/xxFDSaFQqFngdkT43Cgl1li7wPaXBO3RMcSVhB7pURFr8W+9006Cb8lLbq+2UZ5ccHVgUXc0c5AAo4IBWDCCAVQwCwYDVR0PBAQDAgbAMAkGA1UdEwQCMAAwgZ0GA1UdHwSBlTCBkjBDoEGgP4Y9aHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcmwvU00yTkVXU0VDT05EL1NNMk5FV1NFQ09ORF8wLmNybDBLoEmgR4ZFaHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcmwvU00yTkVXU0VDT05EL2luYy9TTTJORVdTRUNPTkRfaW5jXzAuY3JsMB0GA1UdDgQWBBQwStr0cdS9tLsiEeBeCHwPNU1gCjAfBgNVHSMEGDAWgBQLwwLdX1ZtsGuCHA9vZffORUWhvzBaBgNVHSAEUzBRME8GCiqBHIbvMgYEAQEwQTA/BggrBgEFBQcCARYzaHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcHMvU00yTkVXU0VDT05EL2Nwcy5odG1sMAwGCCqBHM9VAYN1BQADSAAwRQIgX55+qvxNDFcSfHpM+2wQ9u7+/SfhXBIDgZJryoMuzUACIQC2M+chbn8K6pMdO0r7+MWajLM+joV5uwulEB6b9l5zog==" +
//                "-----END CERTIFICATE-----";
//        HashMap<String,String> map = analysisCerByContent(content);
//        for(Map.Entry<String, String> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + "   :    " + entry.getValue());
//        }

        String content1 = "-----BEGIN CERTIFICATE-----\n" +
                "    MIIBnDCCAUICAQMwCgYIKoEcz1UBg3UwOTEZMBcGA1UEAwwQQ0VJVCBTTTIgUk9P\n" +
                "    VCBDQTENMAsGA1UECwwEQ0VJVDENMAsGA1UECgwEQ0VJVDAeFw0yMTA4MTMwMjA3\n" +
                "    MjlaFw0yMjA4MTMwMjA3MjlaMHsxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJIQTEN\n" +
                "    MAsGA1UECgwEU0dDQzENMAsGA1UECwwESE5ETDERMA8GA1UEAwwIMjAyMTA4MTMx\n" +
                "    LjAsBgkqhkiG9w0BCQEWHzQ4RjI2MjI0RDYwNjc0N0RAaGEuc2djYy5jb20uY24w\n" +
                "    WTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATQsBarzt58lLjl2qkyl+eRWE9Xdzdd\n" +
                "    Q3w7v9nvi2SwLoOPd9Pxawh9uuYobsdB+oLbowXHfNqdB83RX91z42AtMAoGCCqB\n" +
                "    HM9VAYN1A0gAMEUCIA5LU5etgKSLu65VtZAgbv9BRqOw1ohvAQgAv5BXY4RLAiEA\n" +
                "    8rYT5PTRGRVPbpBPtCzkssiKS5c4i+Vl9sb2cmVnTgc=\n" +
                "    -----END CERTIFICATE-----";
        HashMap<String,String> map1 = analysisCerByContent(content1);
        for(Map.Entry<String, String> entry : map1.entrySet()) {
            System.out.println(entry.getKey() + "   :    " + entry.getValue());
        }

        //
        analysisCer("src/main/resources/template/test1.crt");

        analysisCer("src/main/resources/template/test.crt");

        analysisCer("src/main/resources/template/test2.crt");


    }

}
