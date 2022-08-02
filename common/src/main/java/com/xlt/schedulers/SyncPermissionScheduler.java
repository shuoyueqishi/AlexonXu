package com.xlt.schedulers;

import com.xlt.ISyncPermissionService;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.vo.PermissionVo;
import com.xlt.utils.common.AppContextUtil;
import com.xlt.utils.common.PermissionSyncUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class SyncPermissionScheduler {

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(cron = "*/30 * * * * ?")
    public void syncPermissions(){
        List<PermissionVo> permissionVoList = PermissionSyncUtil.getOperationPermissions();
        log.info("appName={},permissionVoList.size={}",appName,permissionVoList.size());
        if("user".equals(appName)) {
            ISyncPermissionService syncPermissionService = AppContextUtil.getBean(ISyncPermissionService.class);
            syncPermissionService.syncPermissionList(permissionVoList);
        } else {
            HttpEntity<List<PermissionVo>> httpEntity = new HttpEntity<>(permissionVoList);
            ResponseEntity<BasicResponse> responseEntity = restTemplate.postForEntity("http://user/user/permission/synchronize/list", httpEntity, BasicResponse.class);
            if(HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                BasicResponse basicRes = responseEntity.getBody();
                log.info("invoke result = {}",basicRes);
            } else {
                log.error("invoke error: http://user/user/permission/synchronize/list");
            }
            log.info("success to synchronize permission");
        }
    }


}
