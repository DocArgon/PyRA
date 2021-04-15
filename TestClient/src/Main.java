import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main
{
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args)
    {
        try
        {
            startConnection("jerzytest.tk", 9642);
            System.out.println(sendMessage("test3;42;arg2"));
            stopConnection();
        }
        catch (IOException e)
        { e.printStackTrace(); }
    }

    public static void startConnection(String ip, int port) throws IOException
    {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public static String sendMessage(String msg) throws IOException
    {
        out.println(msg);
        return in.readLine();
    }

    public static void stopConnection() throws IOException
    {
        in.close();
        out.close();
        clientSocket.close();
    }
}
