public interface AbstractCipherMode
{
    byte[][] encrypt(byte[] data);
    byte[] encrypt(byte[] data,byte[] key, byte[] vector);
    byte[] decrypt(byte[] data,byte[] key);
}
