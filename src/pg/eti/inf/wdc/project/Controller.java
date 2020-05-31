package pg.eti.inf.wdc.project;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;

import static pg.eti.inf.wdc.project.MultiWindowFunctions.createNewWindow;

public class Controller {

    public Button coding;
    public Button teaching;
    public Button exit;

    @FXML
    protected void exitApp() {
        Platform.exit();
    }

    public void swap(ActionEvent actionEvent) throws IOException {
        createNewWindow(this, actionEvent, "encoding_window.fxml", new Pair<>(600, 400));
    }
}
