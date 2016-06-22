package minesweeper.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

import minesweeper.server.MinesweeperServer;

public class ServerTest {
    private static final String HOST = "localhost";
    private static final int PORT = 4444;
    private static final int TIMEOUT = 10000;
    private static final String[] args = { 
            "true", 
            "-f", 
            "src/minesweeper/boards/board1.txt" 
            };
    
    private static Thread startServer(int port) {
        Thread serverThread = new Thread(() -> MinesweeperServer.test(args, port));
        serverThread.start();
        return serverThread;
    }
    
    private static Socket connectToServer(Thread server) 
            throws UnknownHostException, IOException {
        Socket socket = new Socket(HOST, PORT);
        return socket;
    }
    
    @Test(timeout=TIMEOUT)
    public void testBoardOne() throws UnknownHostException, IOException {
        Thread serverThread = startServer(PORT);
        Socket socket = connectToServer(serverThread);
        
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));
        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);
        
        // Skip greeting message.
        in.readLine();
        
        out.println("look");
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        in.readLine(); // Skip the last \n
        
        out.println("dig 0 0");
        assertEquals("1------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        in.readLine();
        
        out.println("dig 1 0");
        assertEquals("12-----", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        in.readLine();
        
        out.println("dig 3 0");
        assertEquals("BOOM", in.readLine());
        
        out.println("look");
        assertEquals("12-*---", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        in.readLine();

        
        out.println("bye");
        assertEquals("And a very good day to you too, sir!", in.readLine());
        socket.close();
    }
    
    @Test(timeout=TIMEOUT)
    public void testBoardFlag() throws UnknownHostException, IOException {
        Thread serverThread = startServer(PORT+1);
        Socket socket = connectToServer(serverThread);
        
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()));
        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);
        
        // Skip greeting message.
        in.readLine();
        
        out.println("flag 0 0");
        assertEquals("F------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        in.readLine(); // Skip the last \n
        
        // Digging not allowed
        out.println("dig 0 0");
        assertEquals("F------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        assertEquals("-------", in.readLine());
        in.readLine(); // Skip the last \n
        
        socket.close();
    }
    
    @Test(timeout=TIMEOUT) 
    public void testMultiplayer() throws 
            UnknownHostException, IOException, InterruptedException {
        Thread serverThread = startServer(PORT+2);
        
        Socket firstSocket = connectToServer(serverThread);    
        BufferedReader inFirst = new BufferedReader(
                new InputStreamReader(
                    firstSocket.getInputStream()));
        PrintWriter outFirst = new PrintWriter(
                firstSocket.getOutputStream(), true);
        
        Socket secondSocket = connectToServer(serverThread);    
        BufferedReader inSecond = new BufferedReader(
                new InputStreamReader(
                    secondSocket.getInputStream()));
        PrintWriter outSecond = new PrintWriter(
                secondSocket.getOutputStream(), true);
    
        // Skip greeting message.
        inFirst.readLine();
        inSecond.readLine();
        
        // Flag by player one, out by player two.
        outFirst.println("flag 0 0");
        Thread.sleep(1); // 1 ms for server to change board for player 2.
        outSecond.println("look");
        assertEquals("F------", inSecond.readLine());
        assertEquals("-------", inSecond.readLine());
        assertEquals("-------", inSecond.readLine());
        assertEquals("-------", inSecond.readLine());
        assertEquals("-------", inSecond.readLine());
        assertEquals("-------", inSecond.readLine());
        assertEquals("-------", inSecond.readLine());
        inSecond.readLine(); // Skip the last \n
        
        // First player should see the same.
        assertEquals("F------", inFirst.readLine());
        assertEquals("-------", inFirst.readLine());
        assertEquals("-------", inFirst.readLine());
        assertEquals("-------", inFirst.readLine());
        assertEquals("-------", inFirst.readLine());
        assertEquals("-------", inFirst.readLine());
        assertEquals("-------", inFirst.readLine());
        inFirst.readLine(); // Skip the last \n
        
        // First player digs a bomb, second player checks the board.
        outFirst.println("dig 3 0");
        Thread.sleep(1); // 1 ms for server to change board.
        assertEquals("BOOM", inFirst.readLine());
        
        outSecond.println("look");
        assertEquals("F--*---", inSecond.readLine());
        assertEquals("-------", inSecond.readLine());
        assertEquals("-------", inSecond.readLine());
        assertEquals("-------", inSecond.readLine());
        assertEquals("-------", inSecond.readLine());
        assertEquals("-------", inSecond.readLine());
        assertEquals("-------", inSecond.readLine());
        inSecond.readLine(); // Skip the last \n        
    }
}