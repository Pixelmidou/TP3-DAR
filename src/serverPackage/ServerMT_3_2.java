package serverPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMT_3_2 {
    static final int PORT = 1234;

    // Compteur global partagé entre tous les threads ClientProcess_3_2
    private static int compteurOperations = 0;

    // Accès synchronisé : un seul thread à la fois peut incrémenter
    public static synchronized void incrementerCompteur() {
        compteurOperations++;
        System.out.println(">> Nombre total d'opérations traitées : " + compteurOperations);
    }

    public static void main(String[] args) {
        int numero = 0;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur multi-thread en écoute sur le port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                numero++;
                System.out.println("Client n°" + numero
                        + " connecté, adresse : " + socket.getRemoteSocketAddress());

                new ClientProcess_3_2(socket, numero).start();
            }
        } catch (IOException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        }
    }
}