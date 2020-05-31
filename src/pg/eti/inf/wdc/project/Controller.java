package pg.eti.inf.wdc.project;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Controller
{
    //Main menu buttons
    public Button coding;
    public Button teaching;
    public Button exit;

    //Educational mode buttons
    @FXML public Button confirmModeChoice;
    @FXML public Button confirmEncription;
    @FXML public Button confirmDecription;

    //Educational mode radio buttons
    @FXML public RadioButton ecbMode;
    @FXML public RadioButton cbcMode;
    @FXML public RadioButton ctrMode;
    @FXML public RadioButton cfbMode;
    @FXML public RadioButton ofbMode;
    @FXML public ToggleGroup cipherModesGroup;

    //Educational mode text fields
    @FXML public TextField planeText;
    @FXML public TextField encryptedText;
    @FXML public TextField initializationVectorText;

    //Educational mode colorful labels
    @FXML public Label encryptedLabel;
    @FXML public Label decryptedLabel;

    @FXML
    protected void exitApp() {
        Platform.exit();
    }

    @FXML
    protected void educationalMode(ActionEvent event)
    {
        createNewWindow((Node)(event.getSource()), "Educational Mode", "educationalModeSample.fxml", 600, 400);
        hideForChoosingMode();

    }

    protected void createNewWindow(Node oldWindow, String title, String fxmlFile, int width, int height)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.show();
            (oldWindow).getScene().getWindow().hide();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected void initAll()
    {
        
    }

    protected void hideForChoosingMode()
    {
        confirmEncription.setVisible(false);
        confirmDecription.setVisible(false);
        planeText.setVisible(false);
        encryptedText.setVisible(false);
        initializationVectorText.setVisible(false);
        encryptedLabel.setVisible(false);
        decryptedLabel.setVisible(false);
    }
}
