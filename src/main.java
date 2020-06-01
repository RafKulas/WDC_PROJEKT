import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class main
{
    public static void main(String[] args) throws Exception
    {
        String path = "C:\\Users\\janja\\Desktop";
        AES aes = new AES(new CTR(),path);

        aes.encrypt(new File(path + "\\1234.png"));
        aes.decrypt(new File(path + "\\encrypted.png"),new File(path + "\\key.txt"));

        aes.encrypt("qwertyxd");
        String text = new String(Files.readAllBytes(Paths.get("C:\\Users\\janja\\Desktop\\encrypted.txt")),"ISO-8859-1");
        aes.decrypt(text, new File(path + "\\key.txt"));

        aes.encrypt("qwertyxd", "shshshshshshshsh");
        text = new String(Files.readAllBytes(Paths.get("C:\\Users\\janja\\Desktop\\encrypted.txt")),"ISO-8859-1");
        aes.decrypt(text,"shshshshshshshsh");

        aes.encrypt("qwertyxd",new File(path + "\\key.txt"));
        text = new String(Files.readAllBytes(Paths.get("C:\\Users\\janja\\Desktop\\encrypted.txt")),"ISO-8859-1");
        aes.decrypt(text,new File(path + "\\key.txt"));


        aes.encrypt(new File(path + "\\decrypted.txt"));
        aes.decrypt(new File(path + "\\encrypted.txt"),new File(path + "\\key.txt"));

        aes.encrypt(new File(path + "\\decrypted.txt"), "shshsh");
        aes.decrypt(new File(path + "\\encrypted.txt"),"shshsh");

        aes.encrypt(new File(path + "\\decrypted.txt"),new File(path + "\\key.txt"));
        aes.decrypt(new File(path + "\\encrypted.txt"),new File(path + "\\key.txt"));


        text = new String(Files.readAllBytes(Paths.get("C:\\Users\\janja\\Desktop\\encrypted.txt")),"ISO-8859-1");
        String key  = new String(Files.readAllBytes(Paths.get("C:\\Users\\janja\\Desktop\\key.txt")),"ISO-8859-1");

        aes.decrypt(text,key);
        aes.decrypt(text,new File(path + "\\key.txt"));
        aes.decrypt(new File(path + "\\encrypted.txt"),key);
        aes.decrypt(new File(path + "\\encrypted.txt"),new File(path + "\\key.txt"));
    }
}
