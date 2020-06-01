package pg.eti.inf.wdc.project.aes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import pg.eti.inf.wdc.project.MultiWindowFunctions;

public class AES implements IAES {
    private final AbstractCipherMode cipher_mode_;
    private String path_;
    public final String slash;

    public AES(AbstractCipherMode cipher_mode, String path) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            slash = "\\";
        }
        else {
            slash = "/";
        }
        this.cipher_mode_ = cipher_mode;
        this.path_ = path;
    }

    public void SetPath(String path) {
        this.path_ = path;
    }

    public void encrypt(String data) {
        try {
            byte[][] data_array = cipher_mode_.encrypt(data.getBytes(StandardCharsets.ISO_8859_1));

            File file = new File(path_ + slash + "key.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ + slash + "key.txt");
            out.write(data_array[0]);

            file = new File(path_ +  slash + "encrypted.txt");
            file.createNewFile();
            out = new FileOutputStream(path_ +  slash + "encrypted.txt");
            out.write(data_array[1]);
        } catch(Exception ex) {
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }
    }

    public void encrypt(String data, String key) {
        try {
            byte[] encrypted = cipher_mode_.encrypt(data.getBytes(StandardCharsets.ISO_8859_1), key.getBytes(StandardCharsets.ISO_8859_1), null);

            File file = new File(path_ +  slash + "encrypted.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ +  slash + "encrypted.txt");
            out.write(encrypted);
        } catch(Exception ex){
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }
    }

    public void encrypt(String data, File file_key) {
        try {
            byte[] encrypted = cipher_mode_.encrypt(data.getBytes(), new FileInputStream(file_key).readAllBytes(),null);

            File file = new File(path_ +  slash + "encrypted.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ +  slash + "encrypted.txt");
            out.write(encrypted);
        } catch(Exception ex){
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }
    }

    public void encrypt(File file_data) {
        try {
            byte[][] data_array = cipher_mode_.encrypt( new FileInputStream(file_data).readAllBytes());

            File file = new File(path_ +  slash + "key.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ +  slash + "key.txt");
            out.write(data_array[0]);

            String ext = file_data.getName();
            ext = ext.substring(ext.lastIndexOf('.'));
            file = new File(path_ +  slash + "encrypted" + ext);
            file.createNewFile();
            out = new FileOutputStream(path_ +  slash + "encrypted" + ext);
            out.write(data_array[1]);
        } catch(Exception ex){
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }
    }

    public void encrypt(File file_data, String key) {
        try {
            byte[] encrypted = cipher_mode_.encrypt( new FileInputStream(file_data).readAllBytes(), key.getBytes(),null);

            encryptFile(file_data, encrypted);
        } catch(Exception ex){
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }
    }

    public void encrypt(File file_data, File file_key) {
        try {
            byte[] encrypted = cipher_mode_.encrypt( new FileInputStream(file_data).readAllBytes(),  new FileInputStream(file_key).readAllBytes(),null);

            encryptFile(file_data, encrypted);
        } catch(Exception ex){
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }
    }



    public void decrypt(String data, String key) {
        try {
            byte[] decrypted = cipher_mode_.decrypt(data.getBytes(StandardCharsets.ISO_8859_1),key.getBytes(StandardCharsets.ISO_8859_1));

            File file = new File(path_ +  slash + "decrypted.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ +  slash + "decrypted.txt");
            out.write(decrypted);
        } catch(Exception ex){
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }
    }

    public void decrypt(String data, File file_key) {
        try {
            byte[] decrypted = cipher_mode_.decrypt(data.getBytes(StandardCharsets.ISO_8859_1), new FileInputStream(file_key).readAllBytes());

            File file = new File(path_ +  slash + "decrypted.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ +  slash + "decrypted.txt");
            out.write(decrypted);
        } catch(Exception ex){
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }
    }

    public void decrypt(File file_data, String key) {
        try {
            byte[] decrypted = cipher_mode_.decrypt( new FileInputStream(file_data).readAllBytes(), key.getBytes(StandardCharsets.ISO_8859_1));
            decryptFile(file_data, decrypted);
        } catch(Exception ex){
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }
    }

    public void decrypt(File file_data, File file_key) {
        try {
            byte[] decrypted = cipher_mode_.decrypt( new FileInputStream(file_data).readAllBytes(), new FileInputStream(file_key).readAllBytes());
            decryptFile(file_data, decrypted);
        } catch(Exception ex){
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }
    }

    private void decryptFile(File file_data, byte[] decrypted) throws IOException {
        String ext = file_data.getName();
        ext = ext.substring(ext.lastIndexOf('.'));
        File file = new File(path_ +  slash + "decrypted" + ext);
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(path_ +  slash + "decrypted" + ext);
        out.write(decrypted);
    }

    private void encryptFile(File file_data, byte[] encrypted) throws IOException {
        String ext = file_data.getName();
        ext = ext.substring(ext.lastIndexOf('.'));
        File file = new File(path_ +  slash + "encrypted" + ext);
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(path_ +  slash + "encrypted" + ext);
        out.write(encrypted);
    }
}
