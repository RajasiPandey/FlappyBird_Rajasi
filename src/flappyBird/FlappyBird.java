package flappyBird;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {
    public static FlappyBird flappyBird;
    public static final  int HEIGHT = 800 , WIDTH = 800;
    public Renderer renderer;
    public Rectangle bird;
    public ArrayList<Rectangle> columns;
    public Random rand;

    public boolean gameOver, started;
    public int ticks, yMotion, score;



    public FlappyBird(){


        renderer = new Renderer();

        rand = new Random();
        JFrame frame = new JFrame();
        Timer timer = new Timer(20, this);


        frame.add(renderer);
        frame.setTitle("FLAPPY BIRD");
        frame.addMouseListener(this);
        frame.addKeyListener(this);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH,HEIGHT);


        frame.setResizable(false);
        frame.setVisible(true);
        columns = new ArrayList<>();
        bird = new Rectangle(WIDTH/2-10,HEIGHT/2-10,20,20);




        addColumns(true);
        addColumns(true);
        addColumns(true);
        addColumns(true);


        timer.start();





    }

    public static void main(String[] args) {
        flappyBird = new FlappyBird();

    }
    public void addColumns(boolean start){
        int space = 300;
        int width = 100;
        int height = 50+ rand.nextInt(300);
        if (start){
            columns.add(new Rectangle(WIDTH+width+ columns.size()*300, HEIGHT-height-120, width, height));
            columns.add(new Rectangle(WIDTH+width+ (columns.size()-1)*300, 0, width, HEIGHT- height - space));

        }
        else {
            columns.add(new Rectangle(columns.get(columns.size()-1).x+600, HEIGHT-height-120, width, height));
            columns.add(new Rectangle(columns.get(columns.size()-1).x, 0, width, HEIGHT- height - space));
        }

    }
    public void paintColumn(Graphics g, Rectangle column){
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width,column.height);
    }

    public void repaint(Graphics g) {
        // frame
        g.setColor(Color.cyan);
        g.fillRect(0,0,WIDTH, HEIGHT);
        // ground
        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT-150,WIDTH,150);
        //grass
        g.setColor(Color.green);
        g.fillRect(0, HEIGHT-150,WIDTH,20);
        //ccolumnss
        for (Rectangle column : columns ){
            paintColumn(g,column);
        }

        // bird
        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        // game over message
        g.setColor(Color.white);
        g.setFont(new Font("Arial",1,100));
        if (!started){
            g.drawString("START",100, HEIGHT/2-100);
        }

        if (gameOver){
            g.drawString("GAME OVER!!",75, HEIGHT/2-50);
        }

        if(!gameOver && started){
            g.drawString(String.valueOf(score), WIDTH/2-100,100);
        }



    }
    public void jump(){
        if (gameOver){
            bird = new Rectangle(WIDTH/2-10,HEIGHT/2-10,20,20);
            columns.clear();
            yMotion =0;
            score = 0;



            addColumns(true);
            addColumns(true);
            addColumns(true);
            addColumns(true);


            gameOver = false;

        }
        if(!started){
            started = true;

        }
        else if(!gameOver){
            if (yMotion>0){
                yMotion =0;
            }
            yMotion -=10;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int speed = 10;
        ticks++;
        if (started) {
            for (int i = 0; i < columns.size(); i++) {
                Rectangle col = columns.get(i);
                col.x -= speed;
                if (col.x + col.width < 0) {
                    columns.remove(col);

                    if (col.y == 0) {
                        addColumns(false);
                    }
                }

            }
            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }

            bird.y += yMotion;
            // checking for collision
            for (Rectangle currCol : columns) {
                if (currCol.y == 0 && bird.x + bird.width / 2 > currCol.x + currCol.width / 2 -10 && bird.x + bird.width / 2 < currCol.x + currCol.width / 2 + 10)
                {
                    score++;
                }

                if (currCol.intersects(bird)) {
                    gameOver = true;

                    if (bird.x<=currCol.x){
                        bird.x = currCol.x-bird.width;
                    }
                    else {
                        if (currCol.y != 0) {
                            bird.y = currCol.y - bird.height;
                        }
                        else if (bird.y< currCol.height){
                            bird.y = currCol.height;
                        }
                    }
                }
            }
            if (bird.y >= HEIGHT - 120 || bird.y < 0) {
                gameOver = true;
            }
        }


        renderer.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            jump();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
