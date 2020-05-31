package pg.eti.inf.wdc.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;

import java.io.*;
import java.util.Arrays;

import pg.eti.inf.wdc.project.MultiWindowFunctions.*;

public class EncodingWindow {
    public Button chooser;
    public ImageView image;
    public TextArea text;
    public ChoiceBox todo;
    public Label info;
    public Button doAction;
    private File toCrypt; // file to encrypt or decrypt

    public void swap(ActionEvent actionEvent) throws IOException {
        MultiWindowFunctions.createNewWindow(this, actionEvent, "sample.fxml", new Pair<>(300, 275));
    }

    public void chooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            info.setText(selectedFile.getName());
            doAction.setDisable(false);
            toCrypt = selectedFile;
        }
    }

    public void coding(ActionEvent actionEvent) {
        String extension = MultiWindowFunctions.getFileExtention(toCrypt);
        if (extension.equals(".txt")) {
            try {
                FileReader fileReader = new FileReader(toCrypt);
                char[] buffer = new char[80];
                int read;
                while((read = fileReader.read(buffer))>0) {
                    text.appendText(String.valueOf(buffer).substring(0, read));
                }
                text.setVisible(true);
            } catch (FileNotFoundException e) {
                MultiWindowFunctions.showAlert("FILE NOT FOUND!", "Looks like file does not exist or was deleted...");
            } catch (IOException e) {
                MultiWindowFunctions.showAlert("Problem with reading file!", "File is corrupted...");
            }
        }
    }
}
