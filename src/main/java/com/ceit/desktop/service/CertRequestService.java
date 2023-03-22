package com.ceit.desktop.service;


import com.ceit.desktop.entity.EquipmentCertReq;
import com.ceit.desktop.grpc.caCenter.DeviceRegisterRequest;
import com.ceit.desktop.grpc.caCenter.DeviceUnRegisterRequest;
import com.ceit.desktop.grpc.caCenter.SoftRegisterRequest;
import com.ceit.desktop.utils.*;
import com.ceit.ioc.annotations.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

    private String priKeyPath = System.getProperty("priKey.path");

    private String certReqPath = System.getProperty("certReqFile.path");

    private String certFilePath = System.getProperty("certFile.path");

    private static final String serviceCode = "CEIT";

    private static GenerateTransId idFactory =  GenerateTransId.getInstance();

    JdbcUtil jdbcUtil = new JdbcUtil();

    //prod
    public static AccessConfig access = AccessConfig.getInstance();

    //终端注册
    public Result dev_Cert_Req(DeviceRegisterRequest request) {
        int is_handle = request.getIsHandle();
        String serial = request.getSerial();
        String dev_name = request.getDevName();
        String org_id = request.getOrgId();
        String device_ip = request.getDeviceIp();
        //获取HASH值
        String username = serial.substring(0,32);

        int flag = Integer.parseInt(System.getProperty("certApply"));
        if (flag == 0 ){
            //管理员同意注册
            if (is_handle ==1 ){
                String gmssl = "";
                String exeDir = "";

                try{
                    //获取gmssl路径（在配置文件设置）
                    gmssl = fileConfigUtil.load("desktop.properties","gmssl.command");
                    //设置私钥、请求文件、证书存储路径（本地测试环境都存在放同一个位置）
                    exeDir = fileConfigUtil.load("desktop.properties","exeDir.path");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //随机生成流水号
                String tranId = idFactory.createTransId();
                //设备mac
                String device_mac = serial.substring(32);
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

                List<Map<String,Object>> list = jdbcUtil.executeQuery("SELECT time FROM unregister_device where device_id=?",serial);
                Date dev_reg_time = null;
                for (Map map:list){
                    dev_reg_time = (Date) map.get("time");
                }
                jdbcUtil.executeUpdate("delete from unregister_device where device_id = ?",serial);
//            jdbcUtil.executeUpdate("insert into radcheck (username,attribute,op,`value`) values (?,'Cleartext-Password',':=',?) ",username,username.substring(0,16));
                jdbcUtil.executeUpdate("insert into device_cert (username,tran_id,cert_name,cert_time,device_mac,dev_name,org_id,dev_reg_time,dev_id,device_ip,dev_reg_status) values (?,?,?,?,?,?,?,?,?,?,?) ",
                        username,tranId,serial+".crt",cert_time,device_mac,dev_name,org_id,dev_reg_time,username,device_ip,"1");
                return new Result("证书生成成功",200,"success");
            } else {
                jdbcUtil.executeUpdate("update unregister_device set status = 2 where device_id = ? ",serial);
                return new Result("已拒绝",200,"failed");
            }
        } else {
            //1.审核同意
            if (is_handle == 1) {
                //密服平台初始化
                AccessConfig.initConnect();
                //1.向密码服务平台发起请求
                //获取设备MAC
                String device_mac = serial.substring(32);

                //密码服务平台测试
                EquipmentCertReq certReq  = new EquipmentCertReq();
                //生成流水号
                String tranId = idFactory.createTransId();
                certReq.setTransId(tranId);
                //产品编码为“设备序列号+设备mac”
                certReq.setServiceCode(username);
                //产生指定配置的私钥、请求文件
                GenCertReq req = new GenCertReq(username, priKeyPath, certReqPath);
                //调用命令行生成私钥
                CommandUtil.exeCommand(req.getGenPriKey());
                String pkiKeycontent = FileUtil.readFile(System.getProperty("priKey.path") + username + ".key");
                if (pkiKeycontent==null||pkiKeycontent.equals("")){
                    return new Result("密钥对生成失败",400,"error");
                }
                //调用命令行生成证书请求文件
                CommandUtil.exeCommand(req.getGenReq());
                String reqcontent =  FileUtil.readFile(System.getProperty("certReqFile.path") + username + ".req");
                if (reqcontent==null||reqcontent.equals("")){
                    return new Result("证书请求生成失败",400,"error");
                } else {
                    //密服平台证书请求文件不需要"-----BEGIN CERTIFICATE REQUEST-----"，"-----END CERTIFICATE REQUEST-----"
                    reqcontent = reqcontent.replace("-----BEGIN CERTIFICATE REQUEST-----","").replace("-----END CERTIFICATE REQUEST-----","").replace("\n","");
                }
                certReq.setCertRequest(reqcontent);
                certReq.setRaId("device certReq");

                /**
                 * //固定信息返回
                 * String test = "{\"data\":\"{\\\"cert\\\":\\\"MIIC7jCCApKgAwIBAgISIAllltHkeFC8lDSYN1Ght+qpMAwGCCqBHM9VAYN1BQAwPzELMAkGA1UEBhMCQ04xDTALBgNVBAoMBFNHQ0MxDTALBgNVBAsMBEVQUkkxEjAQBgNVBAMMCVNNMi1DRVNISTAeFw0yMTA0MzAwMTM0MTRaFw0yMTA3MjkwMTM0MTRaMFExDTALBgNVBAMMBDA1MjIxCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJHRDELMAkGA1UEBwwCR1oxCzAJBgNVBAoMAlNHMQwwCgYDVQQLDANDU0cwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATlNFyH4RV/xxFDSaFQqFngdkT43Cgl1li7wPaXBO3RMcSVhB7pURFr8W+9006Cb8lLbq+2UZ5ccHVgUXc0c5AAo4IBWDCCAVQwCwYDVR0PBAQDAgbAMAkGA1UdEwQCMAAwgZ0GA1UdHwSBlTCBkjBDoEGgP4Y9aHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcmwvU00yTkVXU0VDT05EL1NNMk5FV1NFQ09ORF8wLmNybDBLoEmgR4ZFaHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcmwvU00yTkVXU0VDT05EL2luYy9TTTJORVdTRUNPTkRfaW5jXzAuY3JsMB0GA1UdDgQWBBQwStr0cdS9tLsiEeBeCHwPNU1gCjAfBgNVHSMEGDAWgBQLwwLdX1ZtsGuCHA9vZffORUWhvzBaBgNVHSAEUzBRME8GCiqBHIbvMgYEAQEwQTA/BggrBgEFBQcCARYzaHR0cDovLzEwLjg1LjE4My4zODoxOTA5MC9jcHMvU00yTkVXU0VDT05EL2Nwcy5odG1sMAwGCCqBHM9VAYN1BQADSAAwRQIgX55+qvxNDFcSfHpM+2wQ9u7+/SfhXBIDgZJryoMuzUACIQC2M+chbn8K6pMdO0r7+MWajLM+joV5uwulEB6b9l5zog==\\\",\\\"certSn\\\":\\\"20096596d1e47850bc9434983751a1b7eaa9\\\"}\",\"message\":\"success\",\"status\":\"0\",\"transId\":\"149184c9\"}";
                 */


                ResultJson res;
                try {
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
                    String totalContent = conf.genCertFile(username, certContent);

                    //存储证书
                    HashMap<String, String> message = CertReader.analysisCerByContent(totalContent);

                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String update_time = simpleDateFormat.format(date);

                    //修改证书表
                    String sql = "insert into device_cert(dev_id,dev_name,username,device_mac,device_ip,org_id,dev_reg_time,dev_reg_status,tran_id,cert_name,cert_time,cert_sn,cert_csr,cert_cst,cert_key,cert_issuer,start_time,end_time) value (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    jdbcUtil.executeUpdate(sql, username,dev_name,username,device_mac,device_ip,org_id,update_time,"1",tranId,username + ".crt",update_time,certSn, certReqPath + username + ".req", certFilePath + username + ".crt", priKeyPath + username + ".key",
                            message.get("issuer"), message.get("dateBefore"), message.get("dateAfter"));
                    //删除未注册设备中的记录
                    String sql1 = "delete from unregister_device where device_id = ?";
                    jdbcUtil.executeUpdate(sql1, serial);
                    return new Result("证书签发成功", 200, "success");
                } else  {
                    String sql3 = "update unregister_device set remarks = '证书签发失败' where device_id = ?";
                    jdbcUtil.executeUpdate(sql3, serial);
                    return new Result("证书签发失败，请重新审核", 200, "error");
                }
            } else {
                String sql4 = "update unregister_device set status = 2 where device_id = ?";
                jdbcUtil.executeUpdate(sql4,serial);
                return new Result("已拒绝", 200, "success");
            }
        }


    }

