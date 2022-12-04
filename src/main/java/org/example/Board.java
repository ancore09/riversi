package org.example;

import java.util.*;

public class Board {
    private char[][] board;
    private boolean firstPlayer = true;
    private HashSet<Point> possibleMoves;
    public Board() {}

    public HashSet<Point> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMoves(HashSet<Point> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public void initBoard() {
        if (board == null) {
            board = new char[8][8];
        }
        for (char[] chars : board) {
            Arrays.fill(chars, ' ');
        }

        board[3][3] = 'w';
        board[3][4] = 'b';
        board[4][3] = 'b';
        board[4][4] = 'w';
        possibleMoves = getPossibleMoves(board, firstPlayer);
    }

    void makeMove(int x, int y) {
        var point = new Point(x, y);
        if (possibleMoves.contains(point)) {
            board[y][x] = firstPlayer ? 'b' : 'w';
            changeColor(x, y);
            firstPlayer = !firstPlayer;
        }
    }

    void changeColor(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                var points = getPointToRecolor(x, y, i, j);
                if (points == null) {
                    continue;
                }
                for (Point point : points) {
                    board[point.y][point.x] = firstPlayer ? 'b' : 'w';
                }
            }
        }
    }

    HashSet<Point> getPointToRecolor(int x, int y, int dirX, int dirY) {
        HashSet<Point> set = new HashSet<>();
        boolean foundAnotherColor = false;
        int i = y+dirY;
        int j = x+dirX;

        if (i < board.length && i >= 0 && j < board.length && j >= 0 && (firstPlayer ? (board[i][j] == 'b') : (board[i][j] == 'w'))) {
            return null;
        }

        while (i < board.length && i >= 0 && j < board.length && j >= 0) {
            if (board[i][j] == ' ') {
                return null;
            }

            if (firstPlayer ? (board[i][j] == 'b') : (board[i][j] == 'w')) {
                if (foundAnotherColor) {
                    return set;
                }
            }

            if (!firstPlayer ? (board[i][j] == 'b') : (board[i][j] == 'w')) {
                foundAnotherColor = true;
                set.add(new Point(j, i));
            }

            i+=dirY;
            j+=dirX;
        }
        return null;
    }

    HashSet<Point> getPossibleMoves(char[][] board, boolean firstPlayer) {
        HashSet<Point> result = new HashSet<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (firstPlayer ? (board[i][j] == 'b') : (board[i][j] == 'w')) {
                    var moves = getMovesForPoint(board, j, i, firstPlayer);
                    result.addAll(moves);
                }
            }
        }
        //possibleMoves = result;
        return result;
    }

    HashSet<Point> getMovesForPoint(char[][] board, int x, int y, boolean firstPlayer) {
        HashSet<Point> set = new HashSet<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                var move = getMoveInDirection(board, i, j, x, y, firstPlayer);
                if (move == null) {
                    continue;
                }
                set.add(new Point(move[0], move[1]));
            }
        }
        return set;
    }

    int[] getMoveInDirection(char[][] board, int dirX, int dirY, int x, int y, boolean firstPlayer) {
        boolean foundAnotherColor = false;
        int[] result = new int[2];

        int i = y+dirY;
        int j = x+dirX;

        if (i < board.length && i >= 0 && j < board.length && j >= 0 && (firstPlayer ? (board[i][j] == 'b') : (board[i][j] == 'w'))) {
            return null;
        }

        while (i < board.length && i >= 0 && j < board.length && j >= 0) {
            if (board[i][j] == ' ') {
                if (foundAnotherColor) {
                    result[0] = j;
                    result[1] = i;
                    return result;
                } else {
                    return null;
                }
            } else if (firstPlayer ? (board[i][j] == 'b') : (board[i][j] == 'w')) {
                if (foundAnotherColor) {
                    return null;
                }
            }

            if (!firstPlayer ? (board[i][j] == 'b') : (board[i][j] == 'w')) {
                foundAnotherColor = true;
            }

            i+=dirY;
            j+=dirX;
        }

        return null;
    }

    public void showPossibleMoves() {
        var moves = getPossibleMoves(board, firstPlayer);
        possibleMoves = moves;

        System.out.print(" ");
        for (int i = 0; i < board.length; i++) {
            System.out.print(" " + i);
        }
        System.out.println();

        for (int i = 0; i < board.length; i++) {
            System.out.print(i + "|");
            for (int j = 0; j < board[i].length; j++) {
                if (moves.contains(new Point(j, i))) {
                    System.out.print(ConsoleColors.YELLOW_BACKGROUND_BRIGHT+"."+ConsoleColors.RESET+"|");
                    continue;
                }

                if (board[i][j] == 'b') {
                    System.out.print(ConsoleColors.BLACK_BACKGROUND+board[i][j]+ConsoleColors.RESET+"|");
                } else if (board[i][j] == 'w') {
                    System.out.print(ConsoleColors.WHITE_BACKGROUND_BRIGHT+Constants.ANSI_BLACK+board[i][j]+ConsoleColors.RESET+"|");
                } else {
                    System.out.print(board[i][j]+"|");

                }
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("Possible moves:");
        for (Point move : moves) {
            System.out.println(move.x + " " + move.y);
        }
    }

    public Board clone() {
        Board clone = new Board();
        clone.board = this.board.clone();
        for (int i = 0; i < clone.board.length; i++) {
            clone.board[i] = board[i].clone();
        }
        clone.firstPlayer = this.firstPlayer;
        clone.possibleMoves = (HashSet<Point>) this.possibleMoves.clone();
        return clone;
    }

    public void printBoard(char[][] board) {
        System.out.print(" ");
        for (int i = 0; i < board.length; i++) {
            System.out.print(" " + i);
        }
        System.out.println();

        for (int i = 0; i < board.length; i++) {
            System.out.print(i + "|");
            for (int j = 0; j < board[i].length; j++) {
                System.out.print("\u001B[31m"+board[i][j]+"\u001B[0m"+"|");
            }
            System.out.println();
        }
    }
}
