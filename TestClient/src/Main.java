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
            startConnection("localhost", 9642);
            System.out.println(sendMessage("test.py;2 3 2"));
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
        int len = in.read(temp);

        return new String(temp).substring(0, len);
    }

    public static void stopConnection() throws IOException
    {
        in.close();
        out.close();
        clientSocket.close();
    }
}
