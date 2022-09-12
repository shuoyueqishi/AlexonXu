package com.xlt.controller;


import com.xlt.auth.OperatePermission;
import com.xlt.constant.OperateConstant;
import com.xlt.logs.OperationLog;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.DataResponse;
import com.xlt.model.response.PagedResponse;
import com.xlt.model.vo.EDocVo;
import com.xlt.model.vo.PageVo;
import com.xlt.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping("/file")
public class EDocController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/batch/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @OperatePermission(resourceName = "EDocController",operateCode = OperateConstant.UPLOAD, operateDesc = "upload file to server")
    @OperationLog(operateModule = "EDocController", operateType = OperateConstant.UPLOAD, operateDesc = "upload file to server")
    DataResponse<List<EDocVo>> batchUploadFile(@RequestParam("files") MultipartFile[] files) {
        List<EDocVo> eDocVoList = fileService.batchSaveFiles(files);
        return new DataResponse<>(eDocVoList);
    }

    @RequestMapping(value = "/single/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @OperatePermission(resourceName = "EDocController",operateCode = OperateConstant.UPLOAD, operateDesc = "upload file to server")
    @OperationLog(operateModule = "EDocController", operateType = OperateConstant.UPLOAD, operateDesc = "upload file to server")
    DataResponse<EDocVo> singleUploadFile(@RequestParam("file") MultipartFile file) {
        EDocVo eDocVo = fileService.saveFile(file);
        return new DataResponse<>(eDocVo);
    }

    @RequestMapping(value = "/download/{docNo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "EDocController",operateCode = OperateConstant.DOWNLOAD, operateDesc = "download file by docNo")
    @OperationLog(operateModule = "EDocController", operateType = OperateConstant.DOWNLOAD, operateDesc = "download file by docNo")
    DataResponse<Boolean> downloadFile(@PathVariable("docNo") String docNo, HttpServletResponse response) {
        Boolean res = fileService.downloadFile(docNo, response);
        return new DataResponse<>(res);
    }

    @RequestMapping(value = "/query/page/list/{pageSize}/{curPage}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "EDocController",operateCode = OperateConstant.READ, operateDesc = "query doc paged list")
    @OperationLog(operateModule = "EDocController", operateType = OperateConstant.READ, operateDesc = "query doc paged list")
    PagedResponse<List<EDocVo>> queryPageList(@QueryParam("")EDocVo eDocVo, @PathVariable("curPage") int curPage, @PathVariable("pageSize")int pageSize) {
        PageVo pageVo = PageVo.builder().currentPage(curPage).pageSize(pageSize).build();
        return fileService.queryPagedList(eDocVo,pageVo);
    }
}
