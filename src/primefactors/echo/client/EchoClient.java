package primefactors.echo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * A simple client that will interact with an EchoServer.
 * 
 * User-to-Client Echo Message Protocol
 * Valid-Input := String NewLine
 * String := [^NewLine]+
 * NewLine := \n
 * 
 * Client-to-User Echo Message Protocol
 * Valid-Input := Prefix Space String NewLine
 * Prefix := >>>
 * String := [^NewLine]+
 * Space := " "
 * NewLine := \n
 */
public class EchoClient {
    private static final String PREFIX = ">>>";
    private static final int DEFAULT_PORT = 4444;
    
	/**
	 * @param args String array containing Program arguments.  It should only 
	 *      contain exactly one String indicating which server to connect to.
	 *      We require that this string be in the form hostname:portnumber.
	 */
	public static void main(String[] args) throws IOException {
	    
	    if (args.length != 1) {
	        System.err.println("Usage java EchoClient <host name>");
	        System.exit(1);
	    }
	    
	    String hostName = args[0];

	    try (
	            Socket echoSocket = new Socket(hostName, DEFAULT_PORT);
	            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
	            BufferedReader in = new BufferedReader(
	                    new InputStreamReader(echoSocket.getInputStream()));
	    ) {
	        BufferedReader stdIn = new BufferedReader(
	                new InputStreamReader(System.in));
	        String fromServer;
	        String fromUser;
	        
	        while ((fromServer = in.readLine()) != null) {
	            if (fromServer.length() > 0)
	                System.out.println(PREFIX + " " + fromServer);

	            if (fromServer.equals("bb"))
	                break;
	            
	            fromUser = stdIn.readLine();
	            if (fromUser != null) {
	                out.println(fromUser);
	            }
	        }
	    } catch (UnknownHostException uhe) {
	        System.err.println("Don't know about host " + hostName);
	        System.exit(1);
	    } catch (IOException e) {
	        System.err.println("Couldn't get I/O for the connection to  " + hostName);
	        System.exit(1);
	    }
	}
}