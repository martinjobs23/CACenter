package com.ceit.desktop.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * <p>文件操作工具类<p>
 * @version 1.0
 * @author li_hao
 * @date 2017年1月18日
 */
public class FileUtil {

	/**
	 * 获取windows/linux的项目根目录
	 * @return
	 */
	public static String getConTextPath(){
		String fileUrl = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		if("usr".equals(fileUrl.substring(1,4))){
			fileUrl = (fileUrl.substring(0,fileUrl.length()-16));//linux
		}else{
			fileUrl = (fileUrl.substring(1,fileUrl.length()-16));//windows
		}
		return fileUrl;
	}

	/**
	 * 字符串转数组
	 * @param str 字符串
	 * @param splitStr 分隔符
	 * @return
	 */
	public static String[] stringToArray(String str,String splitStr){
		String[] arrayStr = null;
		if(!"".equals(str) && str != null){
			if(str.contains(splitStr)){
				arrayStr = str.split(splitStr);
			}else{
				arrayStr = new String[1];
				arrayStr[0] = str;
			}
		}
		return arrayStr;
	}

	/**
	 * 换行读取文件 by zfl
	 *
	 * @param Path
	 * @return
	 */
	public static String readFile(String Path) {
		BufferedReader reader = null;
		StringBuilder laststr = new StringBuilder();
		try {
			BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(Path));
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr.append(tempString).append('\n');
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return laststr.toString();
	}

	/**
	 * 获取文件夹下所有文件的名称 + 模糊查询（当不需要模糊查询时，queryStr传空或null即可）
	 * 1.当路径不存在时，map返回retType值为1
	 * 2.当路径为文件路径时，map返回retType值为2，文件名fileName值为文件名
	 * 3.当路径下有文件夹时，map返回retType值为3，文件名列表fileNameList，文件夹名列表folderNameList
	 * @param folderPath 路径
	 * @param queryStr 模糊查询字符串
	 * @return
	 */
	public static HashMap<String, Object> getFilesName(String folderPath , String queryStr) {
		HashMap<String, Object> map = new HashMap<>();
		List<String> fileNameList = new ArrayList<>();//文件名列表
		List<String> folderNameList = new ArrayList<>();//文件夹名列表
		File f = new File(folderPath);
		if (!f.exists()) { //路径不存在
			map.put("retType", "1");
		}else{
			boolean flag = f.isDirectory();
			if(flag==false){ //路径为文件
				map.put("retType", "2");
				map.put("fileName", f.getName());
			}else{ //路径为文件夹
				map.put("retType", "3");
				File fa[] = f.listFiles();
				queryStr = queryStr==null ? "" : queryStr;//若queryStr传入为null,则替换为空（indexOf匹配值不能为null）
				for (File fs : fa) {
					if (fs.getName().indexOf(queryStr) != -1) {
						if (fs.isDirectory()) {
							folderNameList.add(fs.getName());
						} else {
							fileNameList.add(fs.getName());
						}
					}
				}
				map.put("fileNameList", fileNameList);
				map.put("folderNameList", folderNameList);
			}
		}
		return map;
	}

	/**
	 * 以行为单位读取文件，读取到最后一行
	 * @param filePath
	 * @return
	 */
	public static List<String> readFileContent(String filePath) {
		BufferedReader reader = null;
		List<String> listContent = new ArrayList<>();
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				listContent.add(tempString);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return listContent;
	}

	/**
	 * 读取指定行数据 ，注意：0为开始行
	 * @param filePath
	 * @param lineNumber
	 * @return
	 */
	public static String readLineContent(String filePath,int lineNumber){
		BufferedReader reader = null;
		String lineContent="";
		try {
			reader = new BufferedReader(new FileReader(filePath));
			int line=0;
			while(line<=lineNumber){
				lineContent=reader.readLine();
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return lineContent;
	}

	/**
	 * 读取从beginLine到endLine数据（包含beginLine和endLine），注意：0为开始行
	 * @param filePath
	 * @param beginLineNumber 开始行
	 * @param endLineNumber 结束行
	 * @return
	 */
	public static List<String> readLinesContent(String filePath,int beginLineNumber,int endLineNumber){
		List<String> listContent = new ArrayList<>();
		try{
			int count = 0;
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String content = reader.readLine();
			while(content !=null){
				if(count >= beginLineNumber && count <=endLineNumber){
					listContent.add(content);
				}
				content = reader.readLine();
				count++;
			}
		} catch(Exception e){
		}
		return listContent;
	}

	/**
	 * 读取若干文件中所有数据
	 * @param listFilePath
	 * @return
	 */
	public static List<String> readFileContent_list(List<String> listFilePath) {
		List<String> listContent = new ArrayList<>();
		for(String filePath : listFilePath){
			File file = new File(filePath);
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String tempString = null;
				int line = 1;
				// 一次读入一行，直到读入null为文件结束
				while ((tempString = reader.readLine()) != null) {
					listContent.add(tempString);
					line++;
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e1) {
					}
				}
			}
		}
		return listContent;
	}

