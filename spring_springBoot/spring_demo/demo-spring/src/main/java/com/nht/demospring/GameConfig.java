package com.nht.demospring;

import com.nht.demospring.game.GameConsole;
import com.nht.demospring.game.GameRunner;
import com.nht.demospring.game.MarioGame;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfig {

    @Bean
    public GameConsole gameConsole() {
        // You can switch between different games by changing the implementation here
        return new MarioGame(); // or new PacmanGame();
    }

    @Bean
    public GameRunner gameRunner(GameConsole gameConsole) {
        return new GameRunner(gameConsole);
    }
}
