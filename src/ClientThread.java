import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable
{
    private final Socket clientSocket;

    private InputStream input;
    private OutputStream output;

    private String scriptName;
    private String[] args;

    public ClientThread(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
        try
        {
            //System.out.println("    Client thread connecting...");
            startConnection();

            //System.out.println("    Parsing input...");
            parseInput(input);

            output.write((scriptName+" "+args[0]).getBytes());

            closeConnection();
        } catch (IOException e)
        { e.printStackTrace(); }
    }

    private void startConnection() throws IOException
    {
        input = clientSocket.getInputStream();
        output = clientSocket.getOutputStream();
    }

    private void parseInput(InputStream input) throws IOException
    {
        byte[] buffer = new byte[1024];
        int len = input.read(buffer);

        String[] tempArray = new String(buffer).split(";");

        scriptName = tempArray[0];
        args = new String[tempArray.length-1];

        System.arraycopy(tempArray, 1, args, 0, tempArray.length - 1);
    }

    private void closeConnection() throws IOException
    {
        output.close();
        input.close();
        clientSocket.close();
    }
}
