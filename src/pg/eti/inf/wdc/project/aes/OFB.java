package pg.eti.inf.wdc.project.aes;

public class OFB extends AESModes
{
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
