package pg.eti.inf.wdc.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;

import static pg.eti.inf.wdc.project.MultiWindowFunctions.createNewWindow;

public class EncodingWindow {
    public void swap(ActionEvent actionEvent) throws IOException {
        createNewWindow(this, actionEvent, "sample.fxml", new Pair<>(300, 275));
    }
}
