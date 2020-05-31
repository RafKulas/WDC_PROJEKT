package pg.eti.inf.wdc.project.aes;

import pg.eti.inf.wdc.project.aes.AbstractCipherMode;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

public class ECB implements AbstractCipherMode
{

    public void ECB()
    {

    }

    @Override
    public byte[][] encrypt(byte[] data)
    {
        byte[][] data_array = new byte[2][];
        try
        {
            KeyGenerator key_generator = KeyGenerator.getInstance("pg.eti.inf.wdc.project.aes.AES");
            SecretKey secret_key = key_generator.generateKey();

            Cipher cipher = Cipher.getInstance("pg.eti.inf.wdc.project.aes.AES/pg.eti.inf.wdc.project.aes.ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret_key);

            byte[] encoded = cipher.doFinal(data);
            data_array[0] = secret_key.getEncoded();
            data_array[1] = encoded;
        } catch(Exception ex){System.out.println(ex);}

        return data_array;
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] key)
    {
        byte[] encrypted = null;
        try
        {
            if(key.length != 16 && key.length != 24 && key.length != 32)
                key = Arrays.copyOf(key, 32);
            SecretKey secret_key = new SecretKeySpec(key,"pg.eti.inf.wdc.project.aes.AES");

            Cipher cipher = Cipher.getInstance("pg.eti.inf.wdc.project.aes.AES/pg.eti.inf.wdc.project.aes.ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret_key);

            byte[] encoded = cipher.doFinal(data);
            encrypted = encoded;
        } catch(Exception ex){System.out.println(ex);}

        return encrypted;
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key)
    {
        byte[] decrypted = null;
        try
        {
            if(key.length != 16 && key.length != 24 && key.length != 32)
                key = Arrays.copyOf(key, 32);
            SecretKey secret_key = new SecretKeySpec(key, "pg.eti.inf.wdc.project.aes.AES");
            Cipher cipher = Cipher.getInstance("pg.eti.inf.wdc.project.aes.AES/pg.eti.inf.wdc.project.aes.ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret_key);

            byte[] encoded = cipher.doFinal(data);
            decrypted = encoded;
        } catch(Exception ex){System.out.println(ex);}

        return decrypted;
    }
}
