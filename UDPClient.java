import java.io.*;
import java.net.*;

public class UDPClient {
    public static void main(String... args) {

        try (DatagramSocket sock = new DatagramSocket()) {   
            InetAddress host = InetAddress.getByName("localhost");
            int port = Integer.valueOf(17);
            // ignored message
            String message =  "hello";
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), host, port);
            sock.send(packet);

            byte[] buffer = new byte[512];
            DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
            sock.receive(receivedPacket);

            System.out.println(new String(buffer, 0, receivedPacket.getLength()));

            sock.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
      }
}