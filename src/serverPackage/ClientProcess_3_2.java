package serverPackage;

import commonPackage.Operation;
import java.io.*;
import java.net.Socket;

public class ClientProcess_3_2 extends Thread {

    private Socket socket;
    private int numero;

    public ClientProcess_3_2(Socket socket, int numero) {
        this.socket = socket;
        this.numero = numero;
    }

    @Override
    public void run() {
        try {
            // Ordre important : OIS en premier côté serveur, puis OOS
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            Operation operation = (Operation) in.readObject();
            System.out.println("Client n°" + numero + " a envoyé : "
                    + operation.getOperand1() + " "
                    + operation.getOperator() + " "
                    + operation.getOperand2());

            String reponse;
            try {
                double a = operation.getOperand1();
                double b = operation.getOperand2();

                double resultat = switch (operation.getOperator()) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> {
                        if (b == 0) throw new Exception("Division par zéro");
                        yield a / b;
                    }
                    default -> throw new Exception("Opérateur inconnu");
                };
                reponse = String.valueOf(resultat);

                // Incrémentation du compteur global (accès synchronisé)
                ServerMT_3_2.incrementerCompteur();
            } catch (Exception e) {
                reponse = "Erreur : " + e.getMessage();
            }

            out.writeObject(reponse);
            out.flush();

            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur avec le client n°" + numero + " : " + e.getMessage());
        }
    }
}