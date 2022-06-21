package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;

    private int rowIndex;
    private int colIndex;

    public StringProperty row;
    public StringProperty col;

    public MyViewModel(IModel model) {
        this.model = model;
        rowIndex=0;
        colIndex=0;
        row = new SimpleStringProperty("1");
        col = new SimpleStringProperty("1");
    }


    public int[][] generateMaze(int row, int col) {
        return model.generateMaze(row,col);
    }


    public void move(KeyCode movement) {
        model.move(movement);
    }


    public int [][] solution(MyViewModel m, int charRow, int charCol, String x) {
        return model.solution(m,charRow,charCol,x);
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


    public boolean gameIsFinish() {
        return model.gameIsFinish();
    }



    public int[][] getMaze() {
        return  model.getMaze();
    }

    public Maze getFirstMaze() {
        return model.getFirstMaze();
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


    public void setFirstMaze(Maze m) {

    }


    public void setMaze(int[][] maze) {
        model.setMaze(maze);
    }


    public void setPositionCol(int col) {
        model.setPositionCol(col);
    }

    public void setPositionRow(int row) {
        model.setPositionRow(row);
    }


    public void setGoalPosition(Position goalPosition) {
        model.setGoalPosition(goalPosition);
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