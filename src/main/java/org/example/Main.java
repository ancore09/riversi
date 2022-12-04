package org.example;

public class Main {
    public static void main(String[] args) {
        Statistics stats = new Statistics();
        Menu menu = new Menu(stats);
        boolean playAgain = true;
        boolean exit = false;
        do {
            menu.printMenu();
            UserRequest req = menu.getAndHandleUserCommand();
            if (req.exit) {
                exit = true;
                playAgain = false;
            } else if (req.playAgain) {
                int score = req.game.start();
                stats.setBestScore(score);
                playAgain = menu.askPlayAgain();
            }
        } while (playAgain || !exit);
    }
}