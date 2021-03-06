package com.sandro.game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import com.sandro.game.graphics.Screen;


public class Game extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;

    public static int width = 300;
    public static int height = width / 16 * 9;
    public static int scale = 3;

    private Thread thread;
    private JFrame frame;
    private boolean isRunning = false;

    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    private int [] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

    private Screen screen;



    public Game () {
        Dimension size = new Dimension(width * scale, height * scale);
        setPreferredSize (size);

        frame = new JFrame();
        screen = new Screen(width, height);
    }

    public synchronized void start() {
        isRunning = true;
        thread = new Thread(this, "Display");
        thread.start();
    }

    public synchronized void stop (){
        isRunning = false;
        try {
            thread.join();
        }   catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update() {


    }

    public void render(){
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.render();

        for (int i = 0; i < pixels.length; i++){
            pixels [i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor (Color.BLACK);
        g.fillRect (0,0, getWidth(), getHeight());
        g.drawImage (image, 0, 0, getWidth(), getHeight(), null );
        g.dispose();
        bs.show();
    }



    public void run() {
        while (isRunning) {
            update();
            render();
        }
    }


    public static void main (String[] args) {
        Game game = new Game();
        game.frame.setResizable(false);
        game.frame.setTitle("GameRealm");
        game.frame.add(game);
        game.frame.pack();
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo (null);
        game.frame.setVisible (true);

        game.start();
    }


}
