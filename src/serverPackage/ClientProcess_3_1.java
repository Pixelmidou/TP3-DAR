package serverPackage;

import java.io.*;
import java.net.Socket;

public class ClientProcess_3_1 extends Thread {

    private Socket socket;
    private int numero;

    public ClientProcess_3_1(Socket socket, int numero) {
        this.socket = socket;
        this.numero = numero;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String reponse;
            try {
                String aStr = in.readLine();
                String op = in.readLine();
                String bStr = in.readLine();

                System.out.println("Client n°" + numero + " à envoyer : " + aStr + " " + op + " " + bStr);

                double a = Double.parseDouble(aStr);
                double b = Double.parseDouble(bStr);

                double resultat = switch (op) {
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
            } catch (Exception e) {
                reponse = "Erreur : " + e.getMessage();
            }

            out.println(reponse);

            socket.close();
        } catch (IOException e) {
            System.out.println("Erreur côté client n°" + numero + " : " + e.getMessage());
        }
    }
}
