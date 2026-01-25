package com.nht.aop_demo.controller;

import com.nht.aop_demo.annotation.HandleError;
import com.nht.aop_demo.business.BusinessService1;
import com.nht.aop_demo.dto.ResponseData;
import org.springframework.stereotype.Component;

@Component // REST api yazmıyoruz bu sebeple component olsun.
public class ResourceController1 {

    private final BusinessService1 businessService1;

    public ResourceController1(BusinessService1 businessService1) {
        this.businessService1 = businessService1;
    }

    public ResponseData getResourceById(long id) {
        return new ResponseData(
                200,
                "success",
                businessService1.findResourceByIdSuccess(id)
        );
    }

    @HandleError
    public ResponseData getResourceByIdFail(long id) {
        return new ResponseData(
                200,
                "success",
                businessService1.findResourceById(id)
        );
        // HandleErrorAspect --> Handling Error: Could not find the resource with id: -125
        // PerformanceTrackingAspect --> @Around class com.nht.aop_demo.controller.ResourceController1 - execution of #getResourceByIdFail has taken 1 ms. Returns: ResponseData[status=500, message=error, data=null]
    }

}
