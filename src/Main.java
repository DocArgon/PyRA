import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main
{
    private static ServerSocket serverSocket;
    private static Socket clientSocket;

    public static void main(String[] args)
    {
        loadConfig();
        openSocket();

        while(true)
        {
            listenForConnection();
        }
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
        print("Waiting for connection...");
        try
        { clientSocket = serverSocket.accept(); }
        catch (IOException e)
        { e.printStackTrace(); }
        print("Client connected! Starting client thread...");

        new Thread(new ClientThread(clientSocket)).start();
    }

    private static void print(String msg)
    {
        if(ConfigLoader.verbose)
            System.out.println("[Master]: "+msg);
    }
}
