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
import pg.eti.inf.wdc.project.aes.CBC;
import pg.eti.inf.wdc.project.aes.CTR;
import pg.eti.inf.wdc.project.aes.ECB;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class EducationalModeController
{
    //buttons
    public Button confirmModeChoice;
    public Button confirmEncryption;
    public Button confirmDecryption;
    public Button back;
    public Button changeInitializationVector;

    //radio buttons
    public RadioButton ecbMode;
    public RadioButton cbcMode;
    public RadioButton ctrMode;
    public RadioButton cfbMode;
    public RadioButton ofbMode;
    public ToggleGroup cipherModesGroup;

    //text fields
    public TextArea plainText;
    public TextFlow encryptedText;
    public TextFlow initializationVectorText;

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
    private String firstPlainText, secondPlainText;
    private boolean showInitializationVector;
    private String firstInitializationVector, secondInitializationVector;

    public void initialize()
    {
        hideForChoosingMode();
        ecbMode.setSelected(true);
        secondEncryption = false;
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
        {
            showInitializationVector = false;
            aes = new AES(new ECB(),"./path");
        }
        else if(choice.equals("CBC"))
        {
            showInitializationVector = true;
            aes = new AES(new CBC(), "./path");
        }
        else if(choice.equals("CTR"))
        {
            showInitializationVector = true;
            aes = new AES(new CTR(), "./path");
        }
        showEncrytpion();
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
                if(!showInitializationVector)
                {
                    encryptedFirstMessage = readFile("./path/encrypted.txt", ISO_8859_1);
                }
                else
                {
                    String help =  readFile("./path/encrypted.txt", ISO_8859_1);
                    firstInitializationVector = help.substring(0,16);
                    secondInitializationVector = firstInitializationVector;
                    encryptedFirstMessage = help.substring(16, help.length());
                    makeTextColorful(initializationVectorText, firstInitializationVector, secondInitializationVector);
                    changeInitializationVector.setVisible(true);
                }
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
                if(!showInitializationVector) encryptedSecondMessage = readFile("./path/encrypted.txt", ISO_8859_1);
                else
                {
                    String help =  readFile("./path/encrypted.txt", ISO_8859_1);
                    secondInitializationVector = help.substring(0,16);
                    encryptedSecondMessage = help.substring(16, help.length());
                }
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
    protected void confirmDecryptionAction(ActionEvent event) {
        encryptedSecondMessage = changeRandomChar(encryptedFirstMessage);
        makeTextColorful(encryptedText, encryptedFirstMessage, encryptedSecondMessage);
        if(!showInitializationVector) aes.decrypt(encryptedSecondMessage, key);
        else aes.decrypt(firstInitializationVector.concat(encryptedSecondMessage), key);
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

    @FXML
    protected void changeInitializationVectorAction(ActionEvent event)
    {
        secondInitializationVector = changeRandomChar(firstInitializationVector);
        makeTextColorful(initializationVectorText, firstInitializationVector, secondInitializationVector);
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
        changeInitializationVector.setVisible(false);

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

    private void showEncrytpion()
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

        encryptedSecondMessage = encryptedFirstMessage;
        makeTextColorful(encryptedText, encryptedFirstMessage, encryptedSecondMessage);
        if(!showInitializationVector) aes.decrypt(encryptedFirstMessage, key);
        else aes.decrypt(firstInitializationVector.concat(encryptedFirstMessage), key);
        try
        {
            firstPlainText = readFile("./path/decrypted.txt", ISO_8859_1);
            secondPlainText = firstPlainText;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        makeTextColorful(decryptedFlow, firstPlainText, secondPlainText);
    }

    private String changeRandomChar(String text)
    {
        String help = text;
        Random random = new Random();
        int randomIndex = random.nextInt(text.length());
        char[] helpChars = help.toCharArray();
        int randomAdd = random.nextInt(10)+1;
        helpChars[randomIndex] += randomAdd;
        help = String.valueOf(helpChars);
        return help;
    }

}
