package primefactors.factors.server;



/**
 *  PrimeFactorsServer performs the "server-side" algorithm 
 *  for counting prime factors.
 *
 *  Your PrimeFactorsServer should take in a single Program Argument 
 *  indicating which port your Server will be listening on.
 *      ex. arg of "4444" will make your Server listen on 4444.
 *      
 *  Your server will only need to handle one client at a time.  If the 
 *  connected client disconnects, your server should go back to listening for
 *  future clients to connect to.
 *  
 *  The client messages that come in will indicate the value that is being
 *  factored and the range of values this server will be processing over.  
 *  Your server will take this in and message back all factors for our value.
 */
public class PrimeFactorsServer {
        
    /** Certainty variable for BigInteger isProbablePrime() function. */
    private final static int PRIME_CERTAINTY = 10;
    
    /**
     * @param args String array containing Program arguments.  It should only 
     *      contain one String indicating the port it should connect to.
     *      Defaults to port 4444 if no Program argument is present.
     */
    public static void main(String[] args) {
        // TODO Complete this implementation.
    }
}
