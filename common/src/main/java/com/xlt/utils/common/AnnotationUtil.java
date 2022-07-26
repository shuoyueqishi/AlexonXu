package com.xlt.utils.common;

import com.xlt.annotation.OperatePermission;
import com.xlt.model.vo.PermissionAnnotationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class AnnotationUtil {

    @Value("${base.service.path}")
    private String baseServicePath;

    public List<PermissionAnnotationVo> getOperationPermissions() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(baseServicePath);
        List<PermissionAnnotationVo> permissionVoList = new ArrayList<>();
        for (BeanDefinition candidateComponent : candidateComponents) {
            Class<?> cls = null;
            try {
                cls = Class.forName(candidateComponent.getBeanClassName());
            } catch (ClassNotFoundException e) {
                log.info("class not found, error:",e);
            }
            if (cls == null) {
                continue;
            }
            Method[] methods = cls.getMethods();
            for (Method method : methods) {
                OperatePermission operationPermission = method.getAnnotation(OperatePermission.class);
                if (operationPermission==null){
                    continue;
                }
                String resourceName = operationPermission.resourceName();
                String operateCode = operationPermission.operateCode();
                String operateDesc = operationPermission.operateDesc();
                PermissionAnnotationVo permissionVo = PermissionAnnotationVo.builder()
                        .resourceName(resourceName)
                        .operateCode(operateCode)
                        .operateDesc(operateDesc)
                        .build();
                permissionVoList.add(permissionVo);
            }
        }
        return permissionVoList;
    }
}


