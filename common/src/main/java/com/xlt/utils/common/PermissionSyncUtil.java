package com.xlt.utils.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xlt.model.vo.PermissionAnnotationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.condition.RequestMethodsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class PermissionSyncUtil {

    @Value("${base.service.path}")
    private String baseServicePath;

    @Autowired
    WebApplicationContext applicationContext;

    public List<PermissionAnnotationVo> getOperationPermissions() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        List<Map<String, String>> list = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            HandlerMethod method = entry.getValue();
            PatternsRequestCondition p = info.getPatternsCondition();
            Map<String, String> map1 = new HashMap<>();
            for (String url : p.getPatterns()) {
                map1.put("url", url);
            }
            map1.put("className", method.getMethod().getDeclaringClass().getName()); // 类名
            map1.put("method", method.getMethod().getName()); // 方法名
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                map1.put("type", requestMethod.toString());
            }

            list.add(map1);
        }

        JSON.parseArray(list);

        return new ArrayList<>();
    }

//    public List<PermissionAnnotationVo> getOperationPermissions() {
//        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
//        provider.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
//        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(baseServicePath);
//        List<PermissionAnnotationVo> permissionVoList = new ArrayList<>();
//        for (BeanDefinition candidateComponent : candidateComponents) {
//            Class<?> cls = null;
//            try {
//                cls = Class.forName(candidateComponent.getBeanClassName());
//            } catch (ClassNotFoundException e) {
//                log.info("class not found, error:",e);
//            }
//            if (cls == null) {
//                continue;
//            }
//            Method[] methods = cls.getMethods();
//            for (Method method : methods) {
//                OperatePermission operationPermission = method.getAnnotation(OperatePermission.class);
//                if (operationPermission==null){
//                    continue;
//                }
//                String resourceName = operationPermission.resourceName();
//                String operateCode = operationPermission.operateCode();
//                PermissionAnnotationVo permissionVo = PermissionAnnotationVo.builder()
//                        .apiOperation(resourceName)
//                        .path(operateCode)
//                        .build();
//                permissionVoList.add(permissionVo);
//            }
//        }
//        return permissionVoList;
//    }

}


