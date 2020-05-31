package pg.eti.inf.wdc.project;

public interface AbstractCipherMode
{
    byte[][] encrypt(byte[] data);
    byte[] encrypt(byte[] data,byte[] key);
    byte[] decrypt(byte[] data,byte[] key);
}
