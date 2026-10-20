package clientPackage;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client_3_1 {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Premier opérande : ");
        String a = sc.nextLine();
        System.out.print("Opérateur (+ - * /) : ");
        String op = sc.nextLine();
        System.out.print("Deuxième opérande : ");
        String b = sc.nextLine();

        Socket s = new Socket();
        // same as Socket s = new Socket("localhost", 1234);
        s.connect(new InetSocketAddress(InetAddress.getByName("localhost"), 1234), 5000);

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);

        out.println(a);
        out.println(op);
        out.println(b);

        String reponse = in.readLine();
        System.out.println("Réponse : " + reponse);

        s.close();
        sc.close();
    }
}