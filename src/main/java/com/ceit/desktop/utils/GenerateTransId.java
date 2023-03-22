package com.ceit.desktop.utils;

import org.apache.commons.lang.math.RandomUtils;

/**
 * @ClassName GenerateTransId
 * @Description 调用方法 ： GenerateTransId.getInstance().CreateTransId()  依循 类名.getInstance().方法名。
 * @Author hello world
 * @DATE 2021/4/1  16:05
 **/
public class GenerateTransId {
	//private Logger log = LoggerFactory.getLogger(GenerateTransId.class);

	private GenerateTransId() {
	}

	/**
	 * 静态内部类实现单例模式
	 * @return 利用了类加载机制，懒汉式且线程安全
	 */
	public static GenerateTransId getInstance() {
		return Holder.instance;
	}

	/**
	 * 产生10位随机数，randomNumeric也可以限定长度，16进制随机数要求不超过128位即可
	 * @return 64位系统返回小写8位十六进制数,最大+2147483647，32位最大有符号数
	 */
	public String createTransId() {
		String hexStr = "";
		try {
			int randomString = RandomUtils.nextInt();
			hexStr = Integer.toHexString(randomString);

		} catch(Exception var1) {
			//log.error("transId is null");
		}
		return hexStr;
	}

	private static final class Holder {
		private static final GenerateTransId instance = new GenerateTransId();
		private Holder() {
		}
	}

//	public static void main(String[] args) {
//		String test = GenerateTransId.getInstance().CreateTransId();
//		System.out.println(test);
//	}





}
