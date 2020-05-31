package pg.eti.inf.wdc.project;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.*;

public class EncodingWindow {
    public Button fileChooser;
    public ImageView image;
    public TextArea text;
    public Label fileInfo;
    public Button doAction;
    public Button pathChooser;
    public Label pathInfo;
    private File toCrypt; // file to encrypt or decrypt
    private File destination;

    public void swap(ActionEvent actionEvent) {
        MultiWindowFunctions.createNewWindow(this, actionEvent, "sample.fxml", new Pair<>(300, 275));
    }

    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            fileInfo.setText(selectedFile.getName());
            doAction.setDisable(false);
            toCrypt = selectedFile;
        }
    }

    /**
     * TODO encrypting or decrypting file then view
     */
    public void coding() {
        String extension = MultiWindowFunctions.getFileExtension(toCrypt);
        if (MultiWindowFunctions.checkIfText(extension)) {

            try {
                FileReader fileReader = new FileReader(toCrypt);
                char[] buffer = new char[80];
                int read;
                while((read = fileReader.read(buffer))>0) {
                    text.appendText(String.valueOf(buffer).substring(0, read));
                }
                text.setVisible(true);
                image.setVisible(false);
            } catch (FileNotFoundException e) {
                MultiWindowFunctions.showAlert("FILE NOT FOUND!", "Looks like file does not exist or was deleted...");
            } catch (IOException e) {
                MultiWindowFunctions.showAlert("Problem with reading file!", "File is corrupted...");
            }
        }
        else if (MultiWindowFunctions.checkIfImage(extension)) {
            try {
                Image imageToView = new Image(toCrypt.toURI().toURL().toExternalForm());
                image.setImage(imageToView);
                image.setFitHeight(200.0);
                image.setFitWidth(500.0);
                image.setVisible(true);
                text.setVisible(false);
            } catch (IOException e) {
                MultiWindowFunctions.showAlert("Problem with reading file!", "File is corrupted...");
            }
        }
        else {
            image.setVisible(false);
            text.setVisible(false);
            System.out.println("Gonna implement it later");
        }
    }

    public void choosePath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDir = directoryChooser.showDialog(null);
        if (selectedDir != null) {
            pathInfo.setText(selectedDir.getName());
            doAction.setDisable(false);
            destination = selectedDir;
        }
    }
}
