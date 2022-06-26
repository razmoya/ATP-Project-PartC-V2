package Model;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.scene.input.KeyCode;

import java.io.File;

public interface IModel {

    int[][] generateMaze(int row, int col);

    void move(KeyCode movement);

    int [][] solve(MyViewModel m, int charRow, int charCol, String x);

    boolean gameOver();

    void save(File file);

    void load(File file);

    boolean isSolved();

    int[][] getMaze();

    Maze getOriginMaze();

    int[][] getSolutionArray();

    int getPositionRow();

    int getPositionCol();

    Position getEndPosition();

    void setOriginMaze(Maze m);

    void setMaze(int[][] maze);

    void setPositionCol(int col);

    void setPositionRow(int row);

    void setGoalPosition(Position goalPosition);
}