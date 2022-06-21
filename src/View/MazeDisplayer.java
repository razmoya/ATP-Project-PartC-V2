package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import javax.swing.plaf.synth.SynthGraphicsUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;



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

    public void endposition(Position end) {
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
                Image wallImage = new Image(new FileInputStream("resources/images/6252539.jpg"));
                Image charaImage= new Image(new FileInputStream("resources/images/b.png"));

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
                Image endPos = new Image(new FileInputStream("resources/images/land.png"));
                graphicsContext2D.drawImage(endPos, endPosition.getColumnIndex() * cellWidth, endPosition.getRowIndex() * cellHeight, cellWidth, cellHeight);

                //Draw solution
                if (gameFinish) {
                    Image SolutionImage = new Image(new FileInputStream("resources/images/steps.jpg"));
                    for (int i = 0; i < solutionArray[0].length - 1; i++) {
                        int x = solutionArray[0][i];
                        int y = solutionArray[1][i];
                        graphicsContext2D.drawImage(SolutionImage, y * cellWidth, x * cellHeight, cellWidth, cellHeight);
                    }
                }
//                Image StartPoint = new Image(new FileInputStream("resources/images/knesset.jpg"));
//                graphicsContext2D.drawImage(StartPoint, 0, 0, cellWidth, cellHeight);
            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(String.format("Image not exist: %s", e.getMessage()));
                alert.show();
            }
        }
    }
    ////////////////////////check if we need
    //region Properties
    private StringProperty ImageFileWall = new SimpleStringProperty();
    private StringProperty ImageFileCharacter = new SimpleStringProperty();

    public String getImageFileNameWall() {
        return ImageFileWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileWall.set(imageFileNameWall);
    }

    public String getImageFileNameCharacter() {
        return ImageFileCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileCharacter.set(imageFileNameCharacter);
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