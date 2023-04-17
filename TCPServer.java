import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class TCPServer {

    private static ExecutorService exec = null;
    private static Random rand = null;
    private static List<String> quotes = null;
    
    public static void main(String... args) throws Exception {
        // setting global variables
        exec = Executors.newFixedThreadPool(5);
        rand = new Random();
        quotes = readFile("quotes.txt");

        // doing server things
        ServerSocket server = new ServerSocket(17);
        Socket socket = null;
        while ((socket = server.accept()) != null) {
            System.out.println("Accepted client request");
            final Socket threadSocket = socket;
            exec.submit( () -> handleRequest(threadSocket));
        }
        server.close();
    }    

    private static void handleRequest(Socket socket) {
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
