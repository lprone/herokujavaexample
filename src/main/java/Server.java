import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author lprone
 */
public class Server {
    private static final int PORT = Integer.parseInt(System.getenv("PORT"));
    private static ArrayList<Socket> clients = new ArrayList<Socket>();

    /**
     * @param arg
     */
    public static void main(String[] arg) {
        try {
            final ServerSocket skServer = new ServerSocket(PORT);
            System.out.println("Escuchando Puerto " + PORT);
            while (true) {
                Socket skClient = skServer.accept();
                System.out.println("Cliente " + skClient.toString());
                clients.add(skClient);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OutputStream os;
                            DataInputStream receive;
                            DataOutputStream send;
                            while (true) {
                                receive = new DataInputStream(skClient.getInputStream());
                                String mensaje = receive.readUTF();

                                for (Socket client : clients) {
                                    if (skClient != client) {
                                        os = client.getOutputStream();
                                        try {
                                            send = new DataOutputStream(os);
                                            send.writeUTF(mensaje);
                                        } catch (IOException e) {
                                            System.out.println("Error Reenviar");
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            // System.out.println("Error Run");
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            System.out.println("Error Servidor");
        }
    }
}