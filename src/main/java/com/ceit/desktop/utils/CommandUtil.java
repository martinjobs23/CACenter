package com.ceit.desktop.utils;


import org.apache.commons.exec.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;


/**
 *  Apache commons-exec对Process进行了封装：
 * 1、为Process的stdin, stdout, stderr重定向流，而不是File
 * 2、并发向Process stdin写入数据、读取Process stdout和stderr的数据，避免进程阻塞 提供非阻塞方法调用外部程序。
 * 3、超时终止Process
 * @author hello world
 * @since 2021-07-13
 * @version v1.0
 */
public class CommandUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommandUtil.class.getSimpleName());
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final Long TIMEOUT = 3000L;



    /**
     * 执行指定命令
     *
     * @param command 命令
     * @return 命令执行完成返回结果
     * @throws RuntimeException 失败时抛出异常，由调用者捕获处理
     */
    public synchronized static String exeCommand(String command) throws RuntimeException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            int exitCode = exeCommand(command, out);
            if (exitCode == 0) {
                System.out.println("命令运行成功:" + System.currentTimeMillis());
                logger.debug("命令运行成功:" + System.currentTimeMillis());
            } else {
                logger.debug("命令运行失败:" + System.currentTimeMillis());
            }
            System.out.println("out: " + out.toString());
            return out.toString(DEFAULT_CHARSET);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 执行指定命令，输出结果到指定输出流中
     *
     * @param command 命令
     * @param out     执行结果输出流
     * @return 执行结果状态码：执行成功返回0
     * @throws ExecuteException 失败时抛出异常，由调用者捕获处理
     * @throws IOException      失败时抛出异常，由调用者捕获处理
     */
    public synchronized static int exeCommand(String command, OutputStream out) throws ExecuteException, IOException {
        CommandLine commandLine = CommandLine.parse(command);
        System.out.println(commandLine);
        PumpStreamHandler pumpStreamHandler = null;
        if (null == out) {
            pumpStreamHandler = new PumpStreamHandler();
        } else {
            pumpStreamHandler = new PumpStreamHandler(out);
        }
        DefaultExecutor executor = new DefaultExecutor();
//        executor.setWorkingDirectory(new File("C:\\Users\\Administrator"));
        //设置流处理器
        executor.setStreamHandler(pumpStreamHandler);
        // 设置超时时间为3秒
        ExecuteWatchdog watchdog = new ExecuteWatchdog(TIMEOUT);
        executor.setWatchdog(watchdog);
        //设置命令执行退出值为0，如果命令成功执行并且没有错误，则返回0
        return executor.execute(commandLine);
    }

    public static void runCMD(String[] CMD) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(CMD);
            ByteArrayOutputStream resultOutStream = new ByteArrayOutputStream();
            InputStream errorInStream = new BufferedInputStream(process.getErrorStream());
            InputStream processInStream = new BufferedInputStream(process.getInputStream());
            int num = 0;
            byte[] bs = new byte[1024];
            while ((num = errorInStream.read(bs)) != -1) {
                resultOutStream.write(bs, 0, num);
            }
            while ((num = processInStream.read(bs)) != -1) {
                resultOutStream.write(bs, 0, num);
            }
            String result = new String(resultOutStream.toByteArray(), StandardCharsets.UTF_8);
            System.out.println(result);
            errorInStream.close();
            processInStream.close();
            resultOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) process.destroy();
        }
    }

    /**
     * https://www.cnblogs.com/chenmz1995/p/13401450.html
     * @param CMD
     * @param path
     */
    public static void runCmdOnDir(String[] CMD, String path) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(CMD, null, new File(path));
            ByteArrayOutputStream resultOutStream = new ByteArrayOutputStream();
            InputStream errorInStream = new BufferedInputStream(process.getErrorStream());
            InputStream processInStream = new BufferedInputStream(process.getInputStream());
            int num = 0;
            byte[] bs = new byte[1024];
            while ((num = errorInStream.read(bs)) != -1) {
                resultOutStream.write(bs, 0, num);
            }
            while ((num = processInStream.read(bs)) != -1) {
                resultOutStream.write(bs, 0, num);
            }
            String result = new String(resultOutStream.toByteArray(), StandardCharsets.UTF_8);
            System.out.println(result);
            errorInStream.close();
            processInStream.close();
            resultOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) process.destroy();
        }
    }




//    public static void main(String[] args) throws Exception {
//        String commonName = "tf0012";
//        Dn dn = new Dn(commonName);
//        GenCertReq genCertReq = new GenCertReq(commonName, "/opt/ceitsa/priKey/", "/opt/ceitsa/certReq/");
//        System.out.println(genCertReq);
//        String priKey = FileUtil.readFile("src/main/resources/template/0522.key");
//        System.out.println(priKey);
//        String req = FileUtil.readFile("src/main/resources/template/0522.csr");
//        System.out.println(req);
////        String line = "java -version";
////        CommandLine cmdLine = CommandLine.parse(line);
////        DefaultExecutor executor = new DefaultExecutor();
////        int exitValue = executor.execute(cmdLine);
////        System.out.println("exit value = " + exitValue);
////
////        logger.info(exeCommand(line));
//
//
////        String result = execCmd("ecparam -genkey -name SM2  -text -out ./privateKey/pri2.key", new File("E:\\IDEA-workspace\\TLVpn\\sec_admin\\tool\\gmssl.exe"));
//
//
//    }


}
