import java.io.Serializable;

public class DataBox implements Serializable
{
    public boolean isFile;
    public String interpreter;
    public String scriptName;
    public String arguments;
    public byte[] fileData;

    public DataBox(boolean isFile, String interpreter, String scriptName, String arguments, byte[] fileData)
    {
        this.isFile = isFile;
        this.interpreter = interpreter;
        this.scriptName = scriptName;
        this.arguments = arguments;
        this.fileData = fileData;
    }
}
