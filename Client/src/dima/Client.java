package dima;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private static int serverPort = 20002;
    private static String address = "127.0.0.1";
    private static InetAddress ipAddress;
    protected static int X = 3;
    protected static int Y = Y(150, 300);

    protected static void start() {

        try {
            ipAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        try (Socket socket = new Socket(ipAddress, serverPort)) {
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            out.writeUTF(String.valueOf(X) + "," + String.valueOf(Y));

            boolean exit = true;

            for (; exit; ) {
                String line = keyboard.readLine();
                out.writeUTF(line);
                out.flush();
                line = in.readUTF();
                System.out.println(line);

                if (line.equals("All active connections are closed") || line.equals("close connection")) {
                    exit = false;
                    socket.close();
                }
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    protected static int Y(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
