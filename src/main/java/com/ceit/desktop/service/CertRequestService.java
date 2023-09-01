package com.ceit.desktop.service;

import com.ceit.desktop.entity.EquipmentCertReq;
import com.ceit.desktop.grpc.caCenter.DevRegisterRequest;
import com.ceit.desktop.grpc.caCenter.DeviceUnRegisterRequest;
import com.ceit.desktop.grpc.caCenter.SoftRegisterRequest;
import com.ceit.desktop.utils.*;
import com.ceit.ioc.annotations.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CertRequestService {

    private static Logger logger = LoggerFactory.getLogger(CertRequestService.class);

    private FileConfigUtil fileConfigUtil = new FileConfigUtil();

    @Autowired
    private ClientConf conf;

    @Autowired
    private Hash hash;

    private String priKeyPath = fileConfigUtil.load("desktop.properties","priKey.path");

    private String certReqPath = fileConfigUtil.load("desktop.properties","certReqFile.path");

    private String certFilePath = fileConfigUtil.load("desktop.properties","certFile.path");

    private static final String serviceCode = "CEIT";

    private static GenerateTransId idFactory =  GenerateTransId.getInstance();

    JdbcUtil jdbcUtil = new JdbcUtil();

    //prod
    /**
     * 下面的这一句代码先会被注释掉，连接国网密服平台会报错
    */
    public static AccessConfig access = AccessConfig.getInstance();

    public CertRequestService() throws IOException {
    }

    //终端注册
    public Result dev_Cert_Req(DevRegisterRequest request)  {
        int is_handle = request.getIsHandle();
        String serial = request.getSerial();
        String dev_name = request.getDevName();
        String org_id = request.getOrgId();
        String device_ip = request.getDeviceIp();
        String dev_mac  = request.getDeviceMac();
        //获取HASH值
        String username = serial.substring(0,32);
        String gmssl = "";
        String exeDir = "";
        int flag = 0;
        try{
            //获取gmssl路径（在配置文件设置）
            gmssl = fileConfigUtil.load("desktop.properties","gmssl.command");
            //设置私钥、请求文件、证书存储路径（本地测试环境都存在放同一个位置）
            exeDir = fileConfigUtil.load("desktop.properties","exeDir.path");
            flag = Integer.parseInt(fileConfigUtil.load("desktop.properties","certApply"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (flag == 0 ){
            //管理员同意注册
            if (is_handle ==1 ){
                //随机生成流水号
                String tranId = idFactory.createTransId();
                //设备mac
                //String dev_mac = serial.substring(32);
                //生成私钥命令
                String createKeyPar =gmssl + " ecparam -genkey -name sm2p256v1 -out " + exeDir   + username + ".key";
                //生成请求文件命令
                String createCSRPar =gmssl + " req -new -sm3 -key \""+ exeDir +username+".key\" -out \""+ exeDir +username+".req\" -subj /CN="+username+"/C=CN/ST=BJ/L=NCEPU/O=CEIT/OU=CEIT";
                //生成证书命令
                String createCERPar = gmssl + " ca -md sm3 -in \""+ exeDir +username+".req\" -out "+ exeDir +username+".crt -days 3650 -batch";
                //生成私钥
                CommandUtil.exeCommand(createKeyPar);
                String pkiKeycontent = FileUtil.readFile(exeDir + username + ".key");
                if (pkiKeycontent==null||pkiKeycontent.equals("")){
                    return new Result("私钥生成失败",400,"error");
                }
                //生成请求文件
                CommandUtil.exeCommand(createCSRPar);
                String reqcontent =  FileUtil.readFile(exeDir + username + ".req");
                if (reqcontent==null||reqcontent.equals("")){
                    return new Result("证书请求文件生成失败",400,"error");
                }
                //生成证书
                CommandUtil.exeCommand(createCERPar);
                String crtcontent =  FileUtil.readFile(exeDir + username + ".crt");
                if (crtcontent==null||crtcontent.equals("")){
                    return new Result("证书生成失败",400,"error");
                }

                //证书生成时间
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String cert_time = simpleDateFormat.format(date);

                //解析证书获取证书有效期
                String[] certInfo = CAUtil.get_CA(username,exeDir +username+".crt");
                if(certInfo == null) return new Result("解析证书失败",200,"get_CA failed");

                Date date_start = null;
                Date date_end = null;
                try{
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date_start = ft.parse(certInfo[4]);
                    date_end = ft.parse(certInfo[5]);
                }
                catch (Exception e){
                    e.printStackTrace();
                }


                List<Map<String,Object>> list = jdbcUtil.executeQuery("SELECT time FROM dev_unregister where dev_hash=?",serial);
                Date dev_reg_time = null;
                for (Map map:list){
                    dev_reg_time = (Date) map.get("time");
                }

                //更新未注册设备中的记录
                jdbcUtil.executeUpdate("update dev_unregister set status=1, examine_time =? where dev_hash = ?", cert_time, username);

                list = jdbcUtil.executeQuery("SELECT id FROM dev_cert where dev_hash=?",serial);
                if(null == list || list.isEmpty()){
                    //            jdbcUtil.executeUpdate("insert into radcheck (username,attribute,op,`value`) values (?,'Cleartext-Password',':=',?) ",username,username.substring(0,16));
                    jdbcUtil.executeUpdate("insert into dev_cert (username,tran_id,cert_name,cert_time,dev_mac,dev_name,org_id,dev_reg_time,dev_hash,dev_ip,dev_reg_status,cert_csr,cert_cst,cert_key,cert_issuer,start_time,end_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ",
                            username,tranId,serial+".crt",cert_time,dev_mac,dev_name,org_id,dev_reg_time,username,device_ip,"1",exeDir + username + ".req",exeDir,exeDir + username + ".key","cacenter",date_start,date_end);
                    return new Result("证书生成成功",200,"success");
                } else {
                    //            jdbcUtil.executeUpdate("insert into radcheck (username,attribute,op,`value`) values (?,'Cleartext-Password',':=',?) ",username,username.substring(0,16));
                    jdbcUtil.executeUpdate("update dev_cert set username =?,tran_id = ?,cert_name =?,cert_time =?,dev_mac =?,dev_name =?,org_id =?,dev_reg_time =?,dev_hash =?,dev_ip =?,dev_reg_status =?,cert_csr =?,cert_cst =?,cert_key =?,cert_issuer =?,start_time =?,end_time =? where dev_hash =?",
                            username,tranId,serial+".crt",cert_time,dev_mac,dev_name,org_id, LocalDateTime.now(),username,device_ip,"1",exeDir + username + ".req",exeDir,exeDir + username + ".key","cacenter",date_start,date_end,username);
                    return new Result("更新证书成功",200,"success");
                }
            } else {
                jdbcUtil.executeUpdate("update dev_unregister set status = 2,examine_time = ? where dev_hash = ? ",LocalDateTime.now(),username);
                return new Result("已拒绝",200,"failed");
            }
        } else {
            //1.审核同意
            if (is_handle == 1) {
                //密服平台初始化
                AccessConfig.initConnect();
                //1.向密码服务平台发起请求
                //密码服务平台测试
                EquipmentCertReq certReq  = new EquipmentCertReq();
                //生成流水号
                String tranId = idFactory.createTransId();
                certReq.setTransId(tranId);
                //产品编码为“设备序列号+设备mac”
                //certReq.setServiceCode(username);
                certReq.setServiceCode("cte0001");
                //产生指定配置的私钥、请求文件
                try {
                    GenCertReq req = new GenCertReq(username, priKeyPath, certReqPath);
                    //调用命令行生成私钥
                    CommandUtil.exeCommand(req.getGenPriKey());
                    String pkiKeycontent = null;
                    String reqcontent = null;

                    pkiKeycontent = FileUtil.readFile(fileConfigUtil.load("desktop.properties", "priKey.path") + username + ".key");
                    if (pkiKeycontent == null || pkiKeycontent.equals("")) {
                        return new Result("密钥对生成失败", 400, "error");
                    }
                    //调用命令行生成证书请求文件
                    CommandUtil.exeCommand(req.getGenReq());
                    reqcontent = FileUtil.readFile(fileConfigUtil.load("desktop.properties", "certReqFile.path") + username + ".req");
                    if (reqcontent == null || reqcontent.equals("")) {
                        return new Result("证书请求生成失败", 400, "error");
                    }
                    else {
                       //密服平台证书请求文件不需要"-----BEGIN CERTIFICATE REQUEST-----"，"-----END CERTIFICATE REQUEST-----"
                        reqcontent = reqcontent.replace("-----BEGIN CERTIFICATE REQUEST-----", "").replace("-----END CERTIFICATE REQUEST-----", "").replace("\n", "");
                    }
                    certReq.setCertRequest(reqcontent);
//                    certReq.setRaId("");

                }catch (IOException e) {
                    e.printStackTrace();
                }

                /**
                 * //固定信息返回
                 * String test = "{\"data\":\"{\\\"cert\\\":\\\"MIIC7jCCApKgAwIBAgISIAllltHkeFC8lDSYN1Ght+qpMAwGCCqBHM9VAYN1BQAwPzELMAkGA1UEBhMCQ04xDTALBgNVBAoMBFNHQ0MxDTALBgNVBAsMBEVQUkkxEjAQBgNVBAMMCVNNMi1DRVNISTAeFw0yMTA0MzAwMTM0MTRaFw0yMTA3MjkwMTM0MTRaMFExDTALBgNVBAMMBDA1MjIxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJHRDELMAkGA1UEBwwCR1oxCzAJBgNVBAoMAlNHMQwwCgYDVQQLDANDU0cwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATlNFyH4RV/xxFDSaFQqFngdkT43Cgl1li7wPaXBO3RMcSVhB7pURFr8W+9006Cb8lLbq+2UZ5ccHVgUXc0c5AAo4IBWDCCAVQwCwYDVR0PBAQDAgbAMAkGA1UdEwQCMAAwgZ0GA1UdHwSBlTCBkjBDoEGgP4Y9aHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcmwvU00yTkVXU0VDT05EL1NNMk5FV1NFQ09ORF8wLmNybDBLoEmgR4ZFaHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcmwvU00yTkVXU0VDT05EL2luYy9TTTJORVdTRUNPTkRfaW5jXzAuY3JsMB0GA1UdDgQWBBQwStr0cdS9tLsiEeBeCHwPNU1gCjAfBgNVHSMEGDAWgBQLwwLdX1ZtsGuCHA9vZffORUWhvzBaBgNVHSAEUzBRME8GCiqBHIbvMgYEAQEwQTA/BggrBgEFBQcCARYzaHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcHMvU00yTkVXU0VDT05EL2Nwcy5odG1sMAwGCCqBHM9VAYN1BQADSAAwRQIgX55+qvxNDFcSfHpM+2wQ9u7+/SfhXBIDgZJryoMuzUACIQC2M+chbn8K6pMdO0r7+MWajLM+joV5uwulEB6b9l5zog==\\\",\\\"certSn\\\":\\\"20096596d1e47850bc9434983751a1b7eaa9\\\"}\",\"message\":\"success\",\"status\":\"0\",\"transId\":\"149184c9\"}";
                 */

                ResultJson res;
                try {
                    //生成随机数测试
//                    res = access.randomNumber();
//                    logger.info(res.toString());

                    res = access.handleCertRequest(certReq);
                }
                catch (Exception e){
                    e.printStackTrace();
                    return new Result("error",400,"error");
                }
//            String test = "{\"data\":\"{\\\"cert\\\":\\\"MIIC7jCCApKgAwIBAgISIAllltHkeFC8lDSYN1Ght+qpMAwGCCqBHM9VAYN1BQAwPzELMAkGA1UEBhMCQ04xDTALBgNVBAoMBFNHQ0MxDTALBgNVBAsMBEVQUkkxEjAQBgNVBAMMCVNNMi1DRVNISTAeFw0yMTA0MzAwMTM0MTRaFw0yMTA3MjkwMTM0MTRaMFExDTALBgNVBAMMBDA1MjIxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJHRDELMAkGA1UEBwwCR1oxCzAJBgNVBAoMAlNHMQwwCgYDVQQLDANDU0cwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATlNFyH4RV/xxFDSaFQqFngdkT43Cgl1li7wPaXBO3RMcSVhB7pURFr8W+9006Cb8lLbq+2UZ5ccHVgUXc0c5AAo4IBWDCCAVQwCwYDVR0PBAQDAgbAMAkGA1UdEwQCMAAwgZ0GA1UdHwSBlTCBkjBDoEGgP4Y9aHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcmwvU00yTkVXU0VDT05EL1NNMk5FV1NFQ09ORF8wLmNybDBLoEmgR4ZFaHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcmwvU00yTkVXU0VDT05EL2luYy9TTTJORVdTRUNPTkRfaW5jXzAuY3JsMB0GA1UdDgQWBBQwStr0cdS9tLsiEeBeCHwPNU1gCjAfBgNVHSMEGDAWgBQLwwLdX1ZtsGuCHA9vZffORUWhvzBaBgNVHSAEUzBRME8GCiqBHIbvMgYEAQEwQTA/BggrBgEFBQcCARYzaHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcHMvU00yTkVXU0VDT05EL2Nwcy5odG1sMAwGCCqBHM9VAYN1BQADSAAwRQIgX55+qvxNDFcSfHpM+2wQ9u7+/SfhXBIDgZJryoMuzUACIQC2M+chbn8K6pMdO0r7+MWajLM+joV5uwulEB6b9l5zog==\\\",\\\"certSn\\\":\\\"20096596d1e47850bc9434983751a1b7eaa9\\\"}\",\"message\":\"success\",\"status\":\"0\",\"transId\":\"149184c9\"}";
//            ResultJson res = ResultJson.getResult(test);
                Map<String, String> map;

                //success
                if(res.getStatus().equals("0")) {
                    map = res.getData().get(0);
                    String certContent  = map.get("cert");
                    String certSn = map.get("certSn");

                    //产生证书
                    String overName = username + ".crt";
                    //设置系统换行符 默认linux下
                    String lineSeparator = System.getProperty("line.separator", "\n");
                    StringBuilder sb = new StringBuilder("-----BEGIN CERTIFICATE-----\n");
                    sb.append(certContent).append(lineSeparator).append("-----END CERTIFICATE-----\n");
                    FileUtil.fileLinesWrite(certFilePath + overName, sb.toString(), false);
                    String totalContent=sb.toString();

                    //存储证书
                    HashMap<String, String> message = CertReader.analysisCerByContent(totalContent);

                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String update_time = simpleDateFormat.format(date);

                    //修改证书表
                    String sql = "insert into dev_cert(dev_hash,dev_name,username,dev_mac,dev_ip,org_id,dev_reg_time,dev_reg_status,tran_id,cert_name,cert_time,cert_sn,cert_csr,cert_cst,cert_key,cert_issuer,start_time,end_time) value (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    jdbcUtil.executeUpdate(sql, username,dev_name,username,dev_mac,device_ip,org_id,update_time,"1",tranId,username + ".crt",update_time,certSn, certReqPath + username + ".req", certFilePath + username + ".crt", priKeyPath + username + ".key",
                            message.get("issuer"), message.get("dateBefore"), message.get("dateAfter"));

                    //更新未注册设备中的记录
                    String sql1 = "update dev_unregister set status=1, examine_time =? where dev_hash = ?";
                    jdbcUtil.executeUpdate(sql1, update_time, username);
                    return new Result("证书签发成功", 200, "success");
                } else  {
                    String sql3 = "update dev_unregister set remarks = '证书签发失败' where dev_hash = ?";
                    jdbcUtil.executeUpdate(sql3, username);
                    return new Result("证书签发失败，请重新审核", 200, "error");
                }
            } else {
                String sql4 = "update dev_unregister set status = 2 where dev_hash = ?";
                jdbcUtil.executeUpdate(sql4,username);
                return new Result("已拒绝", 200, "failed");
            }
        }


    }

    public Result dec_Cert_Req_selfCA(DevRegisterRequest request){
        int is_handle = request.getIsHandle();
        String serial = request.getSerial();
        String dev_name = request.getDevName();
        String org_id = request.getOrgId();
        String device_ip = request.getDeviceIp();
        String dev_mac  = request.getDeviceMac();
        //获取HASH值
        String username = serial.substring(0,32);

        //随机生成流水号
        String tranId = idFactory.createTransId();
        //设备mac
        //String dev_mac = serial.substring(32);

        int ret = CAUtil.gen_CA(username);
        if(ret == 0) return new Result("签发证书出错",200,"error");

        String sql1 = "delete from unregister_device_copy1 where device_id = ?";
        jdbcUtil.executeUpdate(sql1, serial);

        String[] certInfo = CAUtil.get_CA(username);
        if(certInfo == null) return new Result("解析证书失败",200,"get_CA failed");

        Date date_start = null;
        Date date_end = null;
        try{
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date_start = ft.parse(certInfo[4]);
            date_end = ft.parse(certInfo[5]);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        List<Map<String,Object>> list = jdbcUtil.executeQuery("SELECT time FROM unregister_device_copy1 where device_id=?",serial);
        Date dev_reg_time = null;
        for (Map map:list){
            dev_reg_time = (Date) map.get("time");
        }

        String sql = "insert into device_cert_cp1 (dev_id,dev_name,username,dev_mac,device_ip,org_id,dev_reg_time,dev_reg_status,tran_id,cert_name,cert_time," +
                "cert_sn,cert_csr,cert_cst,cert_key,cert_issuer,start_time,end_time) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        int res = jdbcUtil.executeUpdate(sql,username,dev_name,username,dev_mac,device_ip,org_id,dev_reg_time,"1",tranId,username + "cer.pem",date_start,
                certInfo[1],username + "csr.pem","/home/workspace_yzk/CA_CEIT/newcers",username + "key.pem",certInfo[3],date_start,date_end);

        return new Result("生成证书",200,res);
    }

//    终端注册撤销
    public Result dev_Cert_Revoke(DeviceUnRegisterRequest request) {
        String username = request.getUsername();
        String dev_mac = request.getDevicaMac();
        //删除dev_cert中的记录（已认证设备详细信息）
        int ret = jdbcUtil.executeUpdate("delete from dev_cert where username = ?",username);
        if(ret ==0){
            return new Result("删除数据库失败",500,"error");
        }
        //将设备重新添加回dev_unregister中（未认证记录）
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        ret = jdbcUtil.executeUpdate("insert into dev_unregister (dev_hash,time,dev_mac) values (?,?,?) ",username,time,dev_mac);
        if(ret == 0){
            return new Result("加入未注册表失败",500,"error");
        }
        return new Result("已成功撤销",200,"success");
    }

    //软件注册
    public Result soft_Register(SoftRegisterRequest request)  {
        String soft_hash = request.getSoftHash();
        //判断软件是否已在软件仓库
        String selectSql = "select sw_name FROM soft_cert where sw_hash = ?";
        List<Map<String,Object>> list = jdbcUtil.executeQuery(selectSql,soft_hash);
        //软件重复
        if (list.size()!=0){
            return new Result("error",100,"该版本软件已存在，请上传其它版本。");
        }
        //软件不重复，申请证书
        int flag = 0;
        String gmssl = "";
        String exeDir = "";
        try{
            //获取gmssl路径（在配置文件设置）
            gmssl = fileConfigUtil.load("desktop.properties","gmssl.command");
            //设置私钥、请求文件、证书存储路径（本地测试环境都存在放同一个位置）
            exeDir = fileConfigUtil.load("desktop.properties","exeDir.path");
            //flag = Integer.parseInt(fileConfigUtil.load("desktop.properties","certApply"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (flag == 0 ){
            //生成私钥命令
            String createKeyPar =gmssl + " ecparam -genkey -name sm2p256v1 -out " + exeDir   + soft_hash + ".key";
            //生成请求文件命令
            String createCSRPar =gmssl + " req -new -sm3 -key \""+ exeDir +soft_hash+".key\" -out \""+ exeDir +soft_hash+".req\" -subj /CN="+soft_hash+"/C=CN/ST=BJ/L=NCEPU/O=CEIT/OU=CEIT";
            //生成证书命令
            String createCERPar = gmssl + " ca -md sm3 -in \""+ exeDir +soft_hash+".req\" -out "+ exeDir +soft_hash+".crt -days 3650 -batch";
            //生成私钥
            CommandUtil.exeCommand(createKeyPar);
            String pkiKeycontent = FileUtil.readFile(exeDir + soft_hash + ".key");
            if (pkiKeycontent==null||pkiKeycontent.equals("")){
                return new Result("error",400,"私钥生成失败");
            }
            //生成请求文件
            CommandUtil.exeCommand(createCSRPar);
            String reqcontent =  FileUtil.readFile(exeDir + soft_hash + ".req");
            if (reqcontent==null||reqcontent.equals("")){
                return new Result("error",400,"证书请求生成失败");
            }
            //生成证书
            CommandUtil.exeCommand(createCERPar);
            String crtcontent =  FileUtil.readFile(exeDir + soft_hash + ".crt");
            if (crtcontent==null||crtcontent.equals("")){
                return new Result("error",400,"证书生成失败");
            }

            return new Result("success",200,"注册成功");
        }
        else{
            //密服平台初始化
            AccessConfig.initConnect();
            //1.向密码服务平台发起请求
            //密码服务平台测试
            EquipmentCertReq certReq = new EquipmentCertReq();
            //生成流水号
            String tranId = idFactory.createTransId();
            certReq.setTransId(tranId);
            certReq.setServiceCode(soft_hash);
            //产生指定配置的私钥、请求文件
            try{
                GenCertReq req = new GenCertReq(soft_hash, priKeyPath, certReqPath);
                //调用命令行生成私钥

                CommandUtil.exeCommand(req.getGenPriKey());
                String pkiKeycontent = null;
                String reqcontent = null;

                pkiKeycontent =FileUtil.readFile(fileConfigUtil.load("desktop.properties","priKey.path") + soft_hash + ".key");

                if (pkiKeycontent==null||pkiKeycontent.equals("")){
                    return new Result("密钥对生成失败",400,"error");
                }
                //调用命令行生成证书请求文件
                CommandUtil.exeCommand(req.getGenReq());
                reqcontent = FileUtil.readFile(fileConfigUtil.load("desktop.properties","certReqFile.path") + soft_hash + ".req");

                if (reqcontent == null || reqcontent.equals("")) {
                    return new Result("证书请求生成失败", 400, "error");
                } else {
                    //密服平台证书请求文件不需要"-----BEGIN CERTIFICATE REQUEST-----"，"-----END CERTIFICATE REQUEST-----"
                    reqcontent = reqcontent.replace("-----BEGIN CERTIFICATE REQUEST-----", "").replace("-----END CERTIFICATE REQUEST-----", "").replace("\n", "");
                }
                certReq.setCertRequest(reqcontent);
                certReq.setRaId("device certReq");
            }catch (IOException e){
                e.printStackTrace();
            }

            ResultJson res;
            try {
                res = access.handleCertRequest(certReq);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result("error", 400, "error");
            }
//            String test = "{\"data\":\"{\\\"cert\\\":\\\"MIIC7jCCApKgAwIBAgISIAllltHkeFC8lDSYN1Ght+qpMAwGCCqBHM9VAYN1BQAwPzELMAkGA1UEBhMCQ04xDTALBgNVBAoMBFNHQ0MxDTALBgNVBAsMBEVQUkkxEjAQBgNVBAMMCVNNMi1DRVNISTAeFw0yMTA0MzAwMTM0MTRaFw0yMTA3MjkwMTM0MTRaMFExDTALBgNVBAMMBDA1MjIxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJHRDELMAkGA1UEBwwCR1oxCzAJBgNVBAoMAlNHMQwwCgYDVQQLDANDU0cwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATlNFyH4RV/xxFDSaFQqFngdkT43Cgl1li7wPaXBO3RMcSVhB7pURFr8W+9006Cb8lLbq+2UZ5ccHVgUXc0c5AAo4IBWDCCAVQwCwYDVR0PBAQDAgbAMAkGA1UdEwQCMAAwgZ0GA1UdHwSBlTCBkjBDoEGgP4Y9aHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcmwvU00yTkVXU0VDT05EL1NNMk5FV1NFQ09ORF8wLmNybDBLoEmgR4ZFaHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcmwvU00yTkVXU0VDT05EL2luYy9TTTJORVdTRUNPTkRfaW5jXzAuY3JsMB0GA1UdDgQWBBQwStr0cdS9tLsiEeBeCHwPNU1gCjAfBgNVHSMEGDAWgBQLwwLdX1ZtsGuCHA9vZffORUWhvzBaBgNVHSAEUzBRME8GCiqBHIbvMgYEAQEwQTA/BggrBgEFBQcCARYzaHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcHMvU00yTkVXU0VDT05EL2Nwcy5odG1sMAwGCCqBHM9VAYN1BQADSAAwRQIgX55+qvxNDFcSfHpM+2wQ9u7+/SfhXBIDgZJryoMuzUACIQC2M+chbn8K6pMdO0r7+MWajLM+joV5uwulEB6b9l5zog==\\\",\\\"certSn\\\":\\\"20096596d1e47850bc9434983751a1b7eaa9\\\"}\",\"message\":\"success\",\"status\":\"0\",\"transId\":\"149184c9\"}";
//            ResultJson res = ResultJson.getResult(test);
            Map<String, String> map;

            //success
            if (res.getStatus().equals("0")) {
                map = res.getData().get(0);
                String certContent = map.get("cert");
                String certSn = map.get("certSn");
                //产生证书
                String totalContent = conf.genCertFile(soft_hash, certContent);
                //存储证书
                HashMap<String, String> message = CertReader.analysisCerByContent(totalContent);

                return new Result("证书签发成功", 200, "success");
            } else {

                return new Result("证书签发失败，请重新审核", 400, "error");
            }
        }

    }


    //硬件认证
    public Result dev_Check(String hashCode,String deviceIp) {
        //首先使用硬件hash值查询
        String sql = "SELECT * FROM `dev_cert` WHERE dev_hash = ?";
        //"5F8934639BDF36D1B35BC04018DB1EF2"
        List<Map<String,Object>> list=jdbcUtil.executeQuery(sql,hashCode);

        //如果该硬件hash值不存在
        if (list.size()==0){
            //使用IP地址查询，判断硬件hash是否被修改
            String sqlForIp = "SELECT * FROM dev_cert WHERE dev_ip = ?";
            List<Map<String,Object>> listForIp=jdbcUtil.executeQuery(sqlForIp,deviceIp);
            if (listForIp.size()==0){
                return new Result("error",100,"该设备不存在");
            } else {
                Map<String,Object> mapForIP = listForIp.get(0);
                String insertForIP = "insert into dev_check_info(dev_name,dev_ip,time,type,description) values(?,?,?,?,?)";
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = simpleDateFormat.format(date);
                jdbcUtil.executeUpdate(insertForIP,mapForIP.get("dev_name"),deviceIp,time,"阻断","设备硬件存在异常");
                return new Result("error",100,"设备硬件异常");
            }
        }

        //如果硬件hash值存在
        for (Map map:list){
            //当终端hash值存在时，判断是否终端是否是首次认证
            if (map.get("dev_ip") == null){
                //首次认证时，需要将当前IP做为终端IP
                String updateIP = "update dev_cert set dev_ip = ? where dev_hash =?";
                jdbcUtil.executeUpdate(updateIP,deviceIp,hashCode);
            } else {
                //非首次认证，判断终端的硬件hash值与终端IP是否一致
                if (map.get("dev_ip")==deviceIp || map.get("dev_ip").equals(deviceIp)){
                    return new Result("seccess",200,"认证成功");
                } else {
                    String insertForIP = "insert into dev_check_info(dev_name,dev_ip,time,type,description) values(?,?,?,?,?)";
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = simpleDateFormat.format(date);
                    jdbcUtil.executeUpdate(insertForIP,map.get("dev_name"),deviceIp,time,"阻断","设备硬件存在异常");
                    return new Result("error",100,"终端IP或硬件异常");
                }
            }
        }

        return new Result("seccess",200,"认证成功");
    }

    //软件认证
//    public Result soft_Check() {
//    }

    //流量认证
//    public Result flow_Check(){
//    }
}