//    终端注册撤销
    public Result dev_Cert_Revoke(DeviceUnRegisterRequest request) {
        String username = request.getUsername();
        String device_mac = request.getDevicaMac();
        //删除radcheck中记录（已认证记录）
        jdbcUtil.executeUpdate("delete from radcheck where username = ?",username);
        //删除device_cert中的记录（已认证设备详细信息）
        jdbcUtil.executeUpdate("delete from device_cert where username = ?",username);
        //将设备重新添加回unregister_device中（未认证记录）
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        String device_id = username+device_mac;
        int ret = jdbcUtil.executeUpdate("insert into unregister_device (device_id,time) values (?,?) ",device_id,time);
        return new Result("已成功撤销",200,"success");
    }

    //软件注册
    public Result soft_Register(SoftRegisterRequest request) {
        String soft_hash = request.getSoftHash();
        //判断软件是否已在软件仓库
        String selectSql = "select sw_name FROM soft_cert where sw_hash = ?";
        List<Map<String,Object>> list = jdbcUtil.executeQuery(selectSql,soft_hash);
        //软件重复
        if (list.size()!=0){
            return new Result("error",100,"该版本软件已存在，请上传其它版本。");
        }
        //软件不重复，申请证书

        String gmssl = "";
        String exeDir = "";
        try{
            //获取gmssl路径（在配置文件设置）
            gmssl = fileConfigUtil.load("desktop.properties","gmssl.command");
            //设置私钥、请求文件、证书存储路径（本地测试环境都存在放同一个位置）
            exeDir = fileConfigUtil.load("desktop.properties","exeDir.path");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            return new Result("私钥生成失败",400,"error");
        }
        //生成请求文件
        CommandUtil.exeCommand(createCSRPar);
        String reqcontent =  FileUtil.readFile(exeDir + soft_hash + ".req");
        if (reqcontent==null||reqcontent.equals("")){
            return new Result("证书请求生成失败",400,"error");
        }
        //生成证书
        CommandUtil.exeCommand(createCERPar);
        String crtcontent =  FileUtil.readFile(exeDir + soft_hash + ".crt");
        if (crtcontent==null||crtcontent.equals("")){
            return new Result("证书生成失败",400,"error");
        }

        return new Result("success",200,"注册成功");
    }

    //硬件认证
    public Result dev_Check(String hashCode,String deviceIp) {
        //首先使用硬件hash值查询
        String sql = "SELECT * FROM `device_cert` WHERE dev_id = ?";
        //"5F8934639BDF36D1B35BC04018DB1EF2"
        List<Map<String,Object>> list=jdbcUtil.executeQuery(sql,hashCode);

        //如果该硬件hash值不存在
        if (list.size()==0){
            //使用IP地址查询，判断硬件hash是否被修改
            String sqlForIp = "SELECT * FROM device_cert WHERE device_ip = ?";
            List<Map<String,Object>> listForIp=jdbcUtil.executeQuery(sqlForIp,deviceIp);
            if (listForIp.size()==0){
                return new Result("error",100,"该设备不存在");
            } else {
                Map<String,Object> mapForIP = listForIp.get(0);
                String insertForIP = "insert into dev_check_info(device_name,device_ip,time,type,description) values(?,?,?,?,?)";
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
            if (map.get("device_ip") == null){
                //首次认证时，需要将当前IP做为终端IP
                String updateIP = "update device_cert set device_ip = ? where dev_id =?";
                jdbcUtil.executeUpdate(updateIP,deviceIp,hashCode);
            } else {
                //非首次认证，判断终端的硬件hash值与终端IP是否一致
                if (map.get("device_ip")==deviceIp || map.get("device_ip").equals(deviceIp)){
                    return new Result("seccess",200,"认证成功");
                } else {
                    String insertForIP = "insert into dev_check_info(device_name,device_ip,time,type,description) values(?,?,?,?,?)";
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
