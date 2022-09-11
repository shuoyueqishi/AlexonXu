package com.xlt.controller;


import com.xlt.auth.OperatePermission;
import com.xlt.constant.OperateConstant;
import com.xlt.logs.OperationLog;
import com.xlt.model.response.DataResponse;
import com.xlt.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
public class EDocController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/batch/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @OperatePermission(resourceName = "EDocController",operateCode = OperateConstant.UPLOAD, operateDesc = "upload file to server")
    @OperationLog(operateModule = "EDocController", operateType = OperateConstant.UPLOAD, operateDesc = "upload file to server")
    DataResponse<List<String>> batchUploadFile(@RequestParam("files") MultipartFile[] files) {
        List<String> downloadUrls = fileService.batchSaveFiles(files);
        return new DataResponse<>(downloadUrls);
    }

    @RequestMapping(value = "/single/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @OperatePermission(resourceName = "EDocController",operateCode = OperateConstant.UPLOAD, operateDesc = "upload file to server")
    @OperationLog(operateModule = "EDocController", operateType = OperateConstant.UPLOAD, operateDesc = "upload file to server")
    DataResponse<String> singleUploadFile(@RequestParam("file") MultipartFile file) {
        String downloadUrl = fileService.saveFile(file);
        return new DataResponse<>(downloadUrl);
    }
}
