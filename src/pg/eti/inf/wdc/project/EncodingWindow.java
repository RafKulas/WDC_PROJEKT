package pg.eti.inf.wdc.project;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import pg.eti.inf.wdc.project.aes.*;
import java.io.*;

public class EncodingWindow {
    // Buttons
    public Button fileChooser;
    public Button doAction;
    public Button pathChooser;

    public ToggleGroup cipherModesGroup;

    //RadioButtons
    public RadioButton ctrMode;
    public RadioButton cbcMode;
    public RadioButton ecbMode;
    public RadioButton ofbMode;
    public RadioButton cfbMode;

    //Labels
    public Label fileInfo;
    public Label pathInfo;
    public Label chooseModeTitleLabel;

    //File view
    public TextArea text;

    //File
    private File toCrypt = null; // file to encrypt or decrypt
    private File destination = null;

    //AES
    private AES aes;

    public ChoiceBox<String> todo;

    public void swap(ActionEvent actionEvent) {
        MultiWindowFunctions.createNewWindow(this, actionEvent, "sample.fxml", new Pair<>(300, 275));
    }

    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
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

    public void coding() {
        RadioButton selectedRadioButton = (RadioButton) cipherModesGroup.getSelectedToggle();
        String choice = selectedRadioButton.getText();
        switch (choice) {
            case "ECB":
                aes = new AES(new ECB(), "");
                break;
            case "CBC":
                aes = new AES(new CBC(), "");
                break;
            case "CTR":
                aes = new AES(new CTR(), "");
                break;
            case "OFB":
                aes = new AES(new OFB(), "");
                break;
            case "CFB":
                aes = new AES(new CFB(), "");
                break;
        }
        aes.SetPath(destination.getAbsolutePath());
        String fileDir;
        if (todo.getValue().equals("Szyfrowanie")) {
            fileDir = destination + aes.slash + "encrypted" + MultiWindowFunctions.getFileExtension(toCrypt);
            aes.encrypt(toCrypt);
        }
        else {
            fileDir = destination + aes.slash + "decrypted" + MultiWindowFunctions.getFileExtension(toCrypt);

            FileChooser fc = new FileChooser();
            fc.setTitle("Wybierz klucz do odszyfrowania");
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Klucze (*.txt)", "*.txt"));
            File selectedFile = fc.showOpenDialog(null);

            File key;
            if (selectedFile != null) {
                key = selectedFile;
            }
            else {
                MultiWindowFunctions.showAlert("KEY NOT SELECTED!", "");
                return;
            }
            aes.decrypt(toCrypt, key);
        }
        showFile(new File(fileDir));

    }

    private void showFile(File f) {
        String extension = MultiWindowFunctions.getFileExtension(f);
        if (MultiWindowFunctions.checkIfText(extension)) {
            try (FileReader fileReader = new FileReader(f);) {
                text.setText("");
                char[] buffer = new char[80];
                int read;
                while((read = fileReader.read(buffer))>0) {
                    text.appendText(String.valueOf(buffer).substring(0, read));
                }
                text.setMinHeight(200);
                text.setMaxHeight(500);
                text.setVisible(true);
            } catch (FileNotFoundException e) {
                MultiWindowFunctions.showAlert("FILE NOT FOUND!", "Looks like file does not exist or was deleted...");
            } catch (IOException e) {
                MultiWindowFunctions.showAlert("Problem with reading file!", "File is corrupted...");
            }
        }
        else {
            text.setMaxHeight(0);
            text.setVisible(false);
        }
    }

}
