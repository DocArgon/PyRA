import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable
{
    private final Socket clientSocket;

    private InputStream input;
    private OutputStream output;

    private String scriptName;
    private String args;
    private byte[] scriptOutput;

    public ClientThread(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
        startConnection();

        parseInput();

        runScript();

        sendOutput();

        closeConnection();
    }

    private void startConnection()
    {
        try
        {
            input = clientSocket.getInputStream();
            output = clientSocket.getOutputStream();
        }
        catch (IOException e)
        { e.printStackTrace(); }
    }

    private void parseInput()
    {
        byte[] buffer = new byte[1024];
        int len = 0;
        try
        { len = input.read(buffer); }
        catch (IOException e)
        { e.printStackTrace(); }

        String[] tempArray = new String(buffer).substring(0, len-1).split(";");

        scriptName = tempArray[0];
        args = tempArray[1];
    }

    private void runScript()
    {
        try
        {
            Process p = Runtime.getRuntime().exec("python3 "+scriptName+" "+args);
            scriptOutput = new byte[1024];
            p.getInputStream().read(scriptOutput);
        }
        catch (IOException e)
        { e.printStackTrace(); }
    }

    private void sendOutput()
    {
        try
        { output.write(scriptOutput); }
        catch (IOException e)
        { e.printStackTrace(); }
    }

    private void closeConnection()
    {
        try
        {
            output.close();
            input.close();
            clientSocket.close();
        }
        catch (IOException e)
        { e.printStackTrace(); }
    }
}
