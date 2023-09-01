package com.ceit.desktop.entity;

/**
 * @ClassName equipmentCertSetting
 * @Description 封装设备证书请求的实体类
 * @Author hello world
 * @DATE 2021/4/6  17:40
 **/
public class EquipmentCertReq {
	//流水号
	private String transId ;
	//产品编码
	private String serviceCode;
	//证书请求
	private String certRequest;
	//策略Id
	private static final String templatePolicyID = "SM2_ST";
	//备用字段
	private String raId;

	public EquipmentCertReq() {
	}

	public EquipmentCertReq(String transId, String serviceCode, String certRequest, String raId) {
		this.transId = transId;
		this.serviceCode = serviceCode;
		this.certRequest = certRequest;
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

	public String getCertRequest() {
		return certRequest;
	}

	public void setCertRequest(String certRequest) {
		this.certRequest = certRequest;
	}

	public static String getTemplatePolicyID() {
		return templatePolicyID;
	}

	public String getRaId() {
		return raId;
	}

	public void setRaId(String raId) {
		this.raId = raId;
	}

	@Override
	public String toString() {
		return "equipmentCertSetting{" +
				"transId='" + transId + '\'' +
				", serviceCode='" + serviceCode + '\'' +
				", certRequest(P10)='" + certRequest + '\'' +
				", raId='" + raId + '\'' +
				'}';
	}
}
