package bellum.deorum;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainMenu extends Canvas implements Runnable , KeyListener, ActionListener {
    
    private static final long serialVersionUID = 1L;
    public static final String NAME = "Bellum Deorum";
    public static final int HEIGHT = 256;
    public static final int WIDTH = HEIGHT * 2;
    public static final int SCALE = 2;
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TRANSLUCENT);
    private static boolean running = false;
    static JButton btnStart;
    static JFrame frame;
    public static void main(String[] args){
        MainMenu menu = new MainMenu();
        frame = new JFrame(NAME);
        
        menu.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        menu.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        menu.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addKeyListener(menu);
        //frame.addWindowListener(menu);
        btnStart = new JButton("Start");
        btnStart.addActionListener(menu);
        //btnStart.setBounds(50, 50,50,50);
        frame.setLayout(new FlowLayout());
        frame.add(btnStart);
        frame.add(menu);
        frame.pack();
        frame.setLocationRelativeTo(null);
        running =true;
    }
    
    private void btnStart() { 
        System.out.println("Starting");
        BellumDeorum game = new BellumDeorum();
        frame.dispose();
        game.start();
    }
    
    public void stop() {
        running = false;
    }

    public void run() {      
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D;

        int frames = 0;
        int ticks = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;
            while (delta >= 1) {
                ticks++;
                tick();
                delta -= 1;
                shouldRender = true;
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                System.out.println(ticks + " ticks," + frames + " frames");
                frames = 0;
                ticks = 0;
            }
        }
    }
    int tickCount;

    public void tick() {
        tickCount++;
    }
    int playerMoveX=0;
    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        paint(g);
        g.dispose();
        bs.show();
    }
    
    
    @Override
    public void paint(Graphics g){
        
    }
    
    @Override
    public void keyTyped(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource()==btnStart){
            btnStart();
        }
    }
}
