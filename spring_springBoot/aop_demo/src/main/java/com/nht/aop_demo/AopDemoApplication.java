package com.nht.aop_demo;

import com.nht.aop_demo.business.BusinessService1;
import com.nht.aop_demo.controller.ResourceController1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AopDemoApplication implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private final BusinessService1 businessService1;
	private final ResourceController1 resourceController1;

    public AopDemoApplication(BusinessService1 businessService1, ResourceController1 resourceController1) {
        this.businessService1 = businessService1;
        this.resourceController1 = resourceController1;
    }

    public static void main(String[] args) {
		SpringApplication.run(AopDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		logger.info("Value returned is {}", businessService1.calculateMax());

		logger.info("3 to power of 4 is: {}", businessService1.power(3, 4));

		businessService1.findResourceByIdSuccess(1001L);

		var result = resourceController1.getResourceById(1002L);
		var resultFailing = resourceController1.getResourceByIdFail(-125L);

		businessService1.findResourceById(-1L);

	}
}
