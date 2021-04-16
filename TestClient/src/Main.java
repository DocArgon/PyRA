import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class Main
{
    private static Socket clientSocket;
    private static OutputStream out;
    private static InputStream in;

    private static ObjectOutputStream serialOut;

    public static void main(String[] args)
    {
        try
        {
            startConnection("jerzytest.tk", 9642);
            DataBox msg = new DataBox(true, "python3","skinsave.py", "Michal2", loadImage("Michal.png"));
            System.out.println(sendObject(msg));
            stopConnection();
        }
        catch (IOException e)
        { e.printStackTrace(); }
    }

    public static byte[] loadImage(String path) throws IOException
    {
        return Files.readAllBytes(new File("Michal.png").toPath());
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

    public static String sendObject(Object obj) throws IOException
    {
        serialOut = new ObjectOutputStream(out);
        serialOut.writeObject(obj);
        serialOut.flush();

        byte[] temp = new byte[1024];
        int len = in.read(temp);

        return new String(temp).substring(0, len);
    }

    public static void stopConnection() throws IOException
    {
        in.close();
        out.close();
        serialOut.close();
        clientSocket.close();
    }
}
