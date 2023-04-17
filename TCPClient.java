import java.io.*;
import java.net.*;

public class TCPClient {
  public static void main(String... args) {
    String host = "localhost";
    int port = Integer.valueOf(17);

    try (Socket sock = new Socket(host, port)) {
      InputStream in = sock.getInputStream();
      int readChar = 0;
      while ((readChar = in.read()) != -1) {
        System.out.write(readChar);
      }
      sock.close();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}