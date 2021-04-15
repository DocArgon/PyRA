import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            ConfigLoader.loadConfig();
            System.out.println(ConfigLoader.port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
