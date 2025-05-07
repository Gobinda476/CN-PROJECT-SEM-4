import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(12345);
            byte[] receiveData = new byte[1024];
            byte[] sendData;

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String data = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received from client: " + data);

                // Simulate frame loss
                if (Math.random() < 0.3) {
                    System.out.println("Frame lost!");
                    continue; // Skip sending acknowledgment
                }

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                String ack = "ACK: " + data;
                sendData = ack.getBytes();

                // Simulate acknowledgment loss
                if (Math.random() < 0.3) {
                    System.out.println("Acknowledgment lost!");
                    continue; // Skip sending acknowledgment
                }

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
                System.out.println("Sent acknowledgment to client");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}
