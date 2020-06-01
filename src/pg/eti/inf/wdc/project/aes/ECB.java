package pg.eti.inf.wdc.project.aes;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;

public class ECB implements AbstractCipherMode
{

    String padding_;

    public ECB()
    {
        this.padding_ = "AES/ECB/PKCS5Padding";
    }

    public ECB(boolean padding)
    {
        if(padding)
            this.padding_ = "AES/ECB/PKCS5Padding";
        else
            this.padding_ = "AES/ECB/NoPadding";
    }

    @Override
    public byte[][] encrypt(byte[] data)
    {
        byte data_array[][] = new byte[2][];
        try
        {
            KeyGenerator key_generator = KeyGenerator.getInstance("AES");
            SecretKey secret_key = key_generator.generateKey();

            Cipher cipher = Cipher.getInstance(padding_);
            cipher.init(Cipher.ENCRYPT_MODE, secret_key);

            byte[] encoded = cipher.doFinal(data);
            data_array[0] = secret_key.getEncoded();
            data_array[1] = encoded;
        } catch(Exception ex){System.out.println(ex);}

        return data_array;
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] key, byte[] vector)
    {
        byte encrypted[] = null;
        try
        {
            //vector is not used in this mode it's here becasue of some magic somewhere else
            byte[] iv = new byte[128/8];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            if(key.length != 16 && key.length != 24 && key.length != 32)
                key = Arrays.copyOf(key, 32);
            SecretKey secret_key = new SecretKeySpec(key,"AES");

            Cipher cipher = Cipher.getInstance(padding_);
            cipher.init(Cipher.ENCRYPT_MODE, secret_key);

            byte[] encoded = cipher.doFinal(data);
            encrypted = new byte[iv.length + encoded.length];
            System.arraycopy(iv, 0, encrypted, 0, iv.length);
            System.arraycopy(encoded, 0, encrypted, iv.length, encoded.length);
        } catch(Exception ex){System.out.println(ex);}

        return encrypted;
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key)
    {
        byte decrypted[] = null;
        try
        {
            if(key.length != 16 && key.length != 24 && key.length != 32)
                key = Arrays.copyOf(key, 32);
            SecretKey secret_key = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance(padding_);
            cipher.init(Cipher.DECRYPT_MODE, secret_key);

            byte[] encoded = cipher.doFinal(data);
            decrypted = encoded;
        } catch(Exception ex){System.out.println(ex);}

        return decrypted;
    }
}
