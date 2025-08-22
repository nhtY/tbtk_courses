package com.nht.demospring.game;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class GameRunner {
    private GameConsole gameConsole;

    public GameRunner(@Qualifier("PacmanGame") GameConsole gameConsole) {
        this.gameConsole = gameConsole;
    }

    public void run() {
        System.out.println("Game is starting...");
        gameConsole.up();
        gameConsole.down();
        gameConsole.left();
        gameConsole.right();
        System.out.println("Game has ended.");
    }
}
