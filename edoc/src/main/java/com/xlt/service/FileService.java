package com.xlt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlt.constant.CommConstant;
import com.xlt.exception.CommonException;
import com.xlt.mapper.IEDocMapper;
import com.xlt.model.po.EDocPo;
import com.xlt.model.request.EDocReq;
import com.xlt.model.response.PagedResponse;
import com.xlt.model.vo.EDocVo;
import com.xlt.model.vo.PageVo;
import com.xlt.utils.common.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Objects;

@Service
@Slf4j
public class FileService {

    @Value("${file.gateway.port}")
    private String gatewayPort;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${file.gateway.context-path}")
    private String gatewayContextPath;

    @Value("${file.dir}")
    private String fileDir;

    @Autowired
    private IEDocMapper eDocMapper;

    @Transactional
    public List<EDocVo> batchSaveFiles(MultipartFile[] files) {
        log.info("file size={}", files.length);
        List<EDocVo> eDocVoList = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            EDocVo eDocVo = saveFile(file);
            eDocVoList.add(eDocVo);
        }
        return eDocVoList;
    }

    @Transactional
    public EDocVo saveFile(MultipartFile multipartFile) {
        String oriFilename = multipartFile.getOriginalFilename();
        AssertUtil.isNull(oriFilename, "originalFilename is null");
        int splitIdx = oriFilename.lastIndexOf(".");
        String docType = oriFilename.substring(splitIdx + 1);
        String docNo = SeqNoGenUtil.getSeqNoWithTime("doc", 6);
        String filename = oriFilename.substring(0, splitIdx) + "-" + docNo + "." + docType;
        String fileUrl = getFileUrl(docNo);
        EDocPo eDocPo = EDocPo.builder()
                .docName(filename)
                .docSize(multipartFile.getSize())
                .docType(docType)
                .docNo(docNo)
                .downloadUrl(fileUrl)
                .deleted(0)
                .build();
        PoUtil.buildCreateUserInfo(eDocPo);
        eDocMapper.insert(eDocPo);
        File file = new File(fileDir + filename);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            log.error("save file error:", e);
            throw new CommonException("save file:" + oriFilename + " error:" + e.getMessage());
        }
        return ObjectUtil.convertObjs(eDocPo, EDocVo.class);
    }

    public boolean deleteFile(String docNo) {
        log.info("delete file docNo={}", docNo);
        boolean res = false;
        QueryWrapper<EDocPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doc_no", docNo);
        queryWrapper.eq("deleted", CommConstant.NOT_DELETED);
        EDocPo eDocPo = eDocMapper.selectOne(queryWrapper);
        AssertUtil.isNull(eDocPo, "file not exist/deleted");
        eDocPo.setDeleted(CommConstant.DELETED);
        eDocMapper.updateById(eDocPo);
        File file2Del = new File(fileDir+eDocPo.getDocName());
        return file2Del.delete();
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

    private String getFileUrl(String docNo) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            String fileUrl = "http://" + address.getHostAddress() + ":" + gatewayPort + gatewayContextPath + contextPath + "/file/download/" + docNo;
            log.info("fileUrl:{}", fileUrl);
            return fileUrl;
        } catch (UnknownHostException e) {
            log.error("get host error:", e);
            throw new CommonException("get host error:" + e.getMessage());
        }
    }

    public Boolean downloadFile(String docNo, HttpServletResponse response) {
        log.info("download file, docNo={}", docNo);
        AssertUtil.isStringEmpty(docNo, "docNo can't be empty");
        QueryWrapper<EDocPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doc_no", docNo);
        EDocPo eDocPo = eDocMapper.selectOne(queryWrapper);
        AssertUtil.isNull(eDocPo, "docNo[" + docNo + "] not exist in system");
        File file = new File(fileDir + eDocPo.getDocName());
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(eDocPo.getDocName(), "UTF-8"));
                ServletOutputStream outputStream = response.getOutputStream();
                FileCopyUtils.copy(fileInputStream, outputStream);
                return true;
            } catch (IOException e) {
                log.error("download file error:", e);
                throw new CommonException("download file error:" + e.getMessage());
            }
        } else {
            throw new CommonException("file exist");
        }
    }

    public PagedResponse<List<EDocVo>> queryPagedList(EDocReq eDocReq, PageVo pageVo) {
        log.info("queryPagedList params:{}", eDocReq);
        QueryWrapper<EDocPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(eDocReq.getDocNo()), "edoc_no", eDocReq.getDocNo());
        queryWrapper.like(StringUtils.isNotEmpty(eDocReq.getDocName()), "doc_name", eDocReq.getDocName());
        queryWrapper.eq(StringUtils.isNotEmpty(eDocReq.getDocType()), "doc_type", eDocReq.getDocType());
        queryWrapper.eq(Objects.nonNull(eDocReq.getDeleted()),"deleted", eDocReq.getDeleted());
        queryWrapper.eq(Objects.nonNull(eDocReq.getCreateBy()), "create_by", eDocReq.getCreateBy());
        queryWrapper.ge(Objects.nonNull(eDocReq.getCreationDateStart()), "creation_date", eDocReq.getCreationDateStart());
        queryWrapper.le(Objects.nonNull(eDocReq.getCreationDateEnd()), "creation_date", eDocReq.getCreationDateEnd());
        queryWrapper.orderByDesc("creation_date");
        Page<EDocPo> page = new Page<>(pageVo.getCurrentPage(), pageVo.getPageSize());
        Page<EDocPo> docPoPage = eDocMapper.selectPage(page, queryWrapper);
        List<EDocVo> eDocVoList = ObjectUtil.convertObjsList(docPoPage.getRecords(), EDocVo.class);
        VoUtil.fillUserNames(eDocVoList);
        pageVo.setTotal(docPoPage.getTotal());
        pageVo.setTotalPages(docPoPage.getPages());
        return new PagedResponse<>(eDocVoList, pageVo);
    }

}
