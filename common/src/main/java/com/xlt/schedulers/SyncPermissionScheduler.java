package com.xlt.schedulers;

import com.alexon.authorization.model.vo.PermissionVo;
import com.alexon.authorization.service.ISyncPermissionService;
import com.alexon.model.response.BasicResponse;
import com.alexon.model.utils.AppContextUtil;
import com.alexon.authorization.utils.PermissionSyncUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
@Slf4j
public class SyncPermissionScheduler {

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RestTemplate restTemplate;

    private static final String PERMISSION_SYNC_LOCK = "permission_sync_lock";

    @Scheduled(cron = "0 0/10 * * * ?")
    public void syncPermissions() {
        RLock fairLock = redissonClient.getFairLock(PERMISSION_SYNC_LOCK);
        try {
            if (!fairLock.tryLock(30, 20, TimeUnit.SECONDS)) {
                log.error("get redisson lock failed");
                return;
            }
            List<PermissionVo> permissionVoList = PermissionSyncUtil.getOperationPermissions();
            log.debug("appName={},permissionVoList.size={}", appName, permissionVoList.size());
            if ("user".equals(appName)) {
                ISyncPermissionService syncPermissionService = AppContextUtil.getBean(ISyncPermissionService.class);
                syncPermissionService.syncPermissionList(permissionVoList);
            } else {
                // internal用于内部鉴权
                HttpHeaders headers = new HttpHeaders();
                headers.add("internal",appName);
                HttpEntity<List<PermissionVo>> httpEntity = new HttpEntity<>(permissionVoList,headers);
                ResponseEntity<BasicResponse> responseEntity = restTemplate.postForEntity("http://user/user/permission/synchronize/list", httpEntity, BasicResponse.class);
                if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                    BasicResponse basicRes = responseEntity.getBody();
                    log.debug("invoke result = {}", basicRes);
                } else {
                    log.error("invoke error: http://user/user/permission/synchronize/list");
                }
                log.debug("success to synchronize permission");
            }
        } catch (InterruptedException e) {
            log.error("get redisson InterruptedException:", e);
        } finally {
            if (fairLock.isHeldByCurrentThread()) {
                fairLock.unlock();
                log.debug("unlock redisson lock success for:" + PERMISSION_SYNC_LOCK);
            }
        }

    }


}
