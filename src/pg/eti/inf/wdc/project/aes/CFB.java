package pg.eti.inf.wdc.project.aes;

public class CFB extends AESModes
{
    public CFB()
    {
        this.padding_ = "AES/CFB/PKCS5Padding";
    }

    public CFB(boolean padding)
    {
        if(padding)
            this.padding_ = "AES/CFB/PKCS5Padding";
        else
            this.padding_ = "AES/CFB/NoPadding";
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
