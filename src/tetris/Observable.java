package tetris;

/**
 * Created by thomas on 1/29/14.
 */
public interface Observable {
    public void registerObserver(Observer observer);
    public void notifyObservers();

}
