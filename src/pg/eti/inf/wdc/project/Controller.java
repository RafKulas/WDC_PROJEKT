package pg.eti.inf.wdc.project;

import static pg.eti.inf.wdc.project.MultiWindowFunctions.createNewWindow;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Objects;

public class Controller
{
    //Main menu buttons
    public Button coding;
    public Button teaching;
    public Button exit;

    @FXML
    protected void exitApp() {
        Platform.exit();
    }

    @FXML
    protected void educationalMode(ActionEvent event)
    {
        createNewWindow(this, event, "EducationalMode.fxml", new Pair<>(600,400));
    }

}
