package com.nht.dependencyinjection.example.e4;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Configuration
@ComponentScan
public class ParameterBeanExample {

    @Bean
    public String name() {
        return "name";
    }

    @Bean
    public String firstName() {
        return "FNAme";
    }

    @Bean
    public String fullName(String firstName) {
        return firstName + " surname";
    }

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(ParameterBeanExample.class)) {
            Arrays.stream(context.getBeanDefinitionNames())
                    .forEach(System.out::println);

            String fullname = context.getBean("fullName", String.class);
            System.out.println(fullname);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
