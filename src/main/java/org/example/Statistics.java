package org.example;

public class Statistics {
    int bestScore;

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = Math.max(this.bestScore, bestScore);
    }
}
