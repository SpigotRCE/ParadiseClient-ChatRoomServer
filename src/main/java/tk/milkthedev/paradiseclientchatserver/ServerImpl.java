package tk.milkthedev.paradiseclientchatserver;

import tk.milkthedev.paradiseclientchatserver.handle.ConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Instance of a Server
 */
public class ServerImpl implements Runnable
{
    private final ArrayList<ConnectionHandler> connections = new ArrayList<>();
    ServerSocket serverSocket;
    private static ServerImpl serverImpl;
    private ExecutorService pool;
    private boolean done = false;

    /**
     * Runs the server instance.
     */
    @Override
    public void run()
    {
        serverImpl = this;
        try
        {
            System.out.println("Starting server at 0.0.0.0:45096");
            serverSocket = new ServerSocket(45096);
            pool = Executors.newCachedThreadPool();
            while (!done)
            {
                Socket client = serverSocket.accept();
                System.out.println("Connection from " + client.getInetAddress().getHostAddress());
                ConnectionHandler connectionHandler = new ConnectionHandler(client);
                connections.add(connectionHandler);
                pool.execute(connectionHandler);
            }
        } catch (IOException e)
        {
            shutdown();
        }
    }

    /**
     * Broadcasts a message
     * @param message The message.
     */
    public void broadcast(String message)
    {
        System.out.println(message);
        for (ConnectionHandler ch : connections) {if (ch != null){ch.sendMessage(message);}}
    }

    /**
     * Stops the server.
     */
    public void shutdown()
    {
        System.out.println("Shutting down server...");
        done = true;
        try
        {
            if (!serverSocket.isClosed()){serverSocket.close();}
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        for (ConnectionHandler ch : connections) {if (ch != null) {if (ch.closeConnection("Server is shutting down...")) {connections.remove(ch);}}}
    }

    /**
     * Gets the current running server instance.
     * @return the server instance.
     */
    public static ServerImpl getServer() {return serverImpl;}
}
