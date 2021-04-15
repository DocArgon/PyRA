import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader
{
    static final String configFileName = "resources/config.properties";
    static int port;

    static void loadConfig() throws IOException
    {
        InputStream inputStream = null;
        try
        {
            Properties prop = new Properties();
            inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(configFileName);

            if(inputStream != null)
            {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("File "+configFileName+" not found");
            }

            port = Integer.parseInt(prop.getProperty("port"));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(inputStream != null)
                inputStream.close();
        }
    }
}
