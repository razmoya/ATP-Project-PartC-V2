package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.*;

import java.io.*;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

/**
 * This class controls the game visualizations through the ViewModel and the Maze Display
 */
public class MyViewController implements Observer, IView {
    @FXML
    private MyViewModel viewModel = new MyViewModel(new MyModel());
    public MazeDisplayer mazeDisplayer = new MazeDisplayer();
    int i = 0;
    boolean showOnce = false;
    public javafx.scene.control.TextField txt_row;
    public javafx.scene.control.TextField txt_col;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button GenerateMaze;
    public javafx.scene.control.Button SolveMaze;
    public javafx.scene.control.Button Hint;
    public javafx.scene.control.Button save;
    private MediaPlayer temp;

    public StringProperty characterPositionRow = new SimpleStringProperty();
    public StringProperty characterPositionColumn = new SimpleStringProperty();

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        bindProperties(viewModel);
    }

    private void bindProperties(MyViewModel viewModel) {
        lbl_rowsNum.textProperty().bind(viewModel.row);
        lbl_columnsNum.textProperty().bind(viewModel.col);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            mazeDisplayer.endPosition(viewModel.getEndPosition());
            mazeDisplayer.setMaze(viewModel.getMaze());
            mazeDisplayer.setCharacterPosition(viewModel.getPositionRow(), viewModel.getPositionCol());
            mazeDisplayer.setGoalPosition(viewModel.getEndPosition());
            displayMaze(viewModel.getMaze());
            GenerateMaze.setDisable(false);
            if (viewModel.gameIsOver() && !showOnce) {
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                alert.getDialogPane().lookupButton(ButtonType.CLOSE).setStyle("-fx-font: 24 david;");
                alert.setContentText("Alice is free!");
                alert.getDialogPane().setStyle("-fx-font: 48 david;");
                try {
                    Image image = new Image(new FileInputStream("resources/images/EndPic.jpeg"));
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(300);
                    imageView.setFitWidth(300);
                    alert.setGraphic(imageView);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Music(0);
                alert.show();
                alert.getDialogPane().lookupButton(ButtonType.CLOSE).addEventFilter(ActionEvent.ACTION, event ->
                        temp.stop()
                );
                showOnce = true;
            }
            mazeDisplayer.redraw();
        }
    }

    @Override
    public void displayMaze(int[][] maze) {
        int characterPositionRow = viewModel.getPositionRow();
        int characterPositionColumn = viewModel.getPositionCol();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        mazeDisplayer.endPosition(viewModel.getEndPosition());
        mazeDisplayer.Solved(viewModel.getSolutionArray());
        mazeDisplayer.isSolved(viewModel.isSolved());
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");
        if (viewModel.isSolved())
            mazeDisplayer.redraw();
    }

    public void generateMaze() {
        mazeDisplayer.isSolved(false);
        Hint.setDisable(false);
        save.setVisible(true);
        showOnce = false;
        Music(1);
        int height;
        try {
            height = Integer.parseInt(txt_row.getText());
        } catch (Exception e) {
            height = 10;
        }
        int width;
        try {
            width = Integer.parseInt(txt_col.getText());
        } catch (Exception e) {
            width = 10;
        }
        int[][] temp = viewModel.generateMaze(height, width);
        mazeDisplayer.endPosition(viewModel.getEndPosition());
        mazeDisplayer.setMaze(temp);
        SolveMaze.setVisible(true);
        displayMaze(temp);
//        Stage s = (Stage) mazeDisplayer.getScene().getWindow();
//        s.requestFocus();
    }

    public void solveMaze() {
        int[][] temp = viewModel.getMaze();
        showAlert();
        int [][] sol=viewModel.solve(this.viewModel, this.viewModel.getPositionRow(), this.viewModel.getPositionCol(), "solve");
        mazeDisplayer.setSolution(sol);
        mazeDisplayer.isSolved(true);
        mazeDisplayer.setMaze(temp);
        SolveMaze.setVisible(false);
    }

    public void getHint() {
        int[][] temp = viewModel.getMaze();
        int [][] sol=viewModel.solve(this.viewModel, this.viewModel.getPositionRow(), this.viewModel.getPositionCol(), "clue");
        mazeDisplayer.setSolution(sol);
        mazeDisplayer.isSolved(true);
        mazeDisplayer.setMaze(temp);
        SolveMaze.setVisible(true);
    }

    public void exit() {
        Platform.exit();
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Hold on... Maze is being solved!");
        alert.show();
    }

    public void KeyPressed(KeyEvent key) {
        viewModel.move(key.getCode());
        key.consume();
    }

    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> mazeDisplayer.redraw());
        scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> mazeDisplayer.redraw());
    }

    public void About() {
        try {
            Stage stage = new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root);
            scene.getStylesheets().add("box.css");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e)
        {
            System.out.println("Error About.fxml not found");
        }
    }

    public void Help(){
        try {
            Stage stage = new Stage();
            stage.setTitle("Help");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
            Scene scene = new Scene(root);
            scene.getStylesheets().add("box.css");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
            System.out.println("Error Help.fxml not found");
        }
    }

    public void Music(int x) {
        String path = "";
        if (temp != null)
            temp.stop();
        if (x == 0){
            path = "resources\\song.mp3";
        }
        if (x == 1){
            path = "resources\\Theme.mp3";
        }
        Media temporal = new Media(Paths.get(path).toUri().toString());
        temp = new MediaPlayer(temporal);
        temp.play();
    }

    public void Option() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Option");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Option.fxml").openStream());
            Scene scene = new Scene(root);
            scene.getStylesheets().add("box.css");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
            System.out.println("Error Option.fxml not found");
        }
    }

    public void saveGame() {
        FileChooser fc = new FileChooser();
        File filePath = new File("./Mazes/");
        boolean res = true;
        if (!filePath.exists())
            res = filePath.mkdir();
        if (res) {
            fc.setTitle("Saving maze");
            fc.setInitialFileName("Maze Number " + i + "");
            i++;
            fc.setInitialDirectory(filePath);
            File file = fc.showSaveDialog(mazeDisplayer.getScene().getWindow());
            if (file != null)
                viewModel.save(file);
        }
    }

    public void loadGame() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Loading maze");
        File filePath = new File("./Mazes/");
        boolean res = true;
        if (!filePath.exists())
            res = filePath.mkdir();
        if (res){
            fc.setInitialDirectory(filePath);
            File file = fc.showOpenDialog(new PopupWindow() {
            });
            if (file != null && file.exists() && !file.isDirectory()) {
                viewModel.load(file);
//                if (songOnce)
//                    Music(0);
                mazeDisplayer.redraw();
            }
        }
    }
    public void mouseClicked() {
        this.mazeDisplayer.requestFocus();
    }
}
