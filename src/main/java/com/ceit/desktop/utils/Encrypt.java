package com.ceit.desktop.utils;

import java.nio.charset.StandardCharsets;

public class Encrypt {

    private static final char[] HEXES = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
    };

    /**
     * byte数组 转换成 16进制小写字符串
     */
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        StringBuilder hex = new StringBuilder();

        for (byte b : bytes) {
            hex.append(HEXES[(b >> 4) & 0x0F]);
            hex.append(HEXES[b & 0x0F]);
        }

        return hex.toString();
    }

    /**
     * 16进制字符串 转换为对应的 byte数组
     */
    public static byte[] hexToBytes(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        char[] hexChars = hex.toCharArray();
        byte[] bytes = new byte[hexChars.length / 2];   // 如果 hex 中的字符不是偶数个, 则忽略最后一个

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt("" + hexChars[i * 2] + hexChars[i * 2 + 1], 16);
        }

        return bytes;
    }

    /**
     * 异或加密
     *
     * @param strOld
     *            源字符串
     * @param strKey
     *            密钥
     * @return 加密后的字符串
     */
    public static String encrypt(String strOld, String strKey) {
        byte[] data = strOld.getBytes(StandardCharsets.UTF_8);
        byte[] keyData = strKey.getBytes(StandardCharsets.UTF_8);
        int keyIndex = 0;
        for (int i = 0; i < strOld.length(); i++) {
            data[ i] = ( byte) ( data[ i] ^ keyData[ keyIndex]);
            if (++ keyIndex == keyData. length) {
                keyIndex = 0;
            }
        }
        return new String(data);
    }

    /**
     * 明文加密后转16进制字符串
     * @param handle
     * @param tfNumber
     * @return
     */
    public static String XorHexEncrypt(String handle, String tfNumber) {
        String encrypt = encrypt(handle, tfNumber);
        //转16进制字符串
        return bytesToHex(encrypt.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 16进制字符串 转明文
     * @param handle
     * @param tfNumber
     * @return
     */
    public static String XorHexDecode(String handle, String tfNumber) {
        String encode = new String(hexToBytes(handle), StandardCharsets.UTF_8);
        return Encrypt.encrypt(encode, tfNumber);
    }

    public static void main(String args[]) {
        String tfNumber = "4b373935303100005661";
        String test = "-----BEGIN CERTIFICATE-----\n" +
                "MIIC7jCCApKgAwIBAgISIAllltHkeFC8lDSYN1Ght+qpMAwGCCqBHM9VAYN1BQAwPzELMAkGA1UEBhMCQ04xDTALBgNVBAoMBFNHQ0MxDTALBgNVBAsMBEVQUkkxEjAQBgNVBAMMCVNNMi1DRVNISTAeFw0yMTA0MzAwMTM0MTRaFw0yMTA3MjkwMTM0MTRaMFExDTALBgNVBAMMBDA1MjIxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJHRDELMAkGA1UEBwwCR1oxCzAJBgNVBAoMAlNHMQwwCgYDVQQLDANDU0cwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATlNFyH4RV/xxFDSaFQqFngdkT43Cgl1li7wPaXBO3RMcSVhB7pURFr8W+9006Cb8lLbq+2UZ5ccHVgUXc0c5AAo4IBWDCCAVQwCwYDVR0PBAQDAgbAMAkGA1UdEwQCMAAwgZ0GA1UdHwSBlTCBkjBDoEGgP4Y9aHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcmwvU00yTkVXU0VDT05EL1NNMk5FV1NFQ09ORF8wLmNybDBLoEmgR4ZFaHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcmwvU00yTkVXU0VDT05EL2luYy9TTTJORVdTRUNPTkRfaW5jXzAuY3JsMB0GA1UdDgQWBBQwStr0cdS9tLsiEeBeCHwPNU1gCjAfBgNVHSMEGDAWgBQLwwLdX1ZtsGuCHA9vZffORUWhvzBaBgNVHSAEUzBRME8GCiqBHIbvMgYEAQEwQTA/BggrBgEFBQcCARYzaHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcHMvU00yTkVXU0VDT05EL2Nwcy5odG1sMAwGCCqBHM9VAYN1BQADSAAwRQIgX55+qvxNDFcSfHpM+2wQ9u7+/SfhXBIDgZJryoMuzUACIQC2M+chbn8K6pMdO0r7+MWajLM+joV5uwulEB6b9l5zog==\n" +
                "-----END CERTIFICATE-----";
        String test1 = encrypt(test, tfNumber);
        System.out.println("加密前 ： ");
        System.out.println(test);
        System.out.println("加密后 ： ");
        System.out.println(test1);
        System.out.println("16进制加密后 ： ");
        String testHex1 = XorHexEncrypt(test, tfNumber);
        System.out.println(testHex1);
        System.out.println("16进制 转 加密字符串 ： ");
        System.out.println("解密后 ： ");
        System.out.println(XorHexDecode(testHex1, tfNumber));


        String key = "-----BEGIN EC PARAMETERS-----\n" +
                "BggqgRzPVQGCLQ==\n" +
                "-----END EC PARAMETERS-----\n" +
                "-----BEGIN EC PRIVATE KEY-----\n" +
                "MHcCAQEEIP8yXnNkaWujfBhe+wlcVV8evP2xpo7UoaJzIqz5bGuMoAoGCCqBHM9V\n" +
                "AYItoUQDQgAE5TRch+EVf8cRQ0mhUKhZ4HZE+NwoJdZYu8D2lwTt0THElYQe6VER\n" +
                "a/FvvdNOgm/JS26vtlGeXHB1YFF3NHOQAA==\n" +
                "-----END EC PRIVATE KEY-----";
        System.out.println("加密前 ： ");
        System.out.println(key);

        String test2 = encrypt(key, tfNumber);
        System.out.println("加密后 ： ");
        System.out.println(test2);
        System.out.println("16进制加密后 ： ");
        String testHex2 = bytesToHex(test2.getBytes(StandardCharsets.UTF_8));
        System.out.println(testHex2);

        System.out.println("16进制 转 加密字符串 ： ");
        String testhh2 = new String(hexToBytes(testHex2), StandardCharsets.UTF_8);

        System.out.println("16进制解密后 ： ");
        System.out.println(XorHexDecode(testHex2, tfNumber));
        System.out.println(Encrypt.encrypt(testhh2, tfNumber));

    }
}
