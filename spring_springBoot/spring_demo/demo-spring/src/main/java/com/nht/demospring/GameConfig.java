package com.nht.demospring;

import com.nht.demospring.game.GameConsole;
import com.nht.demospring.game.GameRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class GameConfig {

    @Bean
    public GameRunner gameRunner(GameConsole gameConsole) {
        return new GameRunner(gameConsole);
    }
}
