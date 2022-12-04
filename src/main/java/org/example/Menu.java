package org.example;

import java.util.Scanner;

public class Menu {
    Statistics stats;

    public Menu(Statistics stats) {
        this.stats = stats;
    }
    void printMenu() {
        System.out.println("\tREVERSI");
        System.out.println("1. Init game with dumb bot");
        System.out.println("2. Init game with smart bot");
        System.out.println("3. Init game with second player");
        System.out.println("4. Show best score");
        System.out.println("5. Exit");
    }

    UserRequest getAndHandleUserCommand() {
        UserRequest out = new UserRequest();
        Scanner sc = new Scanner(System.in);
        String command = sc.next();
        int comNumber = -1;
        do {
            try {
                comNumber = Integer.parseInt(command);
            } catch (Exception e) {
                comNumber = -1;
            }
            switch (comNumber) {
                case 1 -> {
                    System.out.println("Enter your name");
                    String name = sc.next();
                    var p1 = new Player(name, System.out, System.in);
                    var p2 = new ComputerPlayer(false, true);

                    out.game = new Game(p1, p2);
                    out.playAgain = true;
                    out.exit = false;

                    return out;
                }
                case 2 -> {
                    System.out.println("Enter your name");
                    String name = sc.next();
                    var p1 = new Player(name, System.out, System.in);
                    var p2 = new ComputerPlayer(false, false);

                    out.game = new Game(p1, p2);
                    out.playAgain = true;
                    out.exit = false;

                    return out;
                }
                case 3 -> {
                    System.out.println("Enter first player name");
                    String name1 = sc.next();
                    var p1 = new Player(name1, System.out, System.in);
                    System.out.println("Enter second player name");
                    String name2 = sc.next();
                    var p2 = new Player(name2, System.out, System.in);

                    out.game = new Game(p1, p2);
                    out.playAgain = true;
                    out.exit = false;

                    return out;
                }
                case 4 -> {
                    System.out.println("Best session score: " + stats.getBestScore());

                    out.game = null;
                    out.playAgain = false;
                    out.exit = false;

                    return out;
                }
                case 5 -> {
                    out.game = null;
                    out.playAgain = false;
                    out.exit = true;

                    return out;
                }
                case 6 -> {
                    var p1 = new ComputerPlayer(true, false);
                    var p2 = new ComputerPlayer(false, false);
                    out.game = new Game(p1, p2);
                    out.playAgain = true;
                    out.exit = false;

                    return out;
                }
                default -> {
                    System.out.println("Command is not valid. Try again:");
                    command = sc.next();
                }
            }
        } while (true);
        //return null;
    }

    boolean askPlayAgain() {
        System.out.println("Want to play again? (Yes/No)");

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        switch (input) {
            case "Yes":
                return true;
            default:
                return false;
        }
    }
}
