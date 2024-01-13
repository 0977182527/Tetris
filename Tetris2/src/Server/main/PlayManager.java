package Server.main;

import Server.mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayManager {
    final int WIDTH = 360;
    final int HEIGTH = 510;
    public static int left_x;
    public static int right_x;
    public static int bottom_y;
    public static int top_y;

    int level = 1;
    int score = 0;


    //Mino
    Mino CurrentMino;
    final int MINO_START_X;
    final int MINO_START_Y;

    //next Server.mino
    Mino nextMino;
    final int NEXMINO_X;
    final int NEXMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    public static int dropInterval = 60;
    boolean GameOver;


    public PlayManager(){
        left_x = 550;//(GamePanel.WIDTH/2) - (WIDTH/2); // ( (800/2 = 400) - (360/2) = 180 ) = 220
        right_x = left_x + WIDTH;
        top_y = 200;
        bottom_y = top_y + HEIGTH;
        System.out.println(GamePanel.WIDTH/2);
        MINO_START_X = left_x + (WIDTH/2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXMINO_X = right_x + 90;
        NEXMINO_Y = top_y + 450;

        //set the starting Server.mino
        CurrentMino = pickMino();

        CurrentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXMINO_X - 200, NEXMINO_Y - 530);

    }

    private Mino pickMino(){
        Mino mino = null ;
        int i = new Random().nextInt(7);

        switch (i){
            case 0: mino = new Mino_L1();
                System.out.println("Mino L1");
                break;
            case 1: mino = new Mino_L2();
                System.out.println("Mino L2");
            break;
            case 2: mino = new Mino_T();
                System.out.println("Mino T");
            break;
            case 3: mino = new MIno_Z1();
                System.out.println("Mino Z1");
            break;
            case 4: mino = new Mino_Square();
                System.out.println("Mino Square");
            break;
            case 5: mino = new Mino_Bar();
                System.out.println("Mino Bar");
            break;
            case 6: mino = new Mino_Z2();
                System.out.println("Mino Z2");
            break;
        }
        return mino;
    }

    public void update(){

        if(CurrentMino.active == false){
            staticBlocks.add(CurrentMino.b[0]);
            staticBlocks.add(CurrentMino.b[1]);
            staticBlocks.add(CurrentMino.b[2]);
            staticBlocks.add(CurrentMino.b[3]);
            if (CurrentMino.b[0].x == MINO_START_X && CurrentMino.b[0].y == MINO_START_Y){
                GameOver = true;
            }

            CurrentMino.deactivating = false;

            CurrentMino = nextMino;

            CurrentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXMINO_X - 200, NEXMINO_Y - 530);
            // On verifie si le Server.mino devient desactiver
            checkDelete();
        }else{
            CurrentMino.update();
        }
    }
    public void checkDelete(){
        int x = left_x;
        int y = top_y;

        int blockCount = 0;
        int lineCount = 0;

        while(x < right_x && y < bottom_y){
            for (int i = 0; i < staticBlocks.size(); i++) {
                if(staticBlocks.get(i).x == x && staticBlocks.get(i).y ==y){
                    blockCount++;
                }
            }


            x += Block.SIZE;
            if(x == right_x){

                if (blockCount == 12){
                    for (int i = staticBlocks.size() -1; i > -1; i--) {
                        if(staticBlocks.get(i).y == y){
                            staticBlocks.remove(i);
                        }
                    }

                    lineCount ++;

                    for (int i = 0; i < staticBlocks.size() ; i++) {
                        if(staticBlocks.get(i).y < y){
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }
                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }

        score += lineCount;

    }

    public void draw(Graphics2D g2){
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(left_x - 2 ,top_y - 2, WIDTH +8 , HEIGTH +8);

        //Mino frame
        int x =  right_x + 40;
        int y = bottom_y - 150;
        g2.drawRect(x - 183,y - 525,150,155);
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x -140, y - 500);

        //g2.drawString("LEVEL : " + level, x - 400, y - 500);
        g2.drawString("SCORE :" + score, x - 400, y - 500);

        // Draw another rect for a client

        g2.drawRect(left_x + 500 , top_y , WIDTH, HEIGTH);
        // Draw the current Server.mino
        if(CurrentMino != null){
            CurrentMino.draw(g2);
        }

        nextMino.draw(g2);

        //Draw StaticsBlocks
        for (int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }

        //Pause
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(50f));
        if(GameOver){
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("GAME OVER", x, y);
        }else if(KeyHandler.pausePressed){
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }
    }
}
