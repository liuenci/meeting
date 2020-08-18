package com.stylefeng.guns.core.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @program: meeting
 * @description: FTP工具类
 * @author: liuenci
 * @create: 2020-08-03 18:44
 **/
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "ftp")
public class FTPUtils {
    private String hostName;
    private Integer port;
    private String userName;
    private String password;

    private FTPClient ftpClient = null;

    private void initFTPClient() {

        try {
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding("utf-8");
            ftpClient.connect(hostName, port);
            ftpClient.login(userName, password);
        } catch (IOException e) {
            log.error("初始化FTP失败", e);
            e.printStackTrace();
        } finally {
        }
    }

    public String getFileStrByAddress(String fileAddress) {
        BufferedReader bufferedReader = null;
        try {
            initFTPClient();
            bufferedReader = new BufferedReader(new InputStreamReader(ftpClient.retrieveFileStream(fileAddress)));
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                String lineStr = bufferedReader.readLine();
                if (lineStr == null) {
                    break;
                }
                stringBuffer.append(lineStr);
            }
            ftpClient.logout();
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }
}
