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
    private int currentLevel = 0;
    private List<Observer> observers;

    private Map<Tetromino, Integer> statistics = new HashMap<Tetromino, Integer>();

    public int getScore() {
        return score;
    }

    public int getNumLinesRemoved() {
        return numLinesRemoved;
    }

    public void setNumLinesRemoved(int numLinesRemoved, int hardDropModifier) {
        if (numLinesRemoved > 0 && numLinesRemoved <= 4) {
            int scoreModifier;
            switch(numLinesRemoved) {
                case 2:
                    scoreModifier = 300;
                    break;
                case 3:
                    scoreModifier = 500;
                    break;
                case 4:
                    scoreModifier = 800;
                    break;
                default:
                    scoreModifier = 100;
                    break;
            }
            score +=  (scoreModifier * (currentLevel + 1)) + (hardDropModifier * 2);

            this.numLinesRemoved += numLinesRemoved;
            int level = score / 1000;

            if (level != currentLevel) {
                setLevel(level);
            }
        }
    }

    public void setLevel(int level) {
        currentLevel = level;
        notifyObservers();
    }

    public void incTetromino(Tetromino tetromino) {
        numPieces++;
        Integer count = statistics.get(tetromino);
        statistics.put(tetromino, count == null ? 0 : ++count);
    }

    public void reset() {
        numPieces = 0;
        numLinesRemoved = 0;
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
