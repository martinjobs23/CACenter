package com.ceit.desktop.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class FileConfigUtil {

    //获取配置文件。path为配置文件名称，key为关键字
    public String load(String propertiesName,String key) throws IOException {
        Properties properties = new Properties();
        //可以用两种不同的流来加载配置文件
        properties.load(FileConfigUtil.class.getClassLoader().getResourceAsStream(propertiesName));
        return (String)properties.get(key);
    }
}
