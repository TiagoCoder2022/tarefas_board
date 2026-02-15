package com.example.board_tarefas.ui;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MenuRunner implements CommandLineRunner {
    private final MainMenu mainMenu;

    public MenuRunner(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    @Override
    public void run(String... args) {
        mainMenu.execute();
    }
}
