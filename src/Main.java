import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main
{
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static boolean loop = true;

    public static void main(String[] args)
    {
        loadConfig();
        openSocket();

        while(loop)
        {
            listenForConnection();
        }

        closeSocket();
    }

    private static void loadConfig()
    {
        try
        { ConfigLoader.loadConfig(); }
        catch (IOException e)
        { e.printStackTrace(); }
    }

    private static void openSocket()
    {
        try
        { serverSocket = new ServerSocket(ConfigLoader.port); }
        catch (IOException e)
        { e.printStackTrace(); }
    }

    private static void listenForConnection()
    {
        try
        { clientSocket = serverSocket.accept(); }
        catch (IOException e)
        { e.printStackTrace(); }

        new Thread(new ClientThread(clientSocket)).start();
    }

    private static void closeSocket()
    {
        try
        { serverSocket.close(); }
        catch (IOException e)
        { e.printStackTrace(); }
    }
}
