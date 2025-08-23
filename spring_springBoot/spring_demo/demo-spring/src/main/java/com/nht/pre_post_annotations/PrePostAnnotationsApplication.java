package com.nht.pre_post_annotations;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Component
class A {
    private Dependency dependency;
    private Dependency2 dependency2;

    @Autowired
    public void setDependency2(Dependency2 dependency2) {
        System.out.println("A: Dependency2 injected.");
        this.dependency2 = dependency2;
    }


    public A(Dependency dependency) {
        System.out.println("A: Constructor called.");

        this.dependency = dependency;

        System.out.println("A: Dependency injected.");
    }

    @PostConstruct
    public void init() {
        System.out.println("A: Init method called. After constructor and dependency injection.");
        dependency.doSomething();
        dependency2.doSomethingElse();
    }

    @PreDestroy
    public void destroy() {
        System.out.println("A: Destroy method called. Releasing resources...");
    }
}

@Component
class Dependency {
    public void doSomething() {
        System.out.println("Dependency: Doing something.");
    }
}

@Component
class Dependency2 {
    public void doSomethingElse() {
        System.out.println("Dependency2: Doing something else.");
    }
}

@Configuration
@ComponentScan
public class PrePostAnnotationsApplication {

    public static void main(String[] args) {
        try (var context = new org.springframework.context.annotation.AnnotationConfigApplicationContext(PrePostAnnotationsApplication.class)) {
            System.out.println("Context initialized successfully.");
        }
    }
}
