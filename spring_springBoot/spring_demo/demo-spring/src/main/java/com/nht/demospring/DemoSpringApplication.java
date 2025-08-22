package com.nht.demospring;

import com.nht.demospring.game.GameRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class DemoSpringApplication {

	public static void main(String[] args) {
		var context = new AnnotationConfigApplicationContext(GameConfig.class);

		var gameRunner = context.getBean(GameRunner.class);
		gameRunner.run();
	}

}
