package pg.eti.inf.wdc.project;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller
{

    public Button coding;
    public Button teaching;
    public Button exit;

    @FXML
    protected void exitApp() {
        Platform.exit();
    }

    @FXML
    protected void educationalMode()
    {
        
    }
}
