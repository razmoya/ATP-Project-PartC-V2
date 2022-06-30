package View;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable{
    public javafx.scene.control.Button close;
    public javafx.scene.control.Label textInformation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textInformation.setWrapText(true);
        textInformation.setText("""
                How to play:
                Up = 8/UP       Up & Left = 7
                Down = 2/DOWN     Up & Right = 9
                Left = 4/LEFT     Down & Left = 1
                Right = 6/RIGHT    Down & Right = 3
                
                Goal:
                Help Alice reach the white rabbit and escape from the queen of hearts.
                Avoid the cards and find the correct path.
                If you're having difficulties, try getting a clue or use the auto solver.""");
    }

    public void closeWindow() {
        Stage s = (Stage) close.getScene().getWindow();
        s.close();
    }
}
