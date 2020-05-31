package pg.eti.inf.wdc.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;

public class MultiWindowFunctions {
    protected static void createNewWindow(Object controller, ActionEvent actionEvent, String fxmlFile,
                                          Pair<Integer, Integer> size)
    {
        try
        {
            Parent home_page = FXMLLoader.load(controller.getClass().getResource(fxmlFile));
            Stage app = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            app.setScene(new Scene(home_page, size.getKey(), size.getValue()));
            app.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    protected static String getFileExtention(File file) {
        String name = file.getName();
        return name.substring(name.lastIndexOf('.'), name.length());
    }

    protected static void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong...");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
