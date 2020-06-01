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
import pg.eti.inf.wdc.project.aes.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

public class EducationalModeController
{
    //buttons
    public Button confirmModeChoice;
    public Button confirmEncryption;
    public Button confirmDecryption;
    public Button back;
    public Button changeInitializationVectorEncryption;
    public Button changeInitializationVectorDecryption;

    //radio buttons
    public RadioButton ecbMode;
    public RadioButton cbcMode;
    public RadioButton ctrMode;
    public ToggleGroup cipherModesGroup;

    //text fields
    public TextArea plainText;
    public TextFlow encryptedText;
    public TextFlow initializationVectorTextEncryption;
    public TextFlow initializationVectorTextDecryption;

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

    private AbstractCipherMode aes;
    private boolean secondEncryption;
    private boolean showInitializationVector;

    private byte[] encryptedFirstBytes, encryptedSecondBytes;
    private byte[] keyBytes;
    private byte[] firstPlainTextBytes, secondPlainTextBytes;
    private byte[] firstInitializationVectorBytes, secondInitializationVectorBytes;

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
            aes = new ECB(false);
        }
        else if(choice.equals("CBC"))
        {
            showInitializationVector = true;
            aes = new CBC(false);
        }
        else if(choice.equals("CTR"))
        {
            showInitializationVector = true;
            aes = new CTR(false);
        }
        disableChoosingMode();
        showEncryption();
    }

    @FXML
    protected void confirmEncryptionAction(ActionEvent event)
    {
        String text;
        if(!secondEncryption)
        {
            text = normalizeText(plainText.getText());
            firstPlainTextBytes = text.getBytes();

            byte [][] data = aes.encrypt(firstPlainTextBytes);
            keyBytes = data[0];
            if(showInitializationVector)
            {
                byte[] wholeMessage = data[1];
                firstInitializationVectorBytes = Arrays.copyOfRange(wholeMessage, 0, 16);
                encryptedFirstBytes = Arrays.copyOfRange(wholeMessage, 16, wholeMessage.length);
                secondInitializationVectorBytes = firstInitializationVectorBytes;
                changeInitializationVectorEncryption.setVisible(true);
                makeTextColorful(initializationVectorTextEncryption, firstInitializationVectorBytes, secondInitializationVectorBytes);
            }
            else encryptedFirstBytes = data[1];

            encryptedSecondBytes = encryptedFirstBytes;
            secondEncryption = true;
            makeTextColorful(encryptedFlow, encryptedFirstBytes, encryptedSecondBytes);
        }
        else
        {
            text = normalizeText(plainText.getText());
            byte[] wholeMessage;
            if(showInitializationVector)
            {
                wholeMessage = aes.encrypt(text.getBytes(), keyBytes, secondInitializationVectorBytes);
                makeTextColorful(initializationVectorTextEncryption, firstInitializationVectorBytes, secondInitializationVectorBytes);
            }
            else
                wholeMessage = aes.encrypt(text.getBytes(), keyBytes, null);
            secondInitializationVectorBytes = Arrays.copyOfRange(wholeMessage, 0, 16);
            encryptedSecondBytes = Arrays.copyOfRange(wholeMessage, 16, wholeMessage.length);
            makeTextColorful(encryptedFlow, encryptedFirstBytes, encryptedSecondBytes);
            disableEncryption();
            showDecryption();
        }
    }

    @FXML
    protected void confirmDecryptionAction(ActionEvent event) {
        encryptedSecondBytes = changeRandomChar(encryptedFirstBytes);
        makeTextColorful(encryptedText, encryptedFirstBytes, encryptedSecondBytes);

        if(showInitializationVector)
        {
            byte[] wholeMessage = concat(firstInitializationVectorBytes, encryptedSecondBytes);
            secondPlainTextBytes = aes.decrypt(wholeMessage, keyBytes);
        }
        else secondPlainTextBytes = aes.decrypt(encryptedSecondBytes, keyBytes);
        makeTextColorful(decryptedFlow, firstPlainTextBytes, secondPlainTextBytes);
        disableDecryption();
    }

    public static byte[] concat(byte[] a, byte[] b)
    {
        int lenA = a.length;
        int lenB = b.length;
        byte[] c = Arrays.copyOf(a, lenA + lenB);
        System.arraycopy(b, 0, c, lenA, lenB);
        return c;
    }

    @FXML
    protected void changeInitializationVectorEncryptionAction(ActionEvent event)
    {
        secondInitializationVectorBytes = changeRandomChar(firstInitializationVectorBytes);
        makeTextColorful(initializationVectorTextEncryption, firstInitializationVectorBytes, secondInitializationVectorBytes);
    }

    @FXML
    protected void changeInitializationVectorDecryptionAction(ActionEvent event)
    {
        secondInitializationVectorBytes = changeRandomChar(firstInitializationVectorBytes);
        makeTextColorful(initializationVectorTextDecryption, firstInitializationVectorBytes, secondInitializationVectorBytes);
    }

    private void makeTextColorful(TextFlow flow, byte[] first, byte[] second)
    {
        flow.getChildren().clear();
        for(int i=0;i<first.length;i++)
        {
            byte firstChar = first[i];
            byte secondChar = second[i];
            Text text = new Text();
            if(firstChar != secondChar)
            {
                text.setText(String.valueOf((char)(secondChar)));
                text.setFill(Color.RED);
            }
            else
            {
                text.setText(String.valueOf((char)(secondChar)));
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
        changeInitializationVectorEncryption.setVisible(false);
        changeInitializationVectorDecryption.setVisible(false);

        plainText.setVisible(false);
        encryptedText.setVisible(false);
        initializationVectorTextEncryption.setVisible(false);
        initializationVectorTextDecryption.setVisible(false);

        encryptedFlow.setVisible(false);
        decryptedFlow.setVisible(false);

        lineBetweenChoosingAndEncryption.setVisible(false);
        lineBetweenEncryptionAndDecryption.setVisible(false);

        encryptionTitleLabel.setVisible(false);
        decryptionTitleLabel.setVisible(false);
    }

    private void showEncryption()
    {
        encryptionTitleLabel.setVisible(true);
        lineBetweenChoosingAndEncryption.setVisible(true);
        encryptedFlow.setVisible(true);
        confirmEncryption.setVisible(true);
        plainText.setVisible(true);
        if(showInitializationVector)initializationVectorTextEncryption.setVisible(true);
    }

    private void showDecryption()
    {
        decryptionTitleLabel.setVisible(true);
        lineBetweenEncryptionAndDecryption.setVisible(true);
        decryptedFlow.setVisible(true);
        confirmDecryption.setVisible(true);
        encryptedText.setVisible(true);

        encryptedSecondBytes = encryptedFirstBytes;
        makeTextColorful(encryptedText, encryptedFirstBytes, encryptedSecondBytes);

        if(showInitializationVector)
        {
            initializationVectorTextDecryption.setVisible(true);
            changeInitializationVectorDecryption.setVisible(true);
            secondInitializationVectorBytes = firstInitializationVectorBytes;
            makeTextColorful(initializationVectorTextDecryption, firstInitializationVectorBytes, secondInitializationVectorBytes);
            byte[] wholeMessage = concat(firstInitializationVectorBytes, encryptedFirstBytes);
            firstPlainTextBytes = aes.decrypt(wholeMessage, keyBytes);
        }
        else firstPlainTextBytes = aes.decrypt(encryptedFirstBytes, keyBytes);
        secondPlainTextBytes = firstPlainTextBytes;
        makeTextColorful(decryptedFlow, firstPlainTextBytes, secondPlainTextBytes);
    }

    private byte[] changeRandomChar(byte[] array)
    {
        byte[] help = array.clone();
        Random random = new Random();
        int randomIndex = random.nextInt(help.length);
        byte[] randombyte = new byte[1];
        random.nextBytes(randombyte);
        help[randomIndex] = randombyte[0];
        return help;
    }

    private void disableChoosingMode()
    {
        confirmModeChoice.setDisable(true);
    }

    private void disableEncryption()
    {
        plainText.setDisable(true);
        changeInitializationVectorEncryption.setDisable(true);
        confirmEncryption.setDisable(true);
    }

    private void disableDecryption()
    {
        changeInitializationVectorDecryption.setDisable(true);
        confirmDecryption.setDisable(true);
    }

    private String normalizeText(String text)
    {
        if(text.length()%16!=0)
        {
            while (text.length() % 16 != 0)
                text = text.concat(" ");
        }
        return text;
    }

}
