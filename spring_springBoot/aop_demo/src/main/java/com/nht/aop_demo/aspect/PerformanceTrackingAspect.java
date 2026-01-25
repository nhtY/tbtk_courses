package com.nht.aop_demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1) // HandleErrorAspect'teki @Around'dan önce devreye girsin
public class PerformanceTrackingAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Around("com.nht.aop_demo.config.CommonPointCutsConfig.controllerBeans()")
    public Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // before the execution
        long startTimeInMlliSeconds = System.currentTimeMillis();

        // execute
        Object returnValue = proceedingJoinPoint.proceed();
        // after execution
        long endTimeInMilliSeconds = System.currentTimeMillis();

        long executionTime = endTimeInMilliSeconds - startTimeInMlliSeconds;
        logger.info("@Around {} - execution of #{} has taken {} ms. Returns: {}",
                proceedingJoinPoint.getSignature().getDeclaringType(),
                proceedingJoinPoint.getSignature().getName(),
                executionTime,
                returnValue);

        return returnValue;
    }
}
