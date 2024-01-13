package Server.main;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Thread jouer = new Thread(new Runnable() {
            @Override
            public void run() {
                JFrame window = new JFrame("Tetris Game");
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setResizable(true);

                // Ajout de GamePAnel
                GamePanel gp = new GamePanel();

                window.add(gp);
                window.pack();

                window.setLocationRelativeTo(null);
                window.setVisible(true) ;

                gp.launchGame();
            }
        });
        jouer.start();

        ServerSocket socketserver;
        Socket socketduserveur;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);
        try{
            socketserver = new ServerSocket(6500);
            socketduserveur = socketserver.accept();
            // flux pour envoyer
            out = new PrintWriter(socketduserveur.getOutputStream());
            // flux pour recevoir
            in = new BufferedReader(new InputStreamReader(socketduserveur.getInputStream()));

            Thread recevoir = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    while (true){
                        try {
                            msg = in.readLine();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        System.out.println("Client : "+msg);
                    }
                }
            });
            recevoir.start();

        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}