package pg.eti.inf.wdc.project.aes;

public class CTR extends AESModes
{
    public CTR()
    {
        this.padding_ = "AES/CTR/PKCS5Padding";
    }

    public CTR(boolean padding)
    {
        if(padding)
            this.padding_ = "AES/CTR/PKCS5Padding";
        else
            this.padding_ = "AES/CTR/NoPadding";
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
