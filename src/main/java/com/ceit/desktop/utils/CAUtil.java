package com.ceit.desktop.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;

public class CAUtil {
    static String workspace = "/home/workspace_yzk/CA_CEIT";

    static String cmd_gen_key1 = "/home/workspace_yzk/CA_CEIT/gmssl ecparam -genkey -name sm2p256v1 -out /home/workspace_yzk/CA_CEIT/newkeys/%skey.pem -config /home/workspace_yzk/CA_CEIT/openssl.cnf";
    static String cmd_gen_csr1 = "/home/workspace_yzk/CA_CEIT/gmssl req -new -sm3 -key /home/workspace_yzk/CA_CEIT/newkeys/%skey.pem -out /home/workspace_yzk/CA_CEIT/newcsrs/%scsr.pem -subj /C=CN/ST=BJ/L=BJ/O=CEIT/OU=CEIT/CN=%s -config /home/workspace_yzk/CA_CEIT/openssl.cnf";
    static String cmd_gen_cer1 = "/home/workspace_yzk/CA_CEIT/gmssl ca -md sm3 -in /home/workspace_yzk/CA_CEIT/newcsrs/%scsr.pem -out /home/workspace_yzk/CA_CEIT/newcers/%s.cer -days %d -batch -config /home/workspace_yzk/CA_CEIT/openssl.cnf";
    public static int gen_CA_old(String hash){

/*        String[] cmd_gen_key = new String[6];
        cmd_gen_key[0] = "/home/workspace_yzk/CA_CEIT/gmssl";
        cmd_gen_key[1] = "ecparam";
        cmd_gen_key[2] = "-genkey";
        cmd_gen_key[3] = "-name sm2p256v1";
        cmd_gen_key[4] = String.format("-out /home/workspace_yzk/CA_CEIT/newkeys/%skey.pem",hash);
        cmd_gen_key[5] = "-config /home/workspace_yzk/CA_CEIT/openssl.cnf";

        String[] cmd_gen_csr = new String[8];
        cmd_gen_csr[0] = "/home/workspace_yzk/CA_CEIT/gmssl";
        cmd_gen_csr[1] = "req";
        cmd_gen_csr[2] = "-new";
        cmd_gen_csr[3] = "-sm3";
        cmd_gen_csr[4] = String.format("-key /home/workspace_yzk/CA_CEIT/newkeys/%skey.pem",hash);
        cmd_gen_csr[5] = String.format("-out /home/workspace_yzk/CA_CEIT/newcsrs/%scsr.pem",hash);
        cmd_gen_csr[6] = String.format("-subj \"/CN=%s/C=CN/ST=BJ/L=BJ/O=CEIT/OU=CEIT\"",hash);
        cmd_gen_csr[7] = "-config /home/workspace_yzk/CA_CEIT/openssl.cnf";

        String[] cmd_gen_cer = new String[9];
        cmd_gen_cer[0] = "/home/workspace_yzk/CA_CEIT/gmssl";
        cmd_gen_cer[1] = "ca";
        cmd_gen_cer[2] = "-md";
        cmd_gen_cer[3] = "sm3";
        cmd_gen_cer[4] = String.format("-in /home/workspace_yzk/CA_CEIT/newcsrs/%scsr.pem",hash);
        cmd_gen_cer[5] = String.format("-out /home/workspace_yzk/CA_CEIT/newcers/%s.cer",hash);
        cmd_gen_cer[6] = String.format("-days %d",3650);
        cmd_gen_cer[7] = "-batch";
        cmd_gen_cer[8] = "-config /home/workspace_yzk/CA_CEIT/openssl.cnf";*/

        String cmd1 = String.format(cmd_gen_key1, hash);
        String cmd2 = String.format(cmd_gen_csr1, hash, hash, hash);
        String cmd3 = String.format(cmd_gen_cer1, hash, hash, 3650);

        Runtime r = Runtime.getRuntime();
        int exitVal;

        try{
            String[] cmd0 = {"/usr/bin/sh","-c","export LD_LIBRARY_PATH=/home/workspace_yzk/CA_CEIT"} ;
            Process proc0 = r.exec(cmd0);
            exitVal = proc0.waitFor();
            if(exitVal != 0){
                System.out.println("\ncmd0 "+exitVal);

                InputStream is0 = proc0.getErrorStream();
                for (int k = 0; k < is0.available(); ++k)
                    System.out.print(" " + (char)is0.read());
                //return -1;
            }
            Process proc1 = r.exec(cmd1);
            exitVal = proc1.waitFor();
            if(exitVal != 0){
                System.out.println("\ncmd1 "+exitVal);

                InputStream is1 = proc1.getErrorStream();
                for (int k = 0; k < is1.available(); ++k)
                    System.out.print(" " + (char)is1.read());

                //return -1;
            }
            Process proc2 = r.exec(cmd2);
            exitVal = proc2.waitFor();
            if(exitVal != 0){
                System.out.println("\ncmd2 "+exitVal);
                //return -1;

                SequenceInputStream sis = new SequenceInputStream(proc2.getInputStream(), proc2.getErrorStream());
                InputStreamReader inst = new InputStreamReader(sis, "GBK");//设置编码格式并转换为输入流
                BufferedReader br = new BufferedReader(inst);//输入流缓冲区

                String res = null;
                StringBuilder sb = new StringBuilder();
                while ((res = br.readLine()) != null) {//循环读取缓冲区中的数据
                    sb.append(res+"\n");
                }
                br.close();
                System.out.print(sb);
            }
            Process proc3 = r.exec(cmd3);
            exitVal = proc3.waitFor();
            if(exitVal != 0){
                System.out.println("\ncmd3 "+exitVal);
                //return -1;

                SequenceInputStream sis = new SequenceInputStream(proc3.getInputStream(), proc3.getErrorStream());
                InputStreamReader inst = new InputStreamReader(sis, "GBK");//设置编码格式并转换为输入流
                BufferedReader br = new BufferedReader(inst);//输入流缓冲区

                String res = null;
                StringBuilder sb = new StringBuilder();
                while ((res = br.readLine()) != null) {//循环读取缓冲区中的数据
                    sb.append(res+"\n");
                }
                br.close();
                System.out.print(sb);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public static int gen_CA(String hash){
        Runtime r = Runtime.getRuntime();
        int exitVal;

        try{
            String cmd = "/usr/bin/bash /home/workspace_yzk/CA_CEIT/genca.sh " + hash + " 3650";

            Process proc = r.exec(cmd);
            exitVal = proc.waitFor();
            if(exitVal != 0){
                System.out.println("\ncmd "+exitVal);

                SequenceInputStream sis = new SequenceInputStream(proc.getInputStream(), proc.getErrorStream());
                InputStreamReader inst = new InputStreamReader(sis, "GBK");//设置编码格式并转换为输入流
                BufferedReader br = new BufferedReader(inst);//输入流缓冲区

                String res = null;
                StringBuilder sb = new StringBuilder();
                while ((res = br.readLine()) != null) {//循环读取缓冲区中的数据
                    sb.append(res+"\n");
                }
                br.close();
                System.out.print(sb);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return 0;
        }

        return 1;
    }
    //返回string数组 0 1 2 3 4 5 分别是  版本号  序列号  使用者  颁发者  颁发日期 过期日期
    public static String[] get_CA(String hash){
        String path = workspace + "/newcers/" + hash +".cer";
        FileInputStream in = null;
        String[] ret = new String[6];
        try{
            // 引入BC库
            Security.addProvider(new BouncyCastleProvider());
            // 使用BC解析X.509证书
            CertificateFactory CF = CertificateFactory.getInstance("X.509", "BC"); // 从证书工厂中获取X.509的单例类
            in = new FileInputStream(path); // 将本地证书读入文件流
            Certificate C = CF.generateCertificate(in);  // 将文件流的证书转化为证书类
            X509Certificate cer = (X509Certificate)C;
            ret[0] = Integer.toString(cer.getVersion());
            ret[1] = cer.getSerialNumber().toString();
            ret[2] = cer.getSubjectDN().toString();
            ret[3] = cer.getIssuerDN().toString();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = ft.format(cer.getNotBefore());
            ret[4] = time;
            time = ft.format(cer.getNotAfter());
            ret[5] = time;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            if(in != null) {
                try{
                    in.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    public static String[] get_CA(){
        String path = "C:\\Users\\yzk\\Desktop\\RTMRTYUIOPLKJHGFDSAZXCVBNMQWERAS.cer";
        FileInputStream in = null;
        String[] ret = new String[6];
        try{
            // 引入BC库
            Security.addProvider(new BouncyCastleProvider());
            // 使用BC解析X.509证书
            CertificateFactory CF = CertificateFactory.getInstance("X.509", "BC"); // 从证书工厂中获取X.509的单例类
            in = new FileInputStream(path); // 将本地证书读入文件流
            Certificate C = CF.generateCertificate(in);  // 将文件流的证书转化为证书类
            X509Certificate cer = (X509Certificate)C;
            ret[0] = Integer.toString(cer.getVersion());
            ret[1] = cer.getSerialNumber().toString();
            ret[2] = cer.getSubjectDN().toString();
            ret[3] = cer.getIssuerDN().toString();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = ft.format(cer.getNotBefore());
            ret[4] = time;
            time = ft.format(cer.getNotAfter());
            ret[5] = time;
            System.out.println(ret[4] + "==="+ret[5]);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            if(in != null) {
                try{
                    in.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }
}