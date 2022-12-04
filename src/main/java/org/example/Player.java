package org.example;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Scanner;

public class Player implements IPlayer {
    private String name;
    private PrintStream out;
    private InputStream in;

    public Player(String name, PrintStream outStream, InputStream inputStream) {
        this.name = name;
        this.in = inputStream;
        this.out = outStream;
    }

    public Point getMove(Board board) {
        out.println(name + " enter your move (*column* *row*) or 'undo' to undo your last move:");
        Scanner sc = new Scanner(in);
        String input = sc.nextLine();

        if (input.equals("undo")) {
            return null;
        }

        var sp = input.split(" ");
        var x = sp[0];
        var y = sp[1];
        return new Point(Integer.parseInt(x), Integer.parseInt(y));
    }
}
