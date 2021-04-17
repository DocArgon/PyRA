import java.io.*;
import java.net.Socket;

public class ClientPyRA
{
    private final String ip;
    private final String port;

    private Socket clientSocket;
    private OutputStream out;
    private InputStream in;
    private ObjectOutputStream serialOut;

    public ClientPyRA(String ip, String port)
    {
        this.ip = ip;
        this.port = port;
    }

    public boolean sendSkin(String playerName, byte[] skinImage)
    {
        startConnection();
        DataBox msg = new DataBox(true, "skinManager.py", "setSkin "+playerName, skinImage);
        String response = "";
        try
        {
            sendObject(msg);
            response = readBuffer(in);
        } catch (IOException ignore) {}
        stopConnection();
        return Boolean.parseBoolean(response);
    }

    public boolean login(String playerName, String passHash)
    {
        startConnection();
        String response = runScript("userManager.py", "auth "+playerName+" "+passHash);
        stopConnection();
        return Boolean.parseBoolean(response);
    }

    public void hardReset()
    {
        startConnection();
        runScript("serverManager.py", "hardReset");
        stopConnection();
    }

    public String runScript(String scriptName, String arguments)
    {
        startConnection();
        DataBox msg = new DataBox(false, scriptName, arguments, null);
        String response = "";
        try
        {
            sendObject(msg);
            response = readBuffer(in);
        } catch (IOException ignore) {}
        stopConnection();
        return response;
    }

    private void startConnection()
    {
        try
        {
            clientSocket = new Socket(ip, Integer.parseInt(port));
            out = clientSocket.getOutputStream();
            in = clientSocket.getInputStream();
        } catch (IOException ignore) {}
    }

    private void stopConnection()
    {
        try
        {
            in.close();
            out.close();
            serialOut.close();
            clientSocket.close();
        } catch (IOException ignore) {}
    }

    private void sendObject(Object obj) throws IOException
    {
        serialOut = new ObjectOutputStream(out);
        serialOut.writeObject(obj);
        serialOut.flush();
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
