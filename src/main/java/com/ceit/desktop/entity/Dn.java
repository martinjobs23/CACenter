package com.ceit.desktop.entity;


/**
 * @ClassName Dn 里面固定内容待定
 * @Description 生成请求文件时所必需的信息
 * @Author hello world
 * @DATE 2021/4/30  10:41
 **/
public class Dn {


	private static final String C = "CN";

	private static final String ST = "BJ";

	private static final String L = "BJ";

	private static final String O = "BJDKY";

	private static final String OU = "DKY";

	private String CN;

	public Dn(String CN) {
		this.CN = CN;
	}

	public String getC() {
		return C;
	}

	public String getST() {
		return ST;
	}

	public String getL() {
		return L;
	}

	public String getO() {
		return O;
	}

	public String getOU() {
		return OU;
	}

	public String getCN() {
		return CN;
	}

	public void setCN(String CN) {
		this.CN = CN;
	}

	@Override
//	public String toString() {
//
//		return  "C=" + C +
//				", ST= " + ST  +
//				", L= " + L +
//				", O= " + O  +
//				", OU= "  + OU +
//				", CN= " + CN ;
//	}
	public String toString() {

		return  "/CN=" + CN +
				"/C=" + C +
				"/ST=" + ST  +
				"/L=" + L +
				"/O=" + O  +
				"/OU="  + OU ;
	}

	//密服平台P10证书请求文件测试
//	public String toString() {
//
//		return  "CN="+ CN+
//				",C="+C+
//				",ST="+ST+
//				",L="+L+
//				",O="+O+
//				",OU="+OU+";";
//	}
}
