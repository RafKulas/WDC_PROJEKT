package pg.eti.inf.wdc.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EncodingWindow {
    public void swap(ActionEvent actionEvent) throws IOException {
        Parent home_page = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage app = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        app.setScene(new Scene(home_page, 600, 500));
        app.show();
    }
}
