package com.nht.initialization;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

// All beans are initialized at the start of the application by default, which is EAGER.

// But, the following example demonstrates how to use the @Lazy annotation to delay the initialization of a bean
// until it is actually needed.

@Component
class SomeDependency {
    // Simulating a dependency
}

@Component
@Lazy
class TakesTooLongToInitialize {
    SomeDependency someDependency;

    public TakesTooLongToInitialize(SomeDependency someDependency) {
        System.out.println("TakesTooLongToInitialize: Initializing...");
        this.someDependency = someDependency;
    }

    public void doSomething() {
        System.out.println("Doing something in TakesTooLongToInitialize.");
    }
}

@Configuration
@ComponentScan
public class InitializationExample {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(InitializationExample.class)) {
            System.out.println("Context initialized successfully.");

            context.getBean(TakesTooLongToInitialize.class).doSomething();

            System.out.println("Hello There!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Application finished.");
    }
}
