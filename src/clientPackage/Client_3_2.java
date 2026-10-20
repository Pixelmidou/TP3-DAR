package clientPackage;

import commonPackage.Operation;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client_3_2 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Premier opérande : ");
        double a = Double.parseDouble(sc.nextLine());
        System.out.print("Opérateur (+ - * /) : ");
        String op = sc.nextLine();
        System.out.print("Deuxième opérande : ");
        double b = Double.parseDouble(sc.nextLine());

        Operation operation = new Operation(a, op, b);

        // Remplacer "localhost" par l'IP réelle du serveur en réseau local
        String ipServeur = "localhost";
        Socket s = new Socket();
        s.connect(new InetSocketAddress(InetAddress.getByName(ipServeur), 1234), 5000);

        // Ordre côté client : OOS en premier, puis OIS
        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
        out.flush();
        ObjectInputStream in = new ObjectInputStream(s.getInputStream());

        out.writeObject(operation);
        out.flush();

        String reponse = (String) in.readObject();
        System.out.println("Réponse : " + reponse);

        s.close();
        sc.close();
    }
}