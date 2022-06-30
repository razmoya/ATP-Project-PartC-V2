package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * This class merges between the input from the View and the logic from the Model.
 * Functions logic comes from the model.
 */
public class MyViewModel extends Observable implements Observer {

    private final IModel model;

    private int rowIndex;
    private int colIndex;

    public StringProperty row;
    public StringProperty col;

    /**
     * ViewModel constructor
     * @param model a model object
     */
    public MyViewModel(IModel model) {
        this.model = model;
        rowIndex=0;
        colIndex=0;
        row = new SimpleStringProperty("0");
        col = new SimpleStringProperty("0");
    }

    public int[][] generateMaze(int row, int col) {
        return model.generateMaze(row,col);
    }

    public void move(KeyCode movement) {
        model.move(movement);
    }

    public int [][] solve(MyViewModel m, int charRow, int charCol, String x) {
        return model.solve(m,charRow,charCol,x);
    }

    public void save(File file) {
        model.save(file);
    }

    public void load(File file) {
        model.load(file);
    }

    public boolean isSolved() {
        return model.isSolved();
    }

    public boolean gameIsOver() {
        return model.gameOver();
    }

    public int[][] getMaze() {
        return  model.getMaze();
    }

    public int[][] getSolutionArray() {
        return model.getSolutionArray();
    }

    public int getPositionRow() {
        return this.rowIndex;
    }

    public int getPositionCol() {
        return this.colIndex;
    }

    public Position getEndPosition() {
        return model.getEndPosition();
    }

    public void setMaze(int[][] maze) {
        model.setMaze(maze);
    }

    @Override
    public void update(Observable obs, Object arg) {
        if (obs == model) {
            rowIndex = model.getPositionRow();
            row.set(rowIndex + "");
            colIndex = model.getPositionCol();
            col.set(colIndex + "");
        }
        setChanged();
        notifyObservers();
    }
}