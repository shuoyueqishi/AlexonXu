package com.xlt.service;

import com.xlt.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FileService {

    @Value("${server.port}")
    private String port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${file.dir}")
    private String fileDir;

    public List<String> batchSaveFiles(MultipartFile[] files) {
        log.info("file size={}", files.length);
        List<String> fileDownloadUrls = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String downloadUrl = saveFile(file);
            fileDownloadUrls.add(downloadUrl);
        }
        return fileDownloadUrls;
    }


    public String saveFile(MultipartFile multipartFile) {

        String filename = multipartFile.getOriginalFilename();

        File file = new File(fileDir + filename);

        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            log.error("save file error:", e);
            throw new CommonException("save file:" + filename + " error:" + e.getMessage());
        }
        return getFileUrl(filename);
    }

    public List<String> getFiles() {
        List<String> fileUrls = new ArrayList<>();

        File file = new File(fileDir);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    fileUrls.add(getFileUrl(file1.getName()));
                }
            }
        }
        return fileUrls;
    }

    private String getFileUrl(String fileName) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            String fileUrl = "http://" + address.getHostAddress() + ":" + port + contextPath + "/file/" + fileName;
            log.info("fileUrl:{}", fileUrl);
            return fileUrl;
        } catch (UnknownHostException e) {
            log.error("get host error:", e);
            throw new CommonException("get host error:"+e.getMessage());
        }
    }

    public Boolean downloadFile(HttpServletResponse response, String fileName) {
        File file = new File(fileDir + fileName);
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);

                response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
                ServletOutputStream outputStream = response.getOutputStream();

                FileCopyUtils.copy(fileInputStream, outputStream);
                return true;
            } catch (IOException e) {
                log.error("download file error: {}", e.getMessage());
                return false;
            }
        }
        return false;
    }

}
