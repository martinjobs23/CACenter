package com.ceit.desktop.utils;

//import com.ceit.ioc.annotations.Component;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName ClientConf
 * @Description 所有读写配置文件的操作都放在这里，包括一些定时操作  想明白ccd文件和pf文件的修改时机
 * ccd ：由于IP是自动分配的，因此ccd文件的创建和修改只有在创建新终端的时候修改一次
 * pf ： 设置了终端对应commonName的pf目录下的文件   修改时机：  1、终端创建时、删除时  2、 终端分组业务授权时需要修改该终端租下的所有终端对应的pf文件（可能会很耗时） 3、再给资源分组设置权限时（新增、修改、删除）也需要修改
 * @Author hello world
 * @DATE 2021/5/25  22:44
 **/
//@Component
public class ClientConf {

	private String path;

	private String packageFilter;
	private FileConfigUtil fileConfigUtil = new FileConfigUtil();

	//证书文件生成
	private String certFilePath = fileConfigUtil.load("desktop.properties","certFile.path");

	//证书文件生成
	private String keyFilePath;


	//ccd虚拟网卡IP配置  开头结尾
	private static final String cmd = "ifconfig-push";

	//pf文件的开头，结尾
	private static final String begin = "[CLIENTS DROP]\n"  + "[SUBNETS DROP]\n";

	private static final String end = "[END]";

	//证书文件开头结尾
	private static final String certBegin = "-----BEGIN CERTIFICATE-----\n";

	private static final String certEnd = "-----END CERTIFICATE-----\n";

	public ClientConf() throws IOException {
	}

	/**
	 *
	 * @param commonName 拿commonName作为文件名
	 * @param certContent
	 * @return 证书内容
	 * 写文件
	 */
	public String genCertFile (String commonName, String certContent) {
		String overName = commonName + ".crt";
		//设置系统换行符 默认linux下
		String lineSeparator = System.getProperty("line.separator", "\n");
		StringBuilder sb = new StringBuilder(certBegin);
		sb.append(certContent).append(lineSeparator).append(certEnd);
		FileUtil.fileLinesWrite(certFilePath + overName, sb.toString(), false);
		return sb.toString();
	}

	/**
	 * 仅用作测试
	 * @param commonName 拿commonName作为文件名
	 * @param keyContent
	 * @return 证书内容
	 */
	public String genKeyFile(String commonName, String keyContent) {
		String overName = commonName + ".key";
		//设置系统换行符 默认linux下
		//String lineSeparator = System.getProperty("line.separator", "\n");
		FileUtil.fileLinesWrite(keyFilePath + overName, keyContent, false);
		return keyContent;
	}

	/**
	 *
	 * @param commonName 拿commonName作为文件名
	 * @param certContent
	 * @return 证书内容
	 */
	public String genPriKeyFile(String commonName, String certContent) {
		String overName = commonName + ".key";
		//设置系统换行符 默认linux下
		String lineSeparator = System.getProperty("line.separator", "\n");
		StringBuilder sb = new StringBuilder(certBegin);
		sb.append(certContent).append(lineSeparator).append(certEnd);
		FileUtil.fileLinesWrite(certFilePath + overName, sb.toString(), false);
		return sb.toString();
	}



	/**
	 *
	 * @param ip
	 * @param mask
	 * @param clientName 更新时会修改ccd目录下配置文件
	 * @return
	 */
	public String CreateOrUpdateConf(String ip, String mask, String clientName) {
		return FileUtil.fileLinesWrite(path + clientName, cmd + " " + ip + " " + mask, false);
	}

	public boolean delConf(String clientName) {
		return FileUtil.deleteFile(path + clientName);
	}


	/**
	 * 对应两种情况 ：
	 * 1、保存授权 这种情况是改完一个终端组对应的资源组
	 * 2.批量生成终端时
	 * 这里偷了懒，跟新也是全部查询，在更新文件，而不是在文件里该，可以优化
	 * 又以一个问题：这里传入的list应该是 用户组 === 资源组 === 访问策略列表 control_rule
	 * @param resource 用户组对应资源组里面的 访问策略列表
	 * @param clientName 必须跟证书里面的common Name保持一致
	 * @return
	 */
	public String CreateOrUpdatePf(List <String> resource, String clientName) {
		String overName = clientName + ".pf";
		//设置系统换行符 默认linux下
		String lineSeparator = System.getProperty("line.separator", "\n");
		StringBuilder sb = new StringBuilder(begin);
		for(String subnet : resource) {
			sb.append("+").append(subnet).append(lineSeparator);
		}
		sb.append(end);
		return FileUtil.fileLinesWrite(packageFilter + overName, sb.toString(), false);
	}

	/**
	 *
	 * @param clientName 未编码之前的
	 * @return
	 */
	public  boolean delPf(String clientName) {
		String overName = clientName + ".pf";
		return FileUtil.deleteFile(packageFilter + overName);
	}

//	public String CreatePfMiddle(String businessGroupName, List <String> nameList, List<String> rules) {
//		//毫秒计数的时间戳
//		long currentTime = System.currentTimeMillis();
//		FileUtil.fileLinesWrite(template + businessGroupName + currentTime, nameList, )
//	}
}
