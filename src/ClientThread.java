import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable
{
    private final Socket clientSocket;
    private final long id;

    private InputStream input;
    private OutputStream output;
    private ObjectInputStream serialInput;

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

        parseInput();

        runScript();

        sendOutput();

        closeConnection();
    }

    private void startConnection()
    {
        print("Connecting to client...");
        try
        {
            input = clientSocket.getInputStream();
            output = clientSocket.getOutputStream();
            serialInput = new ObjectInputStream(input);
        }
        catch (IOException e)
        { e.printStackTrace(); }
    }

    private void parseInput()
    {
        print("Parsing input...");
        msgObject = null;
        try
        {
            msgObject = (DataBox) serialInput.readObject();

            if(msgObject.isFile)
            {
                print("Saving file...");
                FileOutputStream fileOutput = new FileOutputStream(new File("file"+id));
                fileOutput.write(msgObject.fileData);
                fileOutput.close();
            }
        }
        catch (IOException | ClassNotFoundException e)
        { e.printStackTrace(); }
    }

    private void runScript()
    {
        print("Running script...");
        try
        {
            Process p = Runtime.getRuntime().exec(msgObject.interpreter+" "+msgObject.scriptName+" "+id+" "+msgObject.arguments);
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
        print("Sending output...");
        try
        { output.write(scriptOutput.getBytes()); }
        catch (IOException e)
        { e.printStackTrace(); }
    }

    private void closeConnection()
    {
       print("Closing connection...");
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
            System.out.println(" [Thread "+id+"]: "+msg);
    }

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
}
