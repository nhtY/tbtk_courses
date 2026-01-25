package com.nht.aop_demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(* com.nht.aop_demo.business.*.*(..))")
    public void businessServiceMethods() {}

    @Before("businessServiceMethods()")
    public void logBeforeMethodCall(JoinPoint joinPoint) {
        logger.info("-".repeat(40));
        logger.info("+++BEFORE running: {}", joinPoint);
        logger.info("The args to the method {} is {}", joinPoint.getSignature().getName(), joinPoint.getArgs());

        // Before running: execution(int com.nht.aop_demo.business.BusinessService1.power(int,int))
        // The args to the method power is [3, 4]
    }

    // burada pointcut'ı after içinde inline geçtik: execution(* com.nht.aop_demo.business.*.*(..))
    @After("execution(* com.nht.aop_demo.business.*.*(..))")
    public void logAfterServiceMethods(JoinPoint joinPoint) {
        logger.info("+++AFTER running {}", joinPoint);
        logger.info("-".repeat(40));
    }

    @AfterThrowing(
            pointcut = "com.nht.aop_demo.config.CommonPointCutsConfig.dataPackagePointCut()",
            throwing = "exception"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        logger.error("xxx ERROR while running {}, error: {}\nStackTrace: {}", joinPoint, exception.getMessage(), exception.getStackTrace());
        // xxx ERROR while running execution(long com.nht.aop_demo.data.DataService1.findById(long)), error: Could not find the resource with id: -1
        //StackTrace: [com.nht.aop_demo.data.DataService1.findById(DataService1.java:13), ............
    }

    @AfterReturning(
            pointcut = "com.nht.aop_demo.config.CommonPointCutsConfig.beansContainingServiceInTheirNames()",
            returning = "result"
    )
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.error("ServiceBean: {}, Method: {}, Result: {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), result);
        // ServiceBean: com.nht.aop_demo.business.BusinessService1, Method: power,  Result: 81
    }

}
