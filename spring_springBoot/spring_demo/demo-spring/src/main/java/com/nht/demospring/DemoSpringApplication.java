package com.nht.demospring;

import com.nht.demospring.game.GameConsole;
import com.nht.demospring.game.GameRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class DemoSpringApplication {

	public static void main(String[] args) {
		var context = new AnnotationConfigApplicationContext(DemoSpringApplication.class);

		var gameRunner = context.getBean(GameRunner.class);
		gameRunner.run();
	}

}
