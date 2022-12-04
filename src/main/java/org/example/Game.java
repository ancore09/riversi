package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Game {
    Board board;
    List<Board> history;
    boolean firstPlayer = true;
    int movesCount = 0;
    IPlayer p1;
    IPlayer p2;

    public Game(IPlayer p1, IPlayer p2) {
        this.p1 = p1;
        this.p2 = p2;
        board = new Board();
        history = new ArrayList<>();
        history.add(board);
    }

    public int start() {
        board.initBoard();
        while (board.possibleMoves.size() != 0) {
            board.showPossibleMoves();
            if (board.possibleMoves.size() == 0) {
                break;
            }
            history.add(board.clone());
            var move = requestMove();
            if (move == null) {
                board = history.get(history.size()-3);
                history.remove(history.size()-1);
                history.remove(history.size()-2);
                //firstPlayer = !firstPlayer;
                continue;
            }
            board.makeMove(move.x, move.y);
            System.out.println((firstPlayer ? "Player 1" : "Player 2") + " makes move: " + move.x + " " + move.y);
            firstPlayer = !firstPlayer;
            movesCount++;
        }
        return calculateWinner();
    }

    public Point requestMove() {
        var validMoves = board.possibleMoves;
        Point point;
        do {
            point = firstPlayer ? p1.getMove(board) : p2.getMove(board);
            if (point == null) {
                return null;
            }
        } while (!validMoves.contains(point));

        return point;
    }

    int calculateWinner() {
        int w = 0;
        int b = 0;

        for (char[] chars : board.board) {
            for (char c : chars) {
                if (c == 'b') {
                    b++;
                } else if (c == 'w') {
                    w++;
                }
            }
        }

        System.out.println("Total moves: " + movesCount);


        if (w > b) {
            System.out.println("Player 2 won with " + w + " points");
            return -1;
        } else if (w < b) {
            System.out.println("Player 1 won with " + b + " points");
            return b;
        } else {
            System.out.println("Draw with " + w + " points");
            return -1;
        }
    }
}
