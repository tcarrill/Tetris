package tetris.model;

import tetris.Observable;
import tetris.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 1/23/14.
 */
public class Score implements Observable {
    private int score = 0;
    private int numPieces = 0;
    private int numLinesRemoved = 0;
    private int numTetris = 0;
    private int currentLevel = 1;
    private List<Observer> observers;

    private Map<Tetromino, Integer> statistics = new HashMap<Tetromino, Integer>();

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumPieces() {
        return numPieces;
    }

    public void setNumPieces(int numPieces) {
        this.numPieces = numPieces;
    }

    public int getNumLinesRemoved() {
        return numLinesRemoved;
    }

    public void setNumLinesRemoved(int numLinesRemoved) {
        score += numLinesRemoved * 100;
        this.numLinesRemoved += numLinesRemoved;

        if (this.numLinesRemoved % 10 == 0) {
            currentLevel++;
        }
    }

    public int getNumTetris() {
        return numTetris;
    }

    public void setNumTetris(int numTetris) {
        this.numTetris = numTetris;
    }

    public int getTetrominoCount(Tetromino tetromino) {
        return statistics.get(tetromino);
    }

    public void incTetromino(Tetromino tetromino) {
        numPieces++;
        Integer count = statistics.get(tetromino);
        statistics.put(tetromino, count == null ? 0 : ++count);
    }

    public void incTetris() {
        numTetris++;
        score += 800;
    }

    public void reset() {
        numPieces = 0;
        numLinesRemoved = 0;
        numTetris = 0;
        score = 0;
        statistics.clear();
    }

    @Override
    public void registerObserver(Observer observer) {
        if (observers == null) {
            observers = new ArrayList<Observer>();
        }

        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
