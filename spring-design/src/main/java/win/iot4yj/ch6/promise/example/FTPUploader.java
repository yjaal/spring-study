package win.iot4yj.ch6.promise.example;

import java.io.File;

public interface FTPUploader {

    void init(String ftpServer, String ftpUserName, String password, String serverDir) throws Exception;

    void upload(File file) throws Exception;

    void disconnect();
}