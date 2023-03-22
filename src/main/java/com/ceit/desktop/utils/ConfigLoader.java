package com.ceit.desktop.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class ConfigLoader {
    public ConfigLoader() {
    }

    public static void load(String path) {
        try {
            InputStream ips = new FileInputStream(path);
            Properties props = new Properties();
            props.load(ips);
            ips.close();
            Iterator it = props.entrySet().iterator();

            while(it.hasNext()) {
                Map.Entry<Object, Object> entry = (Map.Entry)it.next();
                System.setProperty((String)entry.getKey(), (String)entry.getValue());
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            System.exit(0);
        }

    }

    public static void loadDefaultConfig() {
        String path = ConfigLoader.class.getResource("/").getPath();
        System.out.println("loadDefaultConfig: " + path + "application.properties");
        System.setProperty("path", path);
        File propertiesFile = new File(path);
        String[] list = propertiesFile.list();
        String[] var3 = list;
        int var4 = list.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String properties = var3[var5];
            String filePath = path + properties;
            new File(filePath);
            if ((new File(path + properties)).isFile() && properties.endsWith(".properties")) {
                System.out.println(filePath);
                load(filePath);
            }
        }

    }
}