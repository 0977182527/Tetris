package Server.main;

import javax.swing.*;
import java.awt.*;

public class GamePanelClient extends JPanel implements Runnable{
    public static final int HEIGHT = 600;
    public static final int WIDTH = 800;
    final int FPS = 60;
    Thread GameThread;
    PlayManager pm;
    PlayManagerClient pmC;

    public GamePanelClient(){
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setBackground(Color.black);
        this.setLayout(null);

        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        pm = new PlayManager();
        pmC = new PlayManagerClient();
    }
    // Lancement du jeu ici
    public void launchGame(){
        GameThread = new Thread(this);
        GameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(GameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta  >= 1){
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update(){
        if(KeyHandler.pausePressed == false && pm.GameOver == false){
            pm.update();
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        pm.draw(g2);

    }
}
