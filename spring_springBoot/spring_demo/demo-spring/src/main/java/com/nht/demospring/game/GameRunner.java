package com.nht.demospring.game;

public class GameRunner {
    private GameConsole gameConsole;

    public GameRunner(GameConsole gameConsole) {
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
