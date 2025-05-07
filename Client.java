import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost");
            byte[] sendData;
            byte[] receiveData = new byte[1024];

            for (int i = 0; i < 10; i++) { // Sending 10 frames
                String message = "Frame " + i;
                sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 12345);

                // Simulate frame loss
                if (Math.random() < 0.3) {
                    System.out.println("Frame " + i + " lost!");
                    continue; // Skip waiting for acknowledgment
                }

                clientSocket.send(sendPacket);
                System.out.println("Sent to server: " + message);

                // Receive acknowledgment
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String ack = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received acknowledgment from server: " + ack);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                clientSocket.close();
            }
        }
    }
}
