package View;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class AboutController extends Observable implements Initializable {
    public javafx.scene.control.Button close;
    public javafx.scene.control.Label textInformation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textInformation.setWrapText(true);
        textInformation.setText("We are Gad and Raz and we hope you enjoy to play\n" +
                "We made for you a game for solve mazes.\n" +
                "We use Prim's Algorithms to build random mazes.\n" +
                "You can try to solve alone, use a hint or to see the complete solution. the algorithms we used to find a solution are:" +
                " DFS, BFS,BEST FS.");

    }

    public void close() {
        Platform.exit();
    }

    public void closeWindow() {
        Stage s = (Stage) close.getScene().getWindow();
        s.close();
    }
}