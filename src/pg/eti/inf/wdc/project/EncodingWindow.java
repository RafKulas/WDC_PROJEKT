package pg.eti.inf.wdc.project;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import pg.eti.inf.wdc.project.aes.AES;
import pg.eti.inf.wdc.project.aes.ECB;

import java.io.*;

public class EncodingWindow {
    // Buttons
    public Button fileChooser;
    public Button doAction;
    public Button pathChooser;

    public ToggleGroup cipherModesGroup;

    //RadioButtons
    public RadioButton ctrMode;
    public RadioButton cfbMode;
    public RadioButton ofbMode;
    public RadioButton cbcMode;
    public RadioButton ecbMode;


    //Labels
    public Label fileInfo;
    public Label pathInfo;
    public Label chooseModeTitleLabel;

    //File view
    public ImageView image;
    public TextArea text;

    //File
    private File toCrypt = null; // file to encrypt or decrypt
    private File postCrypt = null;
    private File destination = null;

    //AES
    private AES aes;

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
            toCrypt = selectedFile;
            if (destination!=null) {
                doAction.setDisable(false);
            }
        }
    }

    public void choosePath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDir = directoryChooser.showDialog(null);
        if (selectedDir != null) {
            pathInfo.setText(selectedDir.getName());
            destination = selectedDir;
            if (toCrypt!=null) {
                doAction.setDisable(false);
            }
        }
    }

    /**
     * TODO encrypting or decrypting file then view
     */
    public void coding() {
        RadioButton selectedRadioButton = (RadioButton) cipherModesGroup.getSelectedToggle();
        String choice = selectedRadioButton.getText();
        if(choice.equals("ECB"))
        {
            aes = new AES(new ECB(),"");
            aes.SetPath(destination.getAbsolutePath());
        }
//        else if(choice.equals("CBC"))
//        {
//            showInitializationVector = true;
//            aes = new AES(new CBC(), "./path");
//        }
//        else if(choice.equals("CTR"))
//        {
//            showInitializationVector = true;
//            aes = new AES(new CTR(), "./path");
//        }
        String fileDir = destination + "/encrypted" + MultiWindowFunctions.getFileExtension(toCrypt);
        System.out.println(fileDir);
        aes.encrypt(toCrypt);
        showFile(new File(fileDir));
    }

    private void showFile(File f) {
        String extension = MultiWindowFunctions.getFileExtension(f);
        if (MultiWindowFunctions.checkIfText(extension)) {

            try {
                FileReader fileReader = new FileReader(f);
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
                Image imageToView = new Image(f.toURI().toURL().toExternalForm());
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

}
