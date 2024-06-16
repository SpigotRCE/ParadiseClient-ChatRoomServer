import tk.milkthedev.paradiseclientchatserver.ServerImpl;

/**
 * The main running class
 */
public class Main
{
    public static void main( String[] args )
    {
        ServerImpl server = new ServerImpl();
        server.run();
    }
}