	/**
	 * 文件数据写入（如果文件夹和文件不存在，则先创建，再写入） 因为想更新、创建都用一个方法需要FileWriter(file, flag)
	 * @param filePath
	 * @param content
	 * @param flag true:如果文件存在且存在内容，则内容换行追加；false:如果文件存在且存在内容，则内容替换
	 */
	public static String fileLinesWrite(String filePath, String content, boolean flag){
		String filedo = "write";
		BufferedOutputStream buf = null;
		try {
			File file=new File(filePath);
			//如果文件夹不存在，则创建文件夹
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			//如果文件不存在，则创建文件,写入第一行内容
			if(!file.exists()) {
				file.createNewFile();
				buf = new BufferedOutputStream(new FileOutputStream(file));
				filedo = "create";
			} else {
				buf = new BufferedOutputStream(new FileOutputStream(file, flag));  //如果文件存在,则追加或替换内容
			}

			//写入文件
			buf.write(content.getBytes(StandardCharsets.UTF_8));
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filedo;
	}

	/**
	 * 写文件
	 * @param ins
	 * @param out
	 */
	public static void writeIntoOut(InputStream ins, OutputStream out) {
		byte[] bb = new byte[10 * 1024];
		try {
			int cnt = ins.read(bb);
			while (cnt > 0) {
				out.write(bb, 0, cnt);
				cnt = ins.read(bb);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				ins.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断list中元素是否完全相同（完全相同返回true,否则返回false）
	 * @param list
	 * @return
	 */
	private static boolean hasSame(List<? extends Object> list){
		if(null == list)
			return false;
		return 1 == new HashSet<Object>(list).size();
	}

	/**
	 * 判断list中是否有重复元素（无重复返回true,否则返回false）
	 * @param list
	 * @return
	 */
	private static boolean hasSame2(List<? extends Object> list){
		if(null == list)
			return false;
		return list.size() == new HashSet<Object>(list).size();
	}

	/**
	 * 增加/减少天数
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date DateAddOrSub(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}


	/**
	 * 递归删除文件或者目录
	 * @param file_path
	 */
	public static void deleteEveryThing(String file_path) {
		try{
			File file=new File(file_path);
			if(!file.exists()){
				return ;
			}
			if(file.isFile()){
				file.delete();
			}else{
				File[] files = file.listFiles();
				for(int i=0;i<files.length;i++){
					String root=files[i].getAbsolutePath();//得到子文件或文件夹的绝对路径
					deleteEveryThing(root);
				}
				file.delete();
			}
		} catch(Exception e) {
			System.out.println("删除文件失败");
		}
	}
	/**
	 * 创建目录
	 * @param dir_path
	 */
	public static void mkDir(String dir_path) {
		File myFolderPath = new File(dir_path);
		try {
			if (!myFolderPath.exists()) {
				myFolderPath.mkdir();
			}
		} catch (Exception e) {
			System.out.println("新建目录操作出错");
			e.printStackTrace();
		}
	}


	/**
	 * 判断指定的文件是否存在。
	 *
	 * @param fileName
	 * @return
	 */
	public static boolean isFileExist(String fileName) {
		return new File(fileName).isFile();
	}

	/* 得到文件后缀名
	 *
	 * @param fileName
	 * @return
	 */
	public static String getFileExt(String fileName) {
		int point = fileName.lastIndexOf('.');
		int length = fileName.length();
		if (point == -1 || point == length - 1) {
			return "";
		} else {
			return fileName.substring(point + 1, length);
		}
	}

	/**
	 * 删除文件夹及其下面的子文件夹
	 *
	 * @param dir
	 * @throws IOException
	 */
	public static void deleteDir(File dir) throws IOException {
		if (dir.isFile())
			throw new IOException("IOException -> BadInputException: not a directory.");
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile()) {
					file.delete();
				} else {
					deleteDir(file);
				}
			}
		}
		dir.delete();
	}


	/**
	 * 删除单个文件
	 * @param   sPath    被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			flag = file.delete();
		}
		return flag;
	}



	/**
	 * 复制文件
	 *
	 * @param src
	 * @param dst
	 * @throws Exception
	 */
	public static void copy (File src, File dst) throws Exception {
		int BUFFER_SIZE = 4096;
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				in = null;
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				out = null;
			}
		}
	}

}