package pg.eti.inf.wdc.project.aes;

import pg.eti.inf.wdc.project.MultiWindowFunctions;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;

public class OFB implements AbstractCipherMode
{

    String padding_;

    public OFB()
    {
        this.padding_ = "AES/OFB/PKCS5Padding";
    }

    public OFB(boolean padding)
    {
        if(padding)
            this.padding_ = "AES/OFB/PKCS5Padding";
        else
            this.padding_ = "AES/OFB/NoPadding";
    }

    @Override
    public byte[][] encrypt(byte[] data)
    {
        byte[][] data_array = new byte[2][];
        try
        {
            byte[] iv = new byte[128/8];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            KeyGenerator key_generator = KeyGenerator.getInstance("AES");
            SecretKey secret_key = key_generator.generateKey();

            Cipher cipher = Cipher.getInstance(padding_);
            cipher.init(Cipher.ENCRYPT_MODE, secret_key, ivspec);

            byte[] encoded = cipher.doFinal(data);
            data_array[0] = secret_key.getEncoded();
            data_array[1] = new byte[iv.length + encoded.length];
            System.arraycopy(iv, 0, data_array[1], 0, iv.length);
            System.arraycopy(encoded, 0, data_array[1], iv.length, encoded.length);
        } catch(Exception ex) {
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }

        return data_array;
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] key, byte[] vector)
    {
        byte[] encrypted = null;
        try
        {
            byte[] iv = new byte[128/8];
            if(vector == null)
            {
                SecureRandom random = new SecureRandom();
                random.nextBytes(iv);
            }
            else
            {
                iv = vector;
            }
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            if(key.length != 16 && key.length != 24 && key.length != 32)
                key = Arrays.copyOf(key, 32);
            SecretKey secret_key = new SecretKeySpec(key,"AES");

            Cipher cipher = Cipher.getInstance(padding_);
            cipher.init(Cipher.ENCRYPT_MODE, secret_key, ivspec);

            byte[] encoded = cipher.doFinal(data);
            encrypted = new byte[iv.length + encoded.length];
            System.arraycopy(iv, 0, encrypted, 0, iv.length);
            System.arraycopy(encoded, 0, encrypted, iv.length, encoded.length);
        } catch(Exception ex) {
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }

        return encrypted;
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key)
    {
        byte[] decrypted = null;
        try
        {
            byte[] iv = Arrays.copyOfRange(data, 0,16);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            if(key.length != 16 && key.length != 24 && key.length != 32)
                key = Arrays.copyOf(key, 32);
            SecretKey secret_key = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance(padding_);
            cipher.init(Cipher.DECRYPT_MODE, secret_key, ivspec);

            byte[] encoded = cipher.doFinal(Arrays.copyOfRange(data, 16,data.length));
            decrypted = encoded;
        } catch(Exception ex) {
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }

        return decrypted;
    }
}
