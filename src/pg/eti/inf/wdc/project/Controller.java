package pg.eti.inf.wdc.project;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.util.Pair;

import static pg.eti.inf.wdc.project.MultiWindowFunctions.createNewWindow;

public class Controller {

    public Button coding;
    public Button teaching;
    public Button exit;

    @FXML
    protected void exitApp() {
        Platform.exit();
    }

    public void swap(ActionEvent actionEvent) {
        createNewWindow(this, actionEvent, "encoding_window.fxml", new Pair<>(600, 400));
    }
}
