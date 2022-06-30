package View;

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
        textInformation.setText("""
                This is a maze game based on Alice In Wonderland!
                The maze is built using a Prim based algorithm.
                It is solved using one of three search algorithms:
                BFS, DFS and Best FS.
                This game is brought to you by Gad Miller and Raz Moyal.""");
    }

    public void closeWindow() {
        Stage s = (Stage) close.getScene().getWindow();
        s.close();
    }
}