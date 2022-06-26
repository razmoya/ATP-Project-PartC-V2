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
                Help Alice reach the white rabbit and escape from the queen of hearts.
                If you're having difficulties, try getting a clue or use the auto solver.
                This game is brought to you by Gad and Raz LTD.""");
    }

    public void closeWindow() {
        Stage s = (Stage) close.getScene().getWindow();
        s.close();
    }
}