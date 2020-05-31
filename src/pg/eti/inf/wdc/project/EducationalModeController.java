package pg.eti.inf.wdc.project;

import static java.nio.charset.StandardCharsets.*;
import static pg.eti.inf.wdc.project.MultiWindowFunctions.createNewWindow;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Pair;
import pg.eti.inf.wdc.project.aes.AES;
import pg.eti.inf.wdc.project.aes.ECB;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EducationalModeController
{
    //buttons
    public Button confirmModeChoice;
    public Button confirmEncryption;
    public Button confirmDecryption;
    public Button back;

    //radio buttons
    public RadioButton ecbMode;
    public RadioButton cbcMode;
    public RadioButton ctrMode;
    public RadioButton cfbMode;
    public RadioButton ofbMode;
    public ToggleGroup cipherModesGroup;

    //text fields
    public TextArea plainText;
    public TextArea encryptedText;
    public TextArea initializationVectorText;

    //colorful labels
    public TextFlow encryptedFlow;
    public TextFlow decryptedFlow;

    //normal labels
    public Label encryptionTitleLabel;
    public Label chooseModeTitleLabel;
    public Label decryptionTitleLabel;

    //lines
    public Line lineBetweenChoosingAndEncryption;
    public Line lineBetweenEncryptionAndDecryption;

    private AES aes;
    private String encryptedFirstMessage, encryptedSecondMessage;
    private String key;
    private String decryptedMessage;
    private boolean secondEncryption;
    private boolean secondDecryption;
    private String firstPlainText, secondPlainText;

    public void initialize()
    {
        hideForChoosingMode();
        ecbMode.setSelected(true);
        secondEncryption = false;
        secondDecryption = false;
    }

    @FXML
    protected void backToMenu(ActionEvent event)
    {
        createNewWindow(this, event, "sample.fxml", new Pair<>(300, 275));
    }

    @FXML
    protected void confirmModeChoiceAction(ActionEvent event)
    {
        RadioButton selectedRadioButton = (RadioButton) cipherModesGroup.getSelectedToggle();
        String choice = selectedRadioButton.getText();
        if(choice.equals("ECB"))
            showEncrytpion(false);
            aes = new AES(new ECB(),"./path");
    }

    @FXML
    protected void confirmEncryptionAction(ActionEvent event)
    {
        if(!secondEncryption)
        {
            firstPlainText = plainText.getText();
            if(firstPlainText.length()%16!=0)
            {
                while (firstPlainText.length() % 16 != 0)
                    firstPlainText = firstPlainText.concat(" ");
            }
            try
            {
                aes.encrypt(firstPlainText);
                encryptedFirstMessage = readFile("./path/encrypted.txt", ISO_8859_1);
                key = readFile("./path/key.txt", ISO_8859_1);
                encryptedSecondMessage = encryptedFirstMessage;
                secondEncryption = true;
                makeTextColorful(encryptedFlow, encryptedFirstMessage, encryptedSecondMessage);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            String text = plainText.getText();
            if(text.length()%16!=0)
            {
                while (text.length() % 16 != 0)
                    text = text.concat(" ");
            }
            try
            {
                aes.encrypt(text, key);
                encryptedSecondMessage = readFile("./path/encrypted.txt", ISO_8859_1);
                makeTextColorful(encryptedFlow, encryptedFirstMessage, encryptedSecondMessage);
                showDecryption();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void confirmDecryptionAction(ActionEvent event)
    {
        byte[] bytes = encryptedText.getText().getBytes(ISO_8859_1);
        encryptedSecondMessage = new String(bytes, ISO_8859_1);
        aes.decrypt(encryptedSecondMessage, key);
        try
        {
            secondPlainText = readFile("./path/decrypted.txt", ISO_8859_1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        makeTextColorful(decryptedFlow, firstPlainText, secondPlainText);
    }

    private void makeTextColorful(TextFlow flow, String first, String second)
    {
        flow.getChildren().clear();
        for(int i=0;i<first.length();i++)
        {
            char firstChar = first.charAt(i);
            char secondChar = second.charAt(i);
            Text text = new Text();
            if(firstChar != secondChar)
            {
                text.setText(String.valueOf(secondChar));
                text.setFill(Color.RED);
            }
            else
            {
                text.setText(String.valueOf(secondChar));
                text.setFill(Color.GREEN);
            }
            flow.getChildren().add(text);
        }
    }

    private static String readFile(String path, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private void hideForChoosingMode()
    {
        confirmEncryption.setVisible(false);
        confirmDecryption.setVisible(false);

        plainText.setVisible(false);
        encryptedText.setVisible(false);
        initializationVectorText.setVisible(false);

        encryptedFlow.setVisible(false);
        decryptedFlow.setVisible(false);

        lineBetweenChoosingAndEncryption.setVisible(false);
        lineBetweenEncryptionAndDecryption.setVisible(false);

        encryptionTitleLabel.setVisible(false);
        decryptionTitleLabel.setVisible(false);
    }

    private void showEncrytpion(boolean showInitializationVector)
    {
        encryptionTitleLabel.setVisible(true);
        lineBetweenChoosingAndEncryption.setVisible(true);
        encryptedFlow.setVisible(true);
        confirmEncryption.setVisible(true);
        plainText.setVisible(true);
        if(showInitializationVector)initializationVectorText.setVisible(true);
    }

    private void showDecryption()
    {
        decryptionTitleLabel.setVisible(true);
        lineBetweenEncryptionAndDecryption.setVisible(true);
        decryptedFlow.setVisible(true);
        confirmDecryption.setVisible(true);
        encryptedText.setVisible(true);

//        TextFormatter<String> formatter = new TextFormatter<String>( change -> {
//            return change;
//        });
//        encryptedText.setTextFormatter(formatter);


        encryptedText.setText(encryptedFirstMessage);
        aes.decrypt(encryptedFirstMessage, key);
        try
        {
            secondPlainText = readFile("./path/decrypted.txt", ISO_8859_1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        makeTextColorful(decryptedFlow, firstPlainText, secondPlainText);

    }
}
