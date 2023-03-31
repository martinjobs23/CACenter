package com.ceit.desktop.utils;


import com.ceit.desktop.entity.*;
import com.uccs.Certservice;
import com.uccs.SessionEncKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xjyb.com.alibaba.fastjson.JSONObject;

import java.util.Date;


/**
 * @ClassName AccessConfig
 * @Description 前置对于密码服务平台进行交互，对于各种证书请求进行处理后进行RPC调用
 * @Author hello world
 * @DATE 2021/4/6  9:57
 **/

public class AccessConfig {

	private static final Logger log = LoggerFactory.getLogger(AccessConfig.class);

	//前置接入 用于证书载入和密钥初始化

	private static SessionEncKey sessionEncKey = SessionEncKey.getInstance();


	static long initTime  = 0;


	public static void initConnect() {
		int n = 0;
		boolean flag = false;
		String msg = "failed";
		Date date = new Date();
		//每30天初始化一次
		if (date.getTime()-initTime>30*24*3600*100){
			flag = connectPwdPlatForm();
			if (flag){
				initTime = date.getTime() ;
			}
			log.info("密服平台初始化。"+ " flag: "+ flag);
		}
	}

	public static  AccessConfig getInstance() {
		return Holder.instance;
	}



	/**
	 * 生成随机业务流水数、完成前置初始化操作
	 * @return 返回是否连接成功
	 */
	public static boolean connectPwdPlatForm() {
		boolean flag = false;
		ResultJson res = null;
		try {
			String transId = GenerateTransId.getInstance().createTransId();
			log.info("transId: "+ transId);
			String returnJson = sessionEncKey.initSecureKey(transId);
			log.info("前置初始化返回消息： "+ returnJson);
			res = JSONObject.parseObject(returnJson, ResultJson.class);
			System.out.println("connect res: "+ res);
			if(res.getStatus().equals("0")) {
				flag = true;
				log.info("The front connect to password platform is successful： "+res.getMessage());
			} else {
				log.info("The front connect to password platform is failed : " +res.getMessage());
			}
		} catch(RuntimeException e) {
			log.info("the Pre-call password platform is failed : "+e.getMessage());
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 设备证书制作
	 * @param equipmentCertReq 通过证书请求 P10 生成设备证书，适用于证书在终端设备中的应用
	 * @return 根据返回的封装类是否为空判断
	 */
	public ResultJson handleCertRequest(EquipmentCertReq equipmentCertReq) {
		ResultJson res = null;
		try {
			String returnJson = Certservice.getInstance().equipmentCert(equipmentCertReq.getTransId(),
					equipmentCertReq.getServiceCode(), equipmentCertReq.getCertRequest(),
					EquipmentCertReq.getTemplatePolicyID(), equipmentCertReq.getRaId());
			res = ResultJson.getResult(returnJson);
			log.info("设备证书制作 {}", res.toString());
		}  catch(RuntimeException e) {
			System.out.println("returnJson catch");
			e.printStackTrace();
			log.info("the Pre-call password platform is failed : {}", e.getMessage());
		}
		return res;
	}

	/**
	 * 证书验签
	 * @param "certSignVerify" 到密服平台对证书进行验签
	 * @return 根据返回的封装类是否为空判断
	 */
	public ResultJson certSignVerify(CertSignVerify certSignVerify){
		ResultJson res = null;
		try{
			String returnJson = Certservice.getInstance().signVerify(certSignVerify.getTransId(),certSignVerify.getServiceCode(),certSignVerify.getReqDoc(),certSignVerify.getSignValue(),certSignVerify.getSignCert());
			res = ResultJson.getResult(returnJson);
			log.info("证书验签 {}", res.toString());
		} catch (Exception e) {
			log.info("the password platform is Exception : {}", e.getMessage());
		}
		return res;
	}

	/**
	 *
	 * @param certUpdate 提供证书延期服务,用于证书更新
	 * @return 根据返回的封装类是否为空判断
	 */
	public ResultJson handleCertUpdate(CertUpdate certUpdate){
		ResultJson res = null;
		try {
			String returnJson = Certservice.getInstance().updateCert(certUpdate.getTransId(),
					certUpdate.getServiceCode(), certUpdate.getCst(), certUpdate.getCertSn(),
					certUpdate.getNewP10(), certUpdate.getPkcs7(), certUpdate.getRaId());
			res = ResultJson.getResult(returnJson);
			log.info("证书更新 {}", res.toString());

//			res = JSONObject.parseObject(returnJson, ResultJson.class);
//			log.info("The updateCert Request's message to password platform : {}", res.getMessage());
		}  catch(Exception e) {
			log.info("the Pre-call password platform is failed : {}", e.getMessage());
		}
		return res;

	}

	/**
	 * 用于业务证书吊销
	 * @param certRevoke 为用户提供吊销服务，禁止该证书进行业务操作
	 * @return 根据返回的封装类是否为空判断
	 */
	public ResultJson handleCertRevoke(CertRevoke certRevoke){
		ResultJson res = null;
		try {
			String returnJson = Certservice.getInstance().revokeCert(certRevoke.getTransId(),
					certRevoke.getServiceCode(), certRevoke.getCertSn(), certRevoke.getRevokeReason(),
					certRevoke.getRaId());
			res = ResultJson.getResult(returnJson);
			log.info("业务证书吊销 {}", res.toString());
		}  catch(Exception e) {
			log.info("the Pre-call password platform is failed : {}", e.getMessage());
		}
		return res;
	}

	/**
	 *
	 * @param crlDownload CRL 吊销列表下载
	 * @return 根据返回的封装类是否为空判断
	 */
	/*public ResultJson handleCrlDownload(CrlDownload crlDownload){
		ResultJson res = null;
		try {
			String returnJson = Certservice.getInstance().downloadCRL(crlDownload.getTransId(),
					crlDownload.getServiceCode(), CrlDownload.getTemplatePolicyID(),
					crlDownload.getProvinceID());
			res = ResultJson.getResult(returnJson);
			log.info("吊销列表下载 {}", res.toString());
		}  catch(Exception e) {
			log.info("the Pre-call password platform is failed : {}", e.getMessage());
		}
		return res;
	}*/

	/**
	 *
	 * @param certQuery  根据证书证书序列号，到密服平台查询证书相关信息。
	 * @return 根据返回的封装类是否为空判断
	 */
	/*public ResultJson handleQueryByCertSn(CertQuery certQuery){
		ResultJson res = null;
		try {
			String returnJson = Certservice.getInstance().queryCert(certQuery.getTransId(),
					certQuery.getCertSn());
			res = ResultJson.getResult(returnJson);
			log.info("查询证书 {}", returnJson);
		}  catch(Exception e) {
			log.info("the Pre-call password platform is failed : {}", e.getMessage());
		}
		return res;
	}*/

	/**
	 * 证书有效性验证
	 * @param "certStateQuery" 根据公钥证书，到密服平台查询证书是否为合法签发者签发，是否在有效期内以及是否被吊销
	 * @return 根据返回的封装类是否为空判断
	 */
	public ResultJson handleQueryCertState(CertStateQuery certStateQuery) {
		ResultJson res = null;
		try {
			String returnJson = Certservice.getInstance().queryCertState(certStateQuery.getTransId(),
					certStateQuery.getServiceCode(), certStateQuery.getCert(),
					certStateQuery.getProvinceID());
			res = ResultJson.getResult(returnJson);
			log.info("证书有效性验证 {}", res.toString());
		}  catch(Exception e) {
			log.info("the Pre-call password platform is failed : {}", e.getMessage());
		}
		return res;
	}


	private static final class Holder {
		private static final AccessConfig instance = new AccessConfig();

		private Holder() {
		}
	}

}
