package Client.main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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

        try {
            Socket socket = new Socket("localhost", 6500);
            // flux pour envoyer
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            // flux pour recevoir
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread envoyer = new Thread(() -> {
                try {
                    Robot robot = new Robot();

                    // Définissez les coordonnées et les dimensions de la zone à capturer
                    int x = 400;
                    int y = 20;
                    int width = 800;
                    int height = 800;

                    // Définissez le temps total de capture en secondes
                    int totalSeconds = 30;

                    // Boucle de capture d'écran chaque seconde pendant 30 secondes
                    for (int i = 0; i < totalSeconds; i++) {
                        // Créez un rectangle représentant la zone de capture de l'écran
                        Rectangle screenRect = new Rectangle(x, y, width, height);

                        // Capturez l'image de la zone spécifiée
                        BufferedImage capture = robot.createScreenCapture(screenRect);

                        // Convertir l'image en tableau de bytes
                        byte[] imageBytes = getBytesFromImage(capture);

                        // Envoyer la taille de l'image
                        out.println(imageBytes.length);
                        out.flush();

                        // Envoyer les données de l'image
                        socket.getOutputStream().write(imageBytes);
                        socket.getOutputStream().flush();

                        System.out.println("Capture d'écran " + i + " envoyée au serveur.");

                        // Pause d'une seconde avant la prochaine capture
                        Thread.sleep(1000);
                    }

                    System.out.println("Captures d'écran terminées.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            envoyer.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getBytesFromImage(BufferedImage image) throws IOException {
        // Convertir l'image en tableau de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
}


