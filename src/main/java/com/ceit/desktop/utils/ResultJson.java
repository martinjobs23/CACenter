package com.ceit.desktop.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ResultJson
 * @Description TODO
 * @Author hello world
 * @DATE 2021/4/6  20:26
 **/
public class ResultJson {

	//流水号
	public String transId;
    //返回状态码
	public String status;
    //返回信息
	public String message;
    //返回数据对应Map的动态数组
	public List <Map <String, String>> data;

	public ResultJson() {
	}

	public ResultJson(String transId, String status, String message, List <Map <String, String>> data) {
		this.transId = transId;
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List <Map <String, String>> getData() {
		return data;
	}

	public void setData(List <Map <String, String>> data) {
		this.data = data;
	}

	/**
	 *
	 * @param result 要对平台返回的json字符进行处理，第一个去除{"data":"{ 中的：后的“     第二个去除{"data":"{。。。}"  }后的‘ ，第三个去除所有的转义字符\
	 * @return 正确的json对象
	 */
	public static ResultJson getResult(String result) {
		String res = result.replace("\"{", "{").replace("}\"", "}").replace("\\", "");
		//System.out.println("res: " + res);
		return JSONObject.parseObject(res,ResultJson.class);
	}

	@Override
	public String toString() {
		return "ResultJson{" +
				"transId='" + transId + '\'' +
				", status='" + status + '\'' +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}




}
