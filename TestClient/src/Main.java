import java.io.*;
import java.net.Socket;

public class Main
{
    private static Socket clientSocket;
    private static OutputStream out;
    private static InputStream in;

    public static void main(String[] args)
    {
        try
        {
            startConnection("jerzytest.tk", 9642);
            System.out.println(sendMessage("test.py;arg1 arg2 arg3"));
            stopConnection();
        }
        catch (IOException e)
        { e.printStackTrace(); }
    }

    public static void startConnection(String ip, int port) throws IOException
    {
        clientSocket = new Socket(ip, port);
        out = clientSocket.getOutputStream();
        in = clientSocket.getInputStream();
    }

    public static String sendMessage(String msg) throws IOException
    {
        out.write(msg.getBytes());
        byte[] temp = new byte[1024];
        in.read(temp);

        return new String(temp);
    }

    public static void stopConnection() throws IOException
    {
        in.close();
        out.close();
        clientSocket.close();
    }
}
