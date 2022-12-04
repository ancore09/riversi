package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class ComputerPlayer implements IPlayer {
    private String name = "bot";
    private boolean firstPlayer;
    private boolean isStupid;

    public ComputerPlayer(boolean firstPlayer, boolean isStupid) {
        this.firstPlayer = firstPlayer;
        this.isStupid = isStupid;
    }

    public Point getMove(Board board) {
        if (isStupid) {
            return getStupidMove(board);
        } else {
            return getSmartMove(board);
        }
    }

    Point getStupidMove(Board board) {
        var boardClone = new char[8][8];
        for (int i = 0; i < board.getBoard().length; i++) {
            boardClone[i] = board.getBoard()[i].clone();
        }

        int maxEval = -100;
        List<Point> bestMoves = new ArrayList<>();
        for (Point possibleMove : board.getPossibleMoves()) {
            var eval = evaluatePosition(boardClone, possibleMove);
            if (eval > maxEval) {
                maxEval = eval;
                bestMoves.clear();
                bestMoves.add(possibleMove);
            } else if (eval == maxEval) {
                bestMoves.add(possibleMove);
            }
        }

        var move = bestMoves.get(new Random().nextInt(0, bestMoves.size()));
        //System.out.println((firstPlayer ? "Player 1" : "Player 2") + " makes move: " + move.x + " " + move.y);
        return move;
    }

    Point getSmartMove(Board board) {
        int maxEval = -10000;
        List<Point> bestMoves = new ArrayList<>();
        for (Point possibleMove : board.getPossibleMoves()) {
            var boardClone = new char[8][8];
            for (int i = 0; i < board.getBoard().length; i++) {
                boardClone[i] = board.getBoard()[i].clone();
            }

            var eval = evaluatePosition(boardClone, possibleMove);

            boardClone[possibleMove.y][possibleMove.x] = firstPlayer ? 'b' : 'w';
            var pointsToRecolor = getAllPointsToRecolor(boardClone, possibleMove.x, possibleMove.y);
            for (Point point : pointsToRecolor) {
                boardClone[point.y][point.x] = firstPlayer ? 'b' : 'w';
            }

            var possibleResponses = board.getPossibleMoves(boardClone, !firstPlayer);
            var maxResponseEval = 0;
            firstPlayer = !firstPlayer;
            for (Point possibleResponse : possibleResponses) {
                var responseEval = evaluatePosition(boardClone, possibleResponse);
                maxResponseEval = Math.max(maxResponseEval, responseEval);
            }
            firstPlayer = !firstPlayer;

            var posEval = eval - maxResponseEval;
            if (maxEval < posEval) {
                maxEval = posEval;
                bestMoves.clear();
                bestMoves.add(possibleMove);
            } else if (maxEval == posEval) {
                bestMoves.add(possibleMove);
            }
        }

        var move = bestMoves.get(new Random().nextInt(0, bestMoves.size()));
        //System.out.println((firstPlayer ? "Player 1" : "Player 2") + " makes move: " + move.x + " " + move.y);
        return move;
    }

    int evaluatePosition(char[][] board, Point move) {
        var points = getAllPointsToRecolor(board, move.x, move.y);

        int evaluation = 0;

        if ((move.x == 0 || move.x == 7) && (move.y == 0 || move.y == 7)) {
            evaluation += 0.8;
        } else if (move.x == 0 || move.x == 7 || move.y == 0 || move.y == 7) {
            evaluation += 0.4;
        }

        for (Point point : points) {
            if (point.x == 0 || point.x == 7 || point.y == 0 || point.y == 7) {
                evaluation += 2;
            } else {
                evaluation += 1;
            }
        }
        return evaluation;
    }

    HashSet<Point> getAllPointsToRecolor(char[][] board, int x, int y) {
        HashSet<Point> set = new HashSet<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                var points = getPointToRecolor(board, x, y, i, j);
                if (points == null) {
                    continue;
                }
                set.addAll(points);
            }
        }
        return set;
    }

    HashSet<Point> getPointToRecolor(char[][] board, int x, int y, int dirX, int dirY) {
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
}
