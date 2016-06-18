package primefactors.factors.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import primefactors.util.BigMath;

/**
 * PrimeFactorsServer performs the "server-side" algorithm for counting prime
 * factors.
 *
 * Your PrimeFactorsServer should take in a single Program Argument indicating
 * which port your Server will be listening on. ex. arg of "4444" will make your
 * Server listen on 4444.
 * 
 * Your server will only need to handle one client at a time. If the connected
 * client disconnects, your server should go back to listening for future
 * clients to connect to.
 * 
 * The client messages that come in will indicate the value that is being
 * factored and the range of values this server will be processing over. Your
 * server will take this in and message back all factors for our value.
 * 
 * Client-to-Server Message Protocol:
 * 
 * Message := Factor Space N Space LowBound Space HighBound Space NewLine
 * Factor := factor
 * N := Number
 * LowBound := Number
 * HighBound := Number
 * Number := [0-9]+
 * Space := " "
 * NewLine := "\n"
 * 
 * Server-to-Client Message Protocol:
 * 
 * Protocol := Message*
 * Message := Found Space N Space Factor NewLine
 *            | Done Space N Space LowBound Space HighBound NewLine
 *            | Invalid NewLine
 * Found := found
 * Done := done
 * Invalid := invalid
 * N := Number
 * Factor := Number
 * LowBound := Number
 * HighBound := Number
 * Number := [0-9]+
 * Space = " "
 * NewLine := "\n"
 */
public class PrimeFactorsServer {

    private final static int DEFAULT_PORT = 4444;
    private final static String INVALID = "invalid";
    
    // Rep invariant: serverSocket != null
    private ServerSocket serverSocket;
    
    /**
     * Make a server that listens for connections on port.
     * @param port
     * @throws IOException
     */
    public PrimeFactorsServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
    
    /**
     * Run server and handle connections.
     * @throws IOException
     */
    public void serve() throws IOException {
//        while (true) {
            Socket socket = serverSocket.accept();
            
            try { 
                handle(socket);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
//        }
    }
    
    /**
     * Handle a client's connection.
     * @param clientSocket The client's socket.
     * @throws IOException
     */
    private void handle(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(
                new OutputStreamWriter(clientSocket.getOutputStream()));
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        
        out.println();

        try {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                // 
                if (inputLine.equals("bb"))
                    break;
                
                if (!isValidMessage(inputLine)) {
                    out.println(INVALID);
                } else {
                    String[] tokens = inputLine.split(" ");
                    BigInteger n = new BigInteger(tokens[1]);
                    BigInteger lo = new BigInteger(tokens[2]);
                    BigInteger hi = new BigInteger(tokens[3]);
                    List<BigInteger> found = BigMath.findAllPrimeFactors(n, lo, hi);
                    
                    for (BigInteger bi : found)
                        out.println(String.format("found %s %s", n.toString(), bi.toString()));
                    out.println(String.format("done %s %s %s", tokens[1], tokens[2], tokens[3]));
                }
                out.flush();
            }
        } finally {
            in.close();
            out.close();
        }
    }

    /**
     * Check the validity of the client's message.
     * See Client-to-Server protocol for message format information.
     * Additional requirements:
     * 2 <= Number
     * 1 <= LowBound <= HighBound
     * @param message The clients message.
     * @return True if a client's message is valid, false otherwise.
     */
    private static boolean isValidMessage(String message) {
        String[] tokens = message.split(" ");
        
        // Number of tokens in a message should be 4 and should start with factor.
        if (tokens.length != 4 || !tokens[0].equals("factor"))
            return false;

        String regex = "\\d+";
        if (!(tokens[1].matches(regex) && tokens[2].matches(regex) && tokens[3].matches(regex)))
                return false;
        
        BigInteger n = new BigInteger(tokens[1]);
        BigInteger low = new BigInteger(tokens[2]);
        BigInteger high = new BigInteger(tokens[3]);

        return n.compareTo(new BigInteger("2")) >= 0 
                && low.compareTo(BigInteger.ONE) >= 0 
                && high.compareTo(low) >= 0;
    }

    /**
     * @param args
     *            String array containing Program arguments. It should only
     *            contain one String indicating the port it should connect to.
     *            Defaults to port 4444 if no Program argument is present.
     */
    public static void main(String[] args) {
//        int portNumber = DEFAULT_PORT;
//        if (args.length > 1) {
//            System.err.println("Usage: java EchoServer <port number>?");
//            System.exit(1);
//        } else if (args.length == 1) {
//            portNumber = Integer.parseInt(args[0]);
//        }
//
//        try (ServerSocket serverSocket = new ServerSocket(portNumber);
//                Socket clientSocket = serverSocket.accept();
//                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//                BufferedReader in = new BufferedReader(
//                        new InputStreamReader(clientSocket.getInputStream()));
//                ) {
//            String inputLine;
//            
//            out.println();
//            while ((inputLine = in.readLine()) != null) {
//                if (inputLine.equals("bb"))
//                    break;
//                
//                if (!isValidMessage(inputLine)) {
//                    out.println(INVALID);
//                } else {
//                    String[] tokens = inputLine.split(" ");
//                    BigInteger n = new BigInteger(tokens[1]);
//                    BigInteger lo = new BigInteger(tokens[2]);
//                    BigInteger hi = new BigInteger(tokens[3]);
//                    List<BigInteger> found = BigMath.findAllPrimeFactors(n, lo, hi);
//                    
//                    for (BigInteger bi : found)
//                        out.println(String.format("found %s %s", n.toString(), bi.toString()));
//                    out.println(String.format("done %s %s %s", tokens[1], tokens[2], tokens[3]));
//                }
//            }
//            
//        } catch (IOException e) {
//            System.out.println("Exception caught when trying to listen on port " + portNumber
//                    + " or listening for a connection");
//            System.out.println(e.getMessage());
//        }
        
      int port = DEFAULT_PORT;
      if (args.length > 1) {
          System.err.println("Usage: java EchoServer <port number>?");
          System.exit(1);
      } else if (args.length == 1) {
          port = Integer.parseInt(args[0]);
      }
      try {
          PrimeFactorsServer server = new PrimeFactorsServer(port);
          server.serve();
      } catch (IOException e) {
          e.printStackTrace();
      }
    }
}