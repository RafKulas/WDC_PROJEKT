package pg.eti.inf.wdc.project.aes;

public class CBC extends AESModes
{
    public CBC()
    {
        this.padding_ = "AES/CBC/PKCS5Padding";
    }

    public CBC(boolean padding)
    {
        if(padding)
            this.padding_ = "AES/CBC/PKCS5Padding";
        else
            this.padding_ = "AES/CBC/NoPadding";
    }

    @Override
    public byte[][] encrypt(byte[] data)
    {
        return encryptWOKey(data);
    }

    @Override
    public byte[] encrypt(byte[] data, byte[] key, byte[] vector)
    {
        return encryptWKey(data, key, vector);
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] key)
    {
        return decryptFile(data, key);
    }
}
