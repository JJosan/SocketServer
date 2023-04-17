import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class SocketServer {

    private static final int PORT = 17;
    private static final int THREADS = 5;

    private static ExecutorService exec = null;
    private static Random rand = null;
    private static List<String> quotes = null;
    
    public static void main(String... args) throws Exception {
        // setting global variables
        exec = Executors.newFixedThreadPool(THREADS);
        rand = new Random();
        quotes = readFile("quotes.txt");

        // doing server things
        exec.submit(() -> TCPServer());
        exec.submit(() -> UDPServer());
    }    

    private static void UDPServer() {
        try {
            while (true) {
                DatagramSocket socket = new DatagramSocket(PORT);
                byte[] buffer = new byte[2];
                DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivedPacket);
                System.out.println("Accepted UDP client request");

                InetAddress clientAddress = receivedPacket.getAddress();
                int clientPort = receivedPacket.getPort();

                String message = quotes.get(rand.nextInt(quotes.size()));

                DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), clientAddress, clientPort);
                socket.send(packet);

                socket.close();
            }
            
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void TCPServer() {
        try {
            ServerSocket server = new ServerSocket(PORT);
            Socket socket = null;
            while ((socket = server.accept()) != null) {
                System.out.println("Accepted TCP client request");
                final Socket threadSocket = socket;
                exec.submit( () -> handleTCPRequest(threadSocket));
            }
            server.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        
    }

    private static void handleTCPRequest(Socket socket) {
        try {
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);
            writer.println(quotes.get(rand.nextInt(quotes.size())));
            out.close();
            socket.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static List<String> readFile(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> result = new ArrayList<>();
        String st;
        while ((st = br.readLine()) != null) {
            result.add(st);
        }
        return result;
    }

}
