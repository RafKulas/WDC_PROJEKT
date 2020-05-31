package pg.eti.inf.wdc.project;

import java.io.File;

public interface IAES
{
    // Set disk path for saving files
    // key will be save as path/key.txt
    // encrypted data will be save as path/encrypted.txt (In some Cipher Mode data will be save with initialization vector at the start of file)
    // decrypted key will be save as path/decrypted.txt (will be changed in future for save as path )
    void SetPath(String path);

    // Some overloaded function
    // Base call looks like:

    // encrypt(data_to_encrypt, key)
    // where data_to_encrypt can be String type or File type
    // and key can be String type or File type or can be not passed (will be generate automaticly and save as written above)
    void encrypt(String data);
    void encrypt(String data, String key);
    void encrypt(String data, File file_key);

    void encrypt(File file_data);
    void encrypt(File file_data, String key);
    void encrypt(File file_data, File file_key);

    // decrypt(data_to_encrypt, key)
    // where data_to_encrypt can be String type or File type
    // and key can be String type or File type
    void decrypt(String data, String key);
    void decrypt(String data, File file_key);

    void decrypt(File file_data, String key);
    void decrypt(File file_data, File file_key);

}
