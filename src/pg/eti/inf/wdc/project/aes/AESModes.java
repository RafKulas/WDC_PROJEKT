package pg.eti.inf.wdc.project.aes;

import pg.eti.inf.wdc.project.MultiWindowFunctions;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public abstract class AESModes implements AbstractCipherMode {
    String padding_;

    protected byte[][] encryptWOKey(byte[] data) {
        byte[][] data_array = new byte[2][];
        try {
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

    protected byte[] encryptWKey(byte[] data, byte[] key, byte[] vector) {
        byte[] encrypted = null;
        try {
            byte[] iv = new byte[128/8];
            if(vector == null) {
                SecureRandom random = new SecureRandom();
                random.nextBytes(iv);
            }
            else {
                iv = vector;
            }
            Cipher cipher = getCipher(iv, key, Cipher.ENCRYPT_MODE);
            byte[] encoded = cipher.doFinal(data);
            encrypted = new byte[iv.length + encoded.length];
            System.arraycopy(iv, 0, encrypted, 0, iv.length);
            System.arraycopy(encoded, 0, encrypted, iv.length, encoded.length);
        } catch(Exception ex) {
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }

        return encrypted;
    }

    public byte[] decryptFile(byte[] data, byte[] key) {
        byte[] decrypted = null;
        try {
            byte[] iv = Arrays.copyOfRange(data, 0,16);
            Cipher cipher = getCipher(iv, key, Cipher.DECRYPT_MODE);
            decrypted = cipher.doFinal(Arrays.copyOfRange(data, 16,data.length));
        } catch(Exception ex) {
            MultiWindowFunctions.showAlert("Something went wrong...", ex.toString());
        }

        return decrypted;
    }

    protected Cipher getCipher(byte[] iv, byte[] key, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        if(key.length != 16 && key.length != 24 && key.length != 32) {
            key = Arrays.copyOf(key, 32);
        }
        SecretKey secret_key = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(padding_);
        cipher.init(mode, secret_key, ivspec);
        return cipher;
    }
}
