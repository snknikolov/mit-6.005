package primefactors.factors.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import primefactors.util.BigMath;

/**
 *  PrimeFactorsClient class for PrimeFactorsServer.  
 *  
 *  Your PrimeFactorsClient class should take in Program arguments space-delimited
 *  indicating which PrimeFactorsServers it will connect to.
 *      ex. args of "localhost:4444 localhost:4445 localhost:4446"
 *          will connect the client to PrimeFactorsServers running on
 *          localhost:4444, localhost:4445, localhost:4446 
 *
 *  Your client should take user input from standard input.  The appropriate input
 *  that can be processed is a number.  If your input is not of the correct format,
 *  you should ignore it and continue to the next one.
 *  
 *  Your client should distribute to each server the appropriate range of values
 *  to look for prime factors through.
 *  
 *  User-to-Client Message Protocol:
 *  Valid-Input := Space N Space NewLine
 *  N := [0-9]+
 *  Space := " "
 *  NewLine := "\n"
 *  
 *  Client-to-User Message Protocol:
 *  Valid-Input := Prefix Space
 *                 ( (N Equals Factor (Mult Factor)* NewLine) | Invalid)
 *  Prefix := ">>>"
 *  N := Number
 *  Factor := Number
 *  Invalid := invalid
 *  Equals := "="
 *  Mult := "*"
 *  Number := [0-9]+
 *  Space := " "
 *  NewLine := "\n"
 *  
 */

public class PrimeFactorsClient {

    private final List<SubClient> subClients;
    
    /**
     * Make a PrimeFactorsClient.
     * Requires host name and port number for each sub client.
     * @param hostNames The list of all host names.
     * @param ports The list of all ports.
     * @throws IOException
     */
    public PrimeFactorsClient(List<String> hostNames, List<Integer> ports) 
            throws IOException {
        assert hostNames.size() == ports.size();
        subClients = new ArrayList<SubClient>();
        
        // For each pair <host name : port>, make a new sub client that is 
        // connected to a server running on host name at specified port.
        for (int i = 0; i < hostNames.size(); i++) {
            SubClient p = new SubClient(hostNames.get(i), ports.get(i));
            subClients.add(p);
        }
    }
        
    /**
     * Send request to each sub client.
     * @param n The number to be factorised.
     * @throws IOException
     */
    public void sendRequest(BigInteger n) throws IOException {
        // Partition the search space and send request to each sub client.
        // The result has the form [low1, high1], [low2, high2], ... , [low n, high n]
        List<BigInteger> partitions = partition(n, BigInteger.ONE);

        for (int i = 0; i < subClients.size(); i++) {
            BigInteger lo = partitions.get(i*2);
            BigInteger hi = partitions.get(i*2+1);
            subClients.get(i).sendRequest(n, lo, hi);
        }
    }
    
