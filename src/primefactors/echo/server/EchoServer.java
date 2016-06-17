package primefactors.echo.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple server that will echo client inputs.
 * 
 * Client-to-Server and Server-to-Client Message Protocol Valid-Input := String
 * NewLine String := [^NewLine]+ NewLine := \n
 */
public class EchoServer {

    private static final int DEFAULT_PORT_NUMBER = 4444;
    
    /**
     * @param args
     *            String array containing Program arguments. It should only
     *            contain at most one String indicating the port it should
     *            connect to. The String should be parseable into an int. If no
     *            arguments, we default to port 4444.
     */
    public static void main(String[] args) throws IOException {

        int portNumber = DEFAULT_PORT_NUMBER;
        if (args.length > 1) {
            System.err.println("Usage: java EchoServer <port number>?");
            System.exit(1);
        } else if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]);
        }

        // Keep listening for new incoming clients on the same assigned port.
            try (
                    ServerSocket serverSocket = new ServerSocket(portNumber);
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    String inputLine;

                    out.println(); // Initiate conversation with client.
                    while ((inputLine = in.readLine()) != null) {
                    out.println(inputLine);
                    if (inputLine.equals("bb"))
                        break;
                }
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port " + portNumber
                        + " or listening for a connection");
                System.out.println(e.getMessage());
            }
    }
}