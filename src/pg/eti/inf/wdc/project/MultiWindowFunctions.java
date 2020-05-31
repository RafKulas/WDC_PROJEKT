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
            MultiWindowFunctions.showAlert("Opening window went wrong!", "Looks like source file was deleted...");
        }
    }

    protected static String getFileExtension(File file) {
        String name = file.getName();
        return name.substring(name.lastIndexOf('.'));
    }

    protected static void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong...");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    protected static boolean checkIfText(String extension) {
        return extension.equals(".txt");
    }

    protected static boolean checkIfImage(String extension) {
        return  extension.equals(".png") || extension.equals(".jpg") ||
                extension.equals(".jpeg");
    }
}
