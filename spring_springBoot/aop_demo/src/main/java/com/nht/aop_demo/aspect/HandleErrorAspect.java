package com.nht.aop_demo.aspect;

import com.nht.aop_demo.dto.ResponseData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2) // PerformanceTrackingAspect'teki @Around'dan sonra devreye girsin.
public class HandleErrorAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Around("com.nht.aop_demo.config.CommonPointCutsConfig.hasHandleErrorAnnotation()")
    public Object handleError(ProceedingJoinPoint proceedingJoinPoint) {
        Object returnValue;
        try {
            returnValue = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            logger.error("Handling Error: {}", e.getMessage());
            return new ResponseData(500, "error", null);
        }
        return returnValue;
    }
}
