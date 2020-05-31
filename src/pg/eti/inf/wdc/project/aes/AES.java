package pg.eti.inf.wdc.project.aes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AES implements IAES
{
    private AbstractCipherMode cipher_mode_;
    private String path_;

    public AES(AbstractCipherMode cipher_mode, String path)
    {
        this.cipher_mode_ = cipher_mode;
        this.path_ = path;
    }

    public void SetPath(String path)
    {
        this.path_ = path;
    }

    public void encrypt(String data)
    {
        try
        {
            byte data_array[][] = cipher_mode_.encrypt(data.getBytes("ISO-8859-1"));

            File file = new File(path_ + "\\key.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ + "\\key.txt");
            out.write(data_array[0]);

            file = new File(path_ + "\\encrypted.txt");
            file.createNewFile();
            out = new FileOutputStream(path_ + "\\encrypted.txt");
            out.write(data_array[1]);
        } catch(Exception ex){System.out.println(ex);}
    }

    public void encrypt(String data, String key)
    {
        try
        {
            byte encrypted[] = cipher_mode_.encrypt(data.getBytes("ISO-8859-1"), key.getBytes("ISO-8859-1"));

            File file = new File(path_ + "\\encrypted.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ + "\\encrypted.txt");
            out.write(encrypted);
        } catch(Exception ex){System.out.println(ex);}
    }

    public void encrypt(String data, File file_key)
    {
        try
        {
            byte encrypted[] = cipher_mode_.encrypt(data.getBytes(), new FileInputStream(file_key).readAllBytes());

            File file = new File(path_ + "\\encrypted.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ + "\\encrypted.txt");
            out.write(encrypted);
        } catch(Exception ex){System.out.println(ex);}
    }

    public void encrypt(File file_data)
    {
        try
        {
            byte data_array[][] = cipher_mode_.encrypt( new FileInputStream(file_data).readAllBytes());

            File file = new File(path_ + "\\key.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ + "\\key.txt");
            out.write(data_array[0]);

            file = new File(path_ + "\\encrypted.txt");
            file.createNewFile();
            out = new FileOutputStream(path_ + "\\encrypted.txt");
            out.write(data_array[1]);
        } catch(Exception ex){System.out.println(ex);}
    }

    public void encrypt(File file_data, String key)
    {
        try
        {
            byte encrypted[] = cipher_mode_.encrypt( new FileInputStream(file_data).readAllBytes(), key.getBytes());

            File file = new File(path_ + "\\encrypted.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ + "\\encrypted.txt");
            out.write(encrypted);
        } catch(Exception ex){System.out.println(ex);}
    }

    public void encrypt(File file_data, File file_key)
    {
        try
        {
            byte encrypted[] = cipher_mode_.encrypt( new FileInputStream(file_data).readAllBytes(),  new FileInputStream(file_key).readAllBytes());

            File file = new File(path_ + "\\encrypted.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ + "\\encrypted.txt");
            out.write(encrypted);
        } catch(Exception ex){System.out.println(ex);}
    }

    public void decrypt(String data, String key)
    {
        try
        {
            byte decrypted[] = cipher_mode_.decrypt(data.getBytes("ISO-8859-1"),key.getBytes("ISO-8859-1"));

            File file = new File(path_ + "\\decrypted.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ + "\\decrypted.txt");
            out.write(decrypted);
        } catch(Exception ex){System.out.println(ex);}
    }

    public void decrypt(String data, File file_key)
    {
        try
        {
            byte decrypted[] = cipher_mode_.decrypt(data.getBytes("ISO-8859-1"), new FileInputStream(file_key).readAllBytes());

            File file = new File(path_ + "\\decrypted.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ + "\\decrypted.txt");
            out.write(decrypted);
        } catch(Exception ex){System.out.println(ex);}
    }

    public void decrypt(File file_data, String key)
    {
        try
        {
            byte decrypted[] = cipher_mode_.decrypt( new FileInputStream(file_data).readAllBytes(), key.getBytes("ISO-8859-1"));

            File file = new File(path_ + "\\decrypted.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ + "\\decrypted.txt");
            out.write(decrypted);
        } catch(Exception ex){System.out.println(ex);}
    }

    public void decrypt(File file_data, File file_key)
    {
        try
        {
            byte decrypted[] = cipher_mode_.decrypt( new FileInputStream(file_data).readAllBytes(), new FileInputStream(file_key).readAllBytes());

            File file = new File(path_ + "\\decrypted.txt");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(path_ + "\\decrypted.txt");
            out.write(decrypted);
        } catch(Exception ex){System.out.println(ex);}
    }
}
