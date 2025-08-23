package com.nht.dependencyinjection.example.e2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
class BusinessService {
    Dependency1 dependency1;
    Dependency2 dependency2;

    @Autowired
    public void setDependency1(Dependency1 dependency1) {
        this.dependency1 = dependency1;
    }

    @Autowired
    public void setDependency2(Dependency2 dependency2) {
        this.dependency2 = dependency2;
    }

    public String toString() {
        return "BusinessService{" +
                "dependency1=" + dependency1 +
                ", dependency2=" + dependency2 +
                '}';
    }
}

@Component
class Dependency1 {
    // Simulating a dependency
}

@Component
class Dependency2 {
    // Simulating another dependency
}


@Configuration
@ComponentScan
public class SetterInjectionExample {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(SetterInjectionExample.class)) {
            Arrays.stream(context.getBeanDefinitionNames())
                    .forEach(System.out::println);

            BusinessService businessService = context.getBean(BusinessService.class);
            System.out.println(businessService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