    /**
     * Collect replies from all sub clients.
     * @param n User's query number.
     * @return The result of prime factorisation 
     *          as defined in Client-to-User protocol.
     * @throws IOException
     */
    public String getReply(String number) throws IOException {
        List<String> allFactors = new LinkedList<String>();

        for (SubClient sc : subClients) {
            List<String> subClientsFactors = sc.getReply();
            allFactors.addAll(subClientsFactors);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(">>> " + number + " = ");
        for (String s : allFactors)
            sb.append(s + " * ");
        
        return sb.toString()
                .substring(0,  sb.length()-2); // Don't include trailing "*".
    }
    
    /**
     * Partition the search space according to the number of servers available.
     * 
     * Partition for i servers:
     * [2, sqrt(N)/i], [sqrt(N)/i + 1, 2*sqrt(N)/i], [2*sqrt(N)/i + 1, 3*sqrt(N)/i]
     * ...
     * [i-1*sqrt(N)/i + 1, i*sqrt(N)/i]
     * 
     * @param n The query number
     * @return List of search space intervals.
     */
    private List<BigInteger> partition(BigInteger n, BigInteger g) {
        List<BigInteger> intervals = new ArrayList<BigInteger>();
        BigInteger denom = BigInteger.valueOf(subClients.size());
        BigInteger sqrtN = BigMath.sqrt(n);
        BigInteger rhs = sqrtN.divide(denom);
        
        BigInteger start = new BigInteger("2");
        BigInteger end = rhs;
        intervals.add(start);
        intervals.add(end);
        
        for (int i = 2; i <= subClients.size(); i++) {
            start = end.add(BigInteger.ONE);
            end = rhs.multiply(BigInteger.valueOf(i));
            
            intervals.add(start);
            intervals.add(end);
        }
        
        // i*sqrt(N)/i might not equal sqrtN.
        // Remove the last element and add sqrt(N) instead.
        intervals.remove(intervals.size() -1);
        intervals.add(sqrtN);
        
        return intervals;
    }
    
        
    /**
     * See class description for use of args.
     */         
    public static void main(String[] args) throws IOException {
      List<String> hosts = new LinkedList<String>();
      List<Integer> ports = new LinkedList<Integer>();
            
      // Collect args and add them to the appropriate lists.
      for (int i = 0; i < args.length; i++) {
          int delimiterPosition = args[0].indexOf(":");
          if (delimiterPosition == -1) {
              System.err.println("Usage: <host name>:<port>");
              System.exit(1);
          }
          String hostName = args[i].substring(0, delimiterPosition);
          String port = args[i].substring(delimiterPosition+1, args[i].length());
          
          if (port.matches("\\d+")) {
              hosts.add(hostName);
              ports.add(Integer.valueOf(port));
          } else {
              System.err.println("Usage: <host name>:<port>");
              System.exit(1);
          }
      }
      
      // Make a new client and reply to user input until user types "bb".
      PrimeFactorsClient client = new PrimeFactorsClient(hosts, ports);
      BufferedReader stdIn = new BufferedReader(
              new InputStreamReader(System.in));
      
      try {
          String fromUser = stdIn.readLine();
          
          while (!fromUser.equals("bb")) {
              if (fromUser.matches("\\d+")) {
                  client.sendRequest(new BigInteger(fromUser));
                  String reply = client.getReply(fromUser);
                  System.out.println(reply); 
              } else {
                  System.out.println(">>> invalid");
              }
              fromUser = stdIn.readLine();
          }
      } finally {
          for (SubClient sc : client.subClients)
              sc.close(); 
          stdIn.close();
      } 
    }     
}

class SubClient {
    
    private static final String SERVER_DONE = "done";
    private static final String SERVER_FACTOR = "factor ";
    
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    
    /**
     * Make a sub client and connect it to server.
     * @param hostName
     * @param port
     * @throws IOException
     */
    SubClient(String hostName, int port) throws IOException {
        socket = new Socket(hostName, port);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream()));
    }
        
    /**
     * Send a request to server.
     * 
     * @param n The integer to be factorised.
     * @param low Low factor bound.
     * @param high High factor bound.
     * @throws IOException
     */
    public void sendRequest(BigInteger n, BigInteger low, BigInteger high)
            throws IOException {
        out.println(SERVER_FACTOR + n + " " + low + " " + high);
        out.flush();
    }
    
    /**
     * Get a reply from server.
     * @return The list of factors found by server.
     * @throws IOException
     */
    public List<String> getReply() throws IOException {
        List<String> factors = new LinkedList<>();
        String fromServer;
        
        while ((fromServer = in.readLine()) != null 
                && fromServer.indexOf(SERVER_DONE) == -1) {
            if (fromServer.length() > 0) {
                String[] result = fromServer.split(" ");
                factors.add(result[2]); // Factor is at position 2.
            }
        }
        return factors;
    }
    
    /**
     * Close the client connections to server.
     * @throws IOException
     */
    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }
}