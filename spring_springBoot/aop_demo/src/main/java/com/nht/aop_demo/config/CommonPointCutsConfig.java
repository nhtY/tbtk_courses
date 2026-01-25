package com.nht.aop_demo.config;

import org.aspectj.lang.annotation.Pointcut;

public class CommonPointCutsConfig {

    @Pointcut("execution(* com.nht.aop_demo.data.*.*(..))")
    public void dataPackagePointCut() {}

    @Pointcut("bean(*Service*)")
    public void beansContainingServiceInTheirNames() {}

    @Pointcut("bean(*Controller*)")
    public void controllerBeans() {}

    @Pointcut("@annotation(com.nht.aop_demo.annotation.HandleError)")
    public void hasHandleErrorAnnotation() {}
}
