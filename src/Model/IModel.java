package Model;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.scene.input.KeyCode;

import java.io.File;

public interface IModel {

    int[][] generateMaze(int row, int col);

    void move(KeyCode movement);

    int [][] solution(MyViewModel m, int charRow, int charCol, String x);

    boolean gameIsFinish();

    void save(File file);

    boolean isSolved();

    void load(File file);



    int[][] getMaze();

    Maze getFirstMaze();

    int[][] getSolutionArray();

    int getPositionRow();

    int getPositionCol();

    Position getEndPosition();



    void setFirstMaze(Maze m);

    void setMaze(int[][] maze);

    void setPositionCol(int col);

    void setPositionRow(int row);

    void setGoalPosition(Position goalPosition);

}