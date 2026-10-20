package clientPackage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

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
            Scanner sc = new Scanner(System.in);

            System.out.print("Premier opérande : ");
            String a = sc.nextLine();
            System.out.print("Opérateur (+ - * /) : ");
            String op = sc.nextLine();
            System.out.print("Deuxième opérande : ");
            String b = sc.nextLine();

            String ipServeur = "192.168.1.10";
            InetSocketAddress endpoint = new InetSocketAddress(ipServeur, 1234);
            Socket s = new Socket();
            s.connect(endpoint, 5000);

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);

            out.println(a);
            out.println(op);
            out.println(b);

            String reponse = in.readLine();
            System.out.println("Réponse : " + reponse + " (Client n°" + numero + ")");

            s.close();
            sc.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
