package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.*;

import java.io.*;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

public class MyViewController implements Observer, IView {
    @FXML
    private MyViewModel viewModel = new MyViewModel(new MyModel());
    public MazeDisplayer mazeDisplayer = new MazeDisplayer();
    int i = 0;
    boolean showOnce = false;
    boolean songonce = true;
    Thread t1;
    boolean hint = false;
    public javafx.scene.control.TextField txt_row;
    public javafx.scene.control.TextField txt_col;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;//where user wants to go
    public javafx.scene.control.Button GenerateMaze;
    public javafx.scene.control.Button SolveMaze;
    public javafx.scene.control.Button Hint;
    public javafx.scene.control.Button save;
    private MediaPlayer temp;
    private int height;
    private int width;

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

    public void setResizeEvent() {
        this.mazeDisplayer.widthProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.redraw();
        });

        this.mazeDisplayer.heightProperty().addListener((observable, oldValue, newValue) -> {
            mazeDisplayer.redraw();
        });
    }
    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            mazeDisplayer.endposition(viewModel.getEndPosition());
            mazeDisplayer.setMaze(viewModel.getMaze());
            mazeDisplayer.setCharacterPosition(viewModel.getPositionRow(), viewModel.getPositionCol());
            mazeDisplayer.setGoalPosition(viewModel.getEndPosition());
            displayMaze(viewModel.getMaze());
            GenerateMaze.setDisable(false);
            if (viewModel.gameIsFinish() && !showOnce) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Game Done");
                Music(1);
                alert.show();
                showOnce = true;
            }
            //mazeDisplayer.setCharacterPosition(mazeDisplayer.getCharacterPositionRow(),mazeDisplayer.getCharacterPositionColumn());
            mazeDisplayer.redraw();
        }
    }

    @Override
    public void displayMaze(int[][] maze) {
        int characterPositionRow = viewModel.getPositionRow();
        int characterPositionColumn = viewModel.getPositionCol();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        mazeDisplayer.endposition(viewModel.getEndPosition());
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
        if (songonce == true)
            Music(0);
        save.setVisible(true);
        showOnce = false;
        try {
            height = Integer.valueOf(txt_row.getText());
        } catch (Exception e) {
            height = 10;
        }
        try {
            width = Integer.valueOf(txt_col.getText());
        } catch (Exception e) {
            width = 10;
        }
        int[][] temp = viewModel.generateMaze(height, width);
        mazeDisplayer.endposition(viewModel.getEndPosition());
        mazeDisplayer.setMaze(temp);
        SolveMaze.setVisible(true);
        displayMaze(temp);

    }

    public void solveMaze(ActionEvent actionEvent) {
        int[][] temp = viewModel.getMaze();
        showAlert("Just a minute... solving maze");
        int [][] sol=viewModel.solution(this.viewModel, this.viewModel.getPositionRow(), this.viewModel.getPositionCol(), "solve");
        mazeDisplayer.setSolution(sol);
        mazeDisplayer.isSolved(true);
        mazeDisplayer.setMaze(temp);
        SolveMaze.setVisible(false);
    }

    public void getHint(ActionEvent actionEvent) {
        int[][] temp = viewModel.getMaze();
        int [][] sol=viewModel.solution(this.viewModel, this.viewModel.getPositionRow(), this.viewModel.getPositionCol(), "clue");
        mazeDisplayer.setSolution(sol);
        mazeDisplayer.isSolved(true);
        mazeDisplayer.setMaze(temp);
        SolveMaze.setVisible(true);
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void setMazeOriginal(Maze m) {
        viewModel.setFirstMaze(m);
    }



    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void KeyPressed(KeyEvent key) {
        viewModel.move(key.getCode());
        key.consume();

    }

    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }

    public StringProperty characterPositionRowProperty() {
        return characterPositionRow;
    }

    public String getCharacterPositionColumn() {
        return characterPositionColumn.get();
    }

    public StringProperty characterPositionColumnProperty() {
        return characterPositionColumn;
    }

    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                mazeDisplayer.redraw();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                mazeDisplayer.redraw();
            }
        });
    }

    public void MazeInfo() {
        String text = null;
        OutputStream output = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("./resources/config.properties"));
            if (bufferedReader == null) {//check if file exthist
                output = new FileOutputStream("Resources/config.properties");
            }
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line + ",");
                stringBuilder.append(System.lineSeparator());
                line = bufferedReader.readLine();
            }
            text= stringBuilder.toString();
            bufferedReader.close();
        } catch (IOException e) { }
        if(text==null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Setting Found");
            alert.setContentText("Please go to option and set what settings you want");
            alert.show();
        }
        else {
            String[] split = text.split(",");
            String content = "";
            content = "Maze Algo: " + splitLine(split[0] + "\n");
            content += "Thread Number: " + splitLine(split[1].substring(4) + "\n");
            content += "Maze Type: " + splitLine(split[2].substring(4) + "\n");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Properties");
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.show();
        }
    }

    private String splitLine(String s) {
        String [] splitedLine = s.split("=");
        return splitedLine[1];
    }

    public void About(ActionEvent actionEvent) {
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
        } catch (Exception e) {
//            System.out.println(e);
            System.out.println("Error About.fxml not found");
        }
    }

    public void Help(ActionEvent actionEvent) {
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
        if (temp != null)
            temp.stop();
        String path = "";
        if (x == 0) {
            songonce = false;
            path = "resources\\Wii.mp3";
        }
        else {
            songonce=true;
            path = "resources\\Wii.mp3";
        }
//        Media tempp = new Media(Paths.get(path).toUri().toString());
        Media tempp = new Media(getClass().getResource("/Wii.mp3").toString());
        temp = new MediaPlayer(tempp);
        temp.play();
    }

    public void Option(ActionEvent actionEvent) {
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
        if (!filePath.exists())
            filePath.mkdir();
        fc.setTitle("Saving maze");
        fc.setInitialFileName("Maze Number " + i + "");
        i++;
        fc.setInitialDirectory(filePath);
        File file = fc.showSaveDialog((Stage) mazeDisplayer.getScene().getWindow());
        if (file != null)
            viewModel.save(file);
    }

    public void loadGame() {

        FileChooser fc = new FileChooser();
        fc.setTitle("Loading maze");
        File filePath = new File("./Mazes/");
        if (!filePath.exists())
            filePath.mkdir();
        fc.setInitialDirectory(filePath);

        File file = fc.showOpenDialog(new PopupWindow() {
        });
        if (file != null && file.exists() && !file.isDirectory()) {
            viewModel.load(file);
            if (songonce==true)
                Music(0);
            mazeDisplayer.redraw();
        }
    }
    public void mouseClicked(MouseEvent mouseEvent) {
        this.mazeDisplayer.requestFocus();
    }
}
