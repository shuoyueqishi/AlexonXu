package com.xlt.schedulers;

import com.alexon.authorization.model.vo.PermissionVo;
import com.alexon.authorization.service.ISyncPermissionService;
import com.alexon.exception.utils.AssertUtil;
import com.alexon.model.response.DataResponse;
import com.alexon.authorization.utils.PermissionSyncUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class SyncPermissionScheduler {


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ISyncPermissionService syncPermissionService;


    @Scheduled(cron = "0 0/1 * * * ?")
    public void syncPermissions() {

        List<String> serviceIds = discoveryClient.getServices();
        for (String serviceId : serviceIds) {
            log.info("synchronize permission for:{}", serviceId);
            if ("gateway".equals(serviceId)) {
                continue;
            }
            if ("user".equals(serviceId)) {
                List<PermissionVo> permissionVoList = PermissionSyncUtil.getOperationPermissions();
                syncPermissionService.syncPermissionList(permissionVoList);
            } else {
                String url = MessageFormat.format("http://{0}/{1}/auth/permission/sync/fetch", serviceId, serviceId);
                log.info("url={}", url);
                String resStr = restTemplate.getForObject(url, String.class);
                DataResponse dataRes = JSON.parseObject(resStr, DataResponse.class);
                AssertUtil.isNotTrue(dataRes.isSuccess(), "invoke error: " + url);
                List<PermissionVo> permissionVoList = JSON.parseArray(dataRes.getData().toString(), PermissionVo.class);
                syncPermissionService.syncPermissionList(permissionVoList);
            }
            log.debug("success to synchronize permission for:{}", serviceId);
        }

    }


}
