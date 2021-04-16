import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable
{
    private final Socket clientSocket;
    private final long id;

    private InputStream input;
    private OutputStream output;
    private ObjectInputStream serialInput;

    //private String scriptName;
    //private String args;
    private String scriptOutput;
    private DataBox msgObject;

    public ClientThread(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        id = Thread.currentThread().getId();
    }

    @Override
    public void run()
    {
        startConnection();

        parseInput2();

        runScript2();

        sendOutput();

        closeConnection();
    }

    private void startConnection()
    {
        print(" Connecting to client...");
        try
        {
            input = clientSocket.getInputStream();
            output = clientSocket.getOutputStream();
            serialInput = new ObjectInputStream(input);
        }
        catch (IOException e)
        { e.printStackTrace(); }
    }

    private void parseInput2()
    {
        msgObject = null;
        try
        {
            msgObject = (DataBox) serialInput.readObject();

            if(msgObject.isFile)
            {
                FileOutputStream fileOutput = new FileOutputStream(new File("file"+id));
                fileOutput.write(msgObject.fileData);
                fileOutput.close();
            }
        }
        catch (IOException | ClassNotFoundException e)
        { e.printStackTrace(); }
    }

    /*private void parseInput()
    {
        print(" Parsing input...");
        byte[] buffer = new byte[1024];
        int len = 0;
        try
        { len = input.read(buffer); }
        catch (IOException e)
        { e.printStackTrace(); }

        String[] tempArray = new String(buffer).substring(0, len).split(";");

        scriptName = tempArray[0];
        args = tempArray[1];
        print(" Script name: "+scriptName);
        print(" Arguments: "+args);
    }*/

    /*private void runScript()
    {
        print(" Running script...");
        try
        {
            Process p = Runtime.getRuntime().exec("python3 "+scriptName+" "+args);
            byte[] byteScriptOutput = new byte[1024];
            int len = p.getInputStream().read(byteScriptOutput);
            scriptOutput = new String(byteScriptOutput).substring(0, len);

            if(ConfigLoader.errorReport)
            {
                byte[] scriptErrors = new byte[1024];
                len = p.getErrorStream().read(scriptErrors);
                if (len > 0)
                    System.out.println(new String(scriptErrors).substring(0, len));
            }
        }
        catch (IOException e)
        { e.printStackTrace(); }
    }*/

    private String readBuffer(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;

        while (( len = inputStream.read(buffer) ) != -1)
        {
            result.write(buffer, 0, len);
        }
        return result.toString();
    }

    private void runScript2()
    {
        print(" Running script...");
        try
        {
            Process p = Runtime.getRuntime().exec("python3 "+msgObject.scriptName+" "+id+" "+msgObject.arguments);
            scriptOutput = readBuffer(p.getInputStream());

            if(ConfigLoader.errorReport)
            {
                String errorString = readBuffer(p.getErrorStream());
                if(errorString.length() != 0)
                    System.out.println(errorString);
            }
        }
        catch (IOException e)
        { e.printStackTrace(); }
    }

    private void sendOutput()
    {
        print(" Sending output...");
        try
        { output.write(scriptOutput.getBytes()); }
        catch (IOException e)
        { e.printStackTrace(); }
    }

    private void closeConnection()
    {
       print(" Closing connection...");
        try
        {
            output.close();
            input.close();
            serialInput.close();
            clientSocket.close();
        }
        catch (IOException e)
        { e.printStackTrace(); }
    }

    private void print(String msg)
    {
        if(ConfigLoader.verbose)
            System.out.println(msg);
    }
}
