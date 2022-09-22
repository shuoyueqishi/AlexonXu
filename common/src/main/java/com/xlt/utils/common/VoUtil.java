package com.xlt.utils.common;

import com.alibaba.fastjson.JSON;
import com.xlt.constant.CommConstant;
import com.xlt.exception.CommonException;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.DataResponse;
import com.xlt.model.vo.BaseVo;
import com.xlt.model.vo.UserVo;
import com.xlt.service.IUserQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
public class VoUtil {
    public static <T extends BaseVo> void fillUserNames(List<T> voList) {
        Set<Long> notCachedSet = new HashSet<>();
        voList.forEach(vo -> {
            UserVo cachedUserVo = JSON.parseObject(JSON.toJSONString(RedisUtil.get(CommConstant.USER_NAMES_PREFIX + vo.getCreateBy())),UserVo.class);
            if (Objects.isNull(cachedUserVo)) {
                notCachedSet.add(vo.getCreateBy());
            } else {
                vo.setLastUpdateByStr(cachedUserVo.getName());
            }
            cachedUserVo = JSON.parseObject(JSON.toJSONString(RedisUtil.get(CommConstant.USER_NAMES_PREFIX + vo.getLastUpdateBy())),UserVo.class);
            if (Objects.isNull(cachedUserVo)) {
                notCachedSet.add(vo.getLastUpdateBy());
            } else {
                vo.setLastUpdateByStr(cachedUserVo.getName());
            }
        });
        if (CollectionUtils.isEmpty(notCachedSet)) {
            return;
        }
        String appName = AppContextUtil.getApplicationContext().getId();
        if ("user".equals(appName)) {
            IUserQueryService userQueryService = AppContextUtil.getBean(IUserQueryService.class);
            Map<Long, UserVo> userVoMap = userQueryService.fetchUserInfo(notCachedSet);
            if (CollectionUtils.isEmpty(userVoMap)) {
                return;
            }
            handlerUserNameWithMap(voList, userVoMap);
        } else {
            // internal用于内部鉴权
            HttpHeaders headers = new HttpHeaders();
            headers.add("internal", appName);
            HttpEntity<Set<Long>> httpEntity = new HttpEntity<>(notCachedSet, headers);
            RestTemplate restTemplate = AppContextUtil.getBean(RestTemplate.class);
            ResponseEntity<DataResponse> responseEntity = restTemplate.postForEntity("http://user/user/query/cache", httpEntity, DataResponse.class);
            if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                DataResponse<Map<Long, UserVo>> response = (DataResponse<Map<Long, UserVo>>) responseEntity.getBody();
                log.debug("invoke result = {}", response);
                AssertUtil.isNotTrue(response.isSuccess(), "invoke http://user/user/query/cache error:" + response.getMessage());
                handlerUserNameWithMap(voList, response.getData());
            }
        }
    }

    private static <T extends BaseVo> void handlerUserNameWithMap(List<T> voList, Map<Long, UserVo> userVoMap) {
        if (CollectionUtils.isEmpty(userVoMap)) {
            return;
        }
        voList.forEach(vo -> {
            UserVo createBy = userVoMap.get(vo.getCreateBy());
            if (Objects.nonNull(createBy)) {
                vo.setCreateByStr(createBy.getName());
            }
            UserVo lastUpdateBy = userVoMap.get(vo.getLastUpdateBy());
            if (Objects.nonNull(lastUpdateBy)) {
                vo.setLastUpdateByStr(lastUpdateBy.getName());
            }
        });
    }
}
