package View;

import algorithms.mazeGenerators.Position;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;




public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private int[][] solutionArray;

    private int row;
    private int col;

    private Position endPosition;

    private boolean gameFinish;

    public void setMaze(int[][] maze) {
        this.maze = maze;
        redraw();
    }

    public void endPosition(Position end) {
        endPosition=end;

    }

    public void Solved(int[][] answer) {
        solutionArray = answer;
    }


    public void setCharacterPosition(int row, int column) {
        this.row = row;
        this.col = column;
    }

    public void redraw() {
        if (maze != null) {

            double canvasHeight = getHeight();
            double canvasWidth =getWidth() ;
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;
            System.out.println(canvasHeight+ ""+canvasWidth);

            try {
                GraphicsContext graphicsContext2D = getGraphicsContext2D();
                graphicsContext2D.clearRect(0, 0, getWidth(), getHeight());
                Image wallImage = new Image(new FileInputStream("resources/images/CardWall.jpeg"));
                Image charaImage= new Image(new FileInputStream("resources/images/AliceCharacter.jpeg"));

                //Draw Maze
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[i].length; j++) {
                        if (maze[i][j] == 1) {
                            graphicsContext2D.drawImage(wallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }
                    }
                }
                //Draw character
                graphicsContext2D.drawImage(charaImage ,col * cellWidth, row * cellHeight, cellWidth, cellHeight);

                //draw end point
                Image endPos = new Image(new FileInputStream("resources/images/GoalRabbit.jpeg"));
                graphicsContext2D.drawImage(endPos, endPosition.getColumnIndex() * cellWidth, endPosition.getRowIndex() * cellHeight, cellWidth, cellHeight);

                //Draw solution
                if (gameFinish) {
                    Image SolutionImage = new Image(new FileInputStream("resources/images/Path.jpg"));
                    for (int i = 1; i < solutionArray[0].length - 1; i++) {
                        int x = solutionArray[0][i];
                        int y = solutionArray[1][i];
                        graphicsContext2D.drawImage(SolutionImage, y * cellWidth, x * cellHeight, cellWidth, cellHeight);
                    }
                }
            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(String.format("Image not exist: %s", e.getMessage()));
                alert.show();
            }
        }
    }

    public void isSolved(boolean solved) {
        this.gameFinish = solved;
    }

    public void setGoalPosition(Position goalPosition) {
        this.endPosition = goalPosition;
    }

    public void setSolution(int[][] sol) {
        solutionArray=sol;
    }
}