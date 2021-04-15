import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientThread implements Runnable
{
    private final Socket clientSocket;

    public ClientThread(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
        try
        {
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();

            output.write(("Polo!").getBytes());

            output.close();
            input.close();
            clientSocket.close();
        } catch (IOException e)
        { e.printStackTrace(); }
    }
}
