package com.nht.scope;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
class NormalClass {

}


@Scope(value= ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
class PrototypeClass {

}

@Configuration
@ComponentScan
public class BeanScopeExampleApllication {

    public static void main(String[] args) {
        try (var context = new org.springframework.context.annotation.AnnotationConfigApplicationContext(BeanScopeExampleApllication.class)) {
            System.out.println("Context initialized successfully.");

            NormalClass normalBean1 = context.getBean(NormalClass.class);
            NormalClass normalBean2 = context.getBean(NormalClass.class);

            System.out.println("NormalBean1 and NormalBean2 addresses: " + normalBean1 + " = " + normalBean2);
            System.out.println("Normal beans are the same instance: " + (normalBean1 == normalBean2));

            PrototypeClass prototypeBean1 = context.getBean(PrototypeClass.class);
            PrototypeClass prototypeBean2 = context.getBean(PrototypeClass.class);

            System.out.println("PrototypeBean1 and PrototypeBean2 addresses: " + prototypeBean1 + " != " + prototypeBean2);
            System.out.println("Prototype beans are different instances: " + (prototypeBean1 != prototypeBean2));

        }
    }
}
