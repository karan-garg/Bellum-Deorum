/*
 * Bellum Deorum
 * Completed June 11, 2014
 * A Homestuck-based strategy game.
 */
package bellum.deorum;

import java.awt.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//NOTE *****VERY IMPORTANT*****Since this program does NOT have a constructor, make sure u intitialise everthing
//under the start method or you will get a Null pointer.******
public class BellumDeorum extends JPanel implements java.io.Serializable, Runnable, MouseListener, KeyListener, WindowListener {

    private final long serialVersionUID = 1L;
    public String Name = "Bellum Deorum";
    public final int HEIGHT = 256;
    public final int WIDTH = HEIGHT * 2;
    public final int SCALE = 2;
    private transient BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TRANSLUCENT);
    private boolean running = false;
    private JFrame frame;
    private transient Canvas canvas;
    private transient BufferStrategy bufferStrategy;

    //This array list holds all coordinates reachable by the selected piece, if a piece is selected.
    private ArrayList<int[]> reachable;
    transient BufferedImage battlebackground;
    protected boolean saved = false, battle = false, newGame = true;
    int tickCount;
    int turn = 1;
    int player1Actions = 4; //Player 1 gets 4 actions per turn.
    int player2Actions = 3; //Player 2 gets 3 actions per turn.
    int playerMoving = 1; // Player 1 moves first on the first turn.

    //Pieces.
    protected Piece selectedPiece; //This variable holds any piece that is selected.
    protected Piece boardPieces[][] = new Piece[8][12]; //The array to hold the pieces on the board.
    //Player 1's pieces;
    protected God john;
    protected God dave;
    protected God rose;
    protected God roxy;
    protected God jake;
    protected Mortal karkat;
    protected Mortal terezi;
    protected Mortal kanaya;
    protected God dirk;
    //Player 2's pieces
    protected Mortal condesce;
    protected God aranea;
    protected God jade;
    protected God jane;
    protected Gamzee gamzee;
    protected Mortal jackNoir;
    protected Mortal theFelt;
    protected God caliborn;
    //An empty piece.
    Piece empty;

    //Sprites.
    transient BufferedImage mapBackground;
    transient BufferedImage baseSprite;
    //The sprites for player 1's pieces.
    protected transient BufferedImage johnSprite;
    protected transient BufferedImage daveSprite;
    protected transient BufferedImage roseSprite;
    protected transient BufferedImage roxySprite;
    protected transient BufferedImage jakeSprite;
    protected transient BufferedImage karkatSprite;
    protected transient BufferedImage tereziSprite;
    protected transient BufferedImage kanayaSprite;
    protected transient BufferedImage dirkSprite;
    //The reversed sprites for player 1's pieces.
    protected transient BufferedImage johnSpriteReversed;
    protected transient BufferedImage daveSpriteReversed;
    protected transient BufferedImage roseSpriteReversed;
    protected transient BufferedImage roxySpriteReversed;
    protected transient BufferedImage jakeSpriteReversed;
    protected transient BufferedImage karkatSpriteReversed;
    protected transient BufferedImage tereziSpriteReversed;
    protected transient BufferedImage kanayaSpriteReversed;
    protected transient BufferedImage dirkSpriteReversed;
    //The sprites for player 2's pieces.
    protected transient BufferedImage condesceSprite;
    protected transient BufferedImage araneaSprite;
    protected transient BufferedImage jadeSpriteEvil;
    protected transient BufferedImage jadeSpriteGood;
    protected transient BufferedImage janeSpriteEvil;
    protected transient BufferedImage janeSpriteGood;
    protected transient BufferedImage gamzeeSprite;
    protected transient BufferedImage jackNoirSprite;
    protected transient BufferedImage theFeltSprite;
    protected transient BufferedImage calibornSprite;
    //The reversed sprites for player 2's pieces.
    protected transient BufferedImage condesceSpriteReversed;
    protected transient BufferedImage araneaSpriteReversed;
    protected transient BufferedImage jadeSpriteEvilReversed;
    protected transient BufferedImage jadeSpriteGoodReversed;
    protected transient BufferedImage janeSpriteEvilReversed;
    protected transient BufferedImage janeSpriteGoodReversed;
    protected transient BufferedImage gamzeeSpriteReversed;
    protected transient BufferedImage jackNoirSpriteReversed;
    protected transient BufferedImage theFeltSpriteReversed;
    protected transient BufferedImage calibornSpriteReversed;

    //Variables relating to combat and god revival.
    protected ArrayList<Piece[]> combatants = new ArrayList(); //This one holds pairs of combatants for battles during a turn.
    protected ArrayList<Piece> reinforcements[]; //This array holds the reinforcements for rounds 2 and 3 of a battle.
    protected boolean needToRevive = false; //This variable is true when a god is killed and is switched back to false after the attempt to revive it is over.
    protected Piece mustRevive; //This holds any piece that one must attempt to revive.
    protected int revivalRoll1 = 0; //These variables hold the dice rolls for the revival screen.
    protected int revivalRoll2 = 0;
    protected int battleRoll1 = 0; //These variables hold the rolls for players 1 and 2 in battle.
    protected int battleRoll2 = 0;
    protected transient BufferedImage[] die; //The sprites for the die.
    protected boolean rolled = false; //This is set to true after dice are rolled. The purpose of this is to let screens be drawn one last time after the user rolls the dice so the user can see what the roles were.
    protected boolean movesDone = false; //This is set to true after the users finish moving in a turn. The purpose of this is to let screen be drawn one last time after the users finish their moves so they can see the board's final position.

    //These are the sprites of tiles that are drwn with pieces to give information about what they are currently doing.
    protected transient BufferedImage attackTile;
    protected transient BufferedImage attackingTile;
    protected transient BufferedImage moveTile;
    protected transient BufferedImage player1Tile;
    protected transient BufferedImage player2Tile;
    protected transient BufferedImage gamzeeTile;
    protected transient BufferedImage whiteTile;
    protected transient BufferedImage greyTile;

    //A variable to hold the identity of the winning player. 0 initially because no one has won yet.
    protected int victor = 0;

    //This method is called when this screen is brought up.
    public void start() {
        frame = new JFrame(Name); //Initialize this frame.
        frame.setVisible(true); //Make this frame visible.
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TRANSLUCENT);
        canvas = new Canvas();
        bufferStrategy = canvas.getBufferStrategy();
        canvas.setBounds(0, 0, WIDTH, HEIGHT); //Do stuff with the frame size.
        canvas.setIgnoreRepaint(true);
        canvas.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        canvas.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        canvas.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        canvas.addMouseListener(this);
        canvas.addKeyListener(this);
        frame.add(canvas);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addKeyListener(this);
        frame.addWindowListener(this);
        running = true;
        die = new BufferedImage[6]; //Initialize the array of images of the die.
        sprites();
        new Thread(this).start();
    }

    public void run() {
        long lastTime = System.nanoTime(); //Store the time in nanoseconds at which this method began.
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
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick() {
        tickCount++;
    }

    public void render() {
        if (bufferStrategy == null) {
            canvas.createBufferStrategy(3);
        }
        bufferStrategy = canvas.getBufferStrategy();
        Graphics g = bufferStrategy.getDrawGraphics();
        paint(g); //Draw the board.
        g.dispose();
        bufferStrategy.show();
    }

    public void sprites() {
        BufferedImageLoader loader = new BufferedImageLoader(); //Make an object to load buffere images with.
        BufferedImage Sprites = null, baseSprites = null;
        try {
            Sprites = loader.loadImage("/resources/spriteSheet.png");
        } catch (IOException ex) {
            Logger.getLogger(BellumDeorum.class.getName()).log(Level.SEVERE, null, ex);
        }
        SpriteSheet SpriteSheet = new SpriteSheet(Sprites); //Make a sprite sheet to grab images off of.
        SpriteSheet baseSheet = new SpriteSheet(baseSprites);

        //Get the sprites for the pieces.
        daveSprite = SpriteSheet.grabSprite(0, 0, 64, 64);
        dirkSprite = SpriteSheet.grabSprite(128, 0, 64, 64);
        araneaSprite = SpriteSheet.grabSprite(0, 64, 64, 64);
        calibornSprite = SpriteSheet.grabSprite(128, 64, 64, 64);
        roseSprite = SpriteSheet.grabSprite(128, 128, 64, 64);
        roxySprite = SpriteSheet.grabSprite(0, 192, 64, 64);
        tereziSprite = SpriteSheet.grabSprite(128, 192, 64, 64);
        janeSpriteGood = SpriteSheet.grabSprite(0, 256, 64, 64);
        janeSpriteEvil = SpriteSheet.grabSprite(128, 256, 64, 64);
        theFeltSprite = SpriteSheet.grabSprite(0, 320, 64, 64);
        jackNoirSprite = SpriteSheet.grabSprite(128, 320, 64, 64);
        kanayaSprite = SpriteSheet.grabSprite(0, 384, 64, 64);
        karkatSprite = SpriteSheet.grabSprite(128, 384, 64, 64);
        johnSprite = SpriteSheet.grabSprite(0, 448, 64, 64);
        jakeSprite = SpriteSheet.grabSprite(128, 448, 64, 64);
        jadeSpriteEvil = SpriteSheet.grabSprite(0, 512, 64, 64);
        jadeSpriteGood = SpriteSheet.grabSprite(128, 512, 64, 64);
        condesceSprite = SpriteSheet.grabSprite(0, 576, 64, 64);
        gamzeeSprite = SpriteSheet.grabSprite(128, 576, 64, 64);

        daveSpriteReversed = SpriteSheet.grabSprite(64, 0, 64, 64);
        dirkSpriteReversed = SpriteSheet.grabSprite(192, 0, 64, 64);
        araneaSpriteReversed = SpriteSheet.grabSprite(64, 64, 64, 64);
        calibornSpriteReversed = SpriteSheet.grabSprite(192, 64, 64, 64);
        roseSpriteReversed = SpriteSheet.grabSprite(192, 128, 64, 64);
        roxySpriteReversed = SpriteSheet.grabSprite(64, 192, 64, 64);
        tereziSpriteReversed = SpriteSheet.grabSprite(192, 192, 64, 64);
        janeSpriteGoodReversed = SpriteSheet.grabSprite(64, 256, 64, 64);
        janeSpriteEvilReversed = SpriteSheet.grabSprite(192, 256, 64, 64);
        theFeltSpriteReversed = SpriteSheet.grabSprite(64, 320, 64, 64);
        jackNoirSpriteReversed = SpriteSheet.grabSprite(192, 320, 64, 64);
        kanayaSpriteReversed = SpriteSheet.grabSprite(64, 384, 64, 64);
        karkatSpriteReversed = SpriteSheet.grabSprite(192, 384, 64, 64);
        johnSpriteReversed = SpriteSheet.grabSprite(64, 448, 64, 64);
        jakeSpriteReversed = SpriteSheet.grabSprite(192, 448, 64, 64);
        jadeSpriteEvilReversed = SpriteSheet.grabSprite(64, 512, 64, 64);
        jadeSpriteGoodReversed = SpriteSheet.grabSprite(192, 512, 64, 64);
        condesceSpriteReversed = SpriteSheet.grabSprite(64, 576, 64, 64);
        gamzeeSpriteReversed = SpriteSheet.grabSprite(192, 576, 64, 64);

        for (int x = 0; x < 4; x++) { //Load the array of dice sprites
            die[x] = SpriteSheet.grabSprite(x * 64, 640, 64, 64);
            if (x < 2) {
                die[x + 4] = SpriteSheet.grabSprite(x * 64, 704, 64, 64);
            }
        }

        try {
            //Load all images not on the spritesheet.
            mapBackground = ImageIO.read(new File("src/resources/LOFAF.png"));
            battlebackground = ImageIO.read(new File("src/resources/battlebackground.png"));
            attackTile = ImageIO.read(new File("src/resources/Attack Tile.png"));
            attackingTile = ImageIO.read(new File("src/resources/Attacking Tile.png"));
            moveTile = ImageIO.read(new File("src/resources/Move Tile.png"));
            player1Tile = ImageIO.read(new File("src/resources/Player 1 Tile.png"));
            player2Tile = ImageIO.read(new File("src/resources/Player 2 Tile.png"));
            gamzeeTile = ImageIO.read(new File("src/resources/Gamzee Tile.png"));
            whiteTile = ImageIO.read(new File("src/resources/White Tile.png"));
            greyTile = ImageIO.read(new File("src/resources/Grey Tile.png"));
        } catch (IOException e) {
            System.out.println("Image Load Failed: " + e);
        }
        //Run the method that initializes the piecs.
        loadPieces();
    }

    @SuppressWarnings("empty-statement")
    public void loadPieces() {
        //creates all of the piece of objects

        if (newGame) {
            //If this is a new game, initialize the pieces.

            dave = new God(daveSprite, daveSpriteReversed, "Dave", 3, 7, 5, 0, 3, 1);
            john = new God(johnSprite, johnSpriteReversed, "John", 4, 7, 5, 10, 7, 1);
            rose = new God(roseSprite, roseSpriteReversed, "Rose", 3, 7, 3, 11, 4, 1);
            roxy = new God(roxySprite, roxySpriteReversed, "Roxy", 3, 7, 4, 11, 3, 1);
            jake = new God(jakeSprite, jakeSpriteReversed, "Jake", 3, 7, 1, 6, 6, 1);
            karkat = new Mortal(karkatSprite, karkatSpriteReversed, "Karkat", 3, 7, 2, 2, 7, 1);
            kanaya = new Mortal(kanayaSprite, kanayaSpriteReversed, "Kanaya", 3, 7, 3, 1, 6, 1);
            terezi = new Mortal(tereziSprite, tereziSpriteReversed, "Terezi", 3, 7, 2, 4, 7, 1);
            dirk = new God(dirkSprite, dirkSpriteReversed, "Dirk", 3, 7, 5, -1, -1, 1);

            condesce = new Mortal(condesceSprite, condesceSpriteReversed, "The Condesce", 3, 15, 7, 3, 0, 2);
            aranea = new God(araneaSprite, araneaSpriteReversed, "Aranea", 3, 10, 2, 10, 1, 2);
            jade = new God(jadeSpriteEvil, jadeSpriteEvilReversed, "Jade", 3, 7, 5, 5, 3, 2);
            jane = new God(janeSpriteEvil, janeSpriteEvilReversed, "Jane", 3, 9, 3, 6, 3, 2);
            caliborn = new God(calibornSprite, calibornSpriteReversed, "Caliborn", 3, 6, 8, -1, -1, 2);
            theFelt = new Mortal(theFeltSprite, theFeltSpriteReversed, "The Felt", 2, 12, 3, -1, -1, 2);
            jackNoir = new Mortal(jackNoirSprite, jackNoirSpriteReversed, "Jack Noir", 2, 7, 5, -1, -1, 2);

            gamzee = new Gamzee(gamzeeSprite, gamzeeSpriteReversed, "Gamzee", 1, 999, 0, 4, 5, 0);
        } else {
            //If it's not a new game it's a loaded game. In this case, only images need to be passed as to the objects (the rest of the variables are loaded in apparently).

            dave.setBufferedImage(daveSprite, daveSpriteReversed);
            john.setBufferedImage(johnSprite, johnSpriteReversed);
            rose.setBufferedImage(roseSprite, roseSpriteReversed);
            roxy.setBufferedImage(roxySprite, roxySpriteReversed);
            jake.setBufferedImage(jakeSprite, jakeSpriteReversed);
            karkat.setBufferedImage(karkatSprite, karkatSpriteReversed);
            kanaya.setBufferedImage(kanayaSprite, kanayaSpriteReversed);
            terezi.setBufferedImage(tereziSprite, tereziSpriteReversed);
            dirk.setBufferedImage(dirkSprite, dirkSpriteReversed);

            condesce.setBufferedImage(condesceSprite, condesceSpriteReversed);
            aranea.setBufferedImage(araneaSprite, araneaSpriteReversed);
            if (jade.getPlayer() == 2) {
                jade.setBufferedImage(jadeSpriteEvil, jadeSpriteEvilReversed);
            } else {
                jade.setBufferedImage(jadeSpriteGood, jadeSpriteGoodReversed);
            }
            if (jane.getPlayer() == 2) {
                jane.setBufferedImage(janeSpriteEvil, janeSpriteEvilReversed);
            } else {
                jane.setBufferedImage(janeSpriteGood, janeSpriteGoodReversed);
            }
            caliborn.setBufferedImage(calibornSprite, calibornSpriteReversed);
            theFelt.setBufferedImage(theFeltSprite, theFeltSpriteReversed);
            jackNoir.setBufferedImage(jackNoirSprite, jackNoirSpriteReversed);

            gamzee.setBufferedImage(gamzeeSprite, gamzeeSpriteReversed);

        }

        //stores the pieces in the initial board
        if (john.getHealth() > 0) {
            boardPieces[john.getY()][john.getX()] = john;
        }
        if (dave.getHealth() > 0) {
            boardPieces[dave.getY()][dave.getX()] = dave;
        }
        if (rose.getHealth() > 0) {
            boardPieces[rose.getY()][rose.getX()] = rose;
        }
        if (roxy.getHealth() > 0) {
            boardPieces[roxy.getY()][roxy.getX()] = roxy;
        }
        if (jake.getHealth() > 0) {
            boardPieces[jake.getY()][jake.getX()] = jake;
        }
        if (dirk.getHealth() > 0 && turn > 3) {
            boardPieces[dirk.getY()][dirk.getX()] = dirk;
        }

        if (karkat.getHealth() > 0) {
            boardPieces[karkat.getY()][karkat.getX()] = karkat;
        }
        if (kanaya.getHealth() > 0) {
            boardPieces[kanaya.getY()][kanaya.getX()] = kanaya;
        }
        if (terezi.getHealth() > 0) {
            boardPieces[terezi.getY()][terezi.getX()] = terezi;
        }

        if (condesce.getHealth() > 0) {
            boardPieces[condesce.getY()][condesce.getX()] = condesce;
        }
        if (aranea.getHealth() > 0) {
            boardPieces[aranea.getY()][aranea.getX()] = aranea;
        }
        if (jade.getHealth() > 0) {
            boardPieces[jade.getY()][jade.getX()] = jade;
        }
        if (jane.getHealth() > 0) {
            boardPieces[jane.getY()][jane.getX()] = jane;
        }
        if (jackNoir.getHealth() > 0 && turn > 4) {
            boardPieces[jackNoir.getY()][jackNoir.getX()] = jackNoir;
        }
        if (theFelt.getHealth() > 0 && turn > 5) {
            boardPieces[theFelt.getY()][theFelt.getX()] = theFelt;
        }
        if (caliborn.getHealth() > 0 && turn > 7) {
            boardPieces[caliborn.getY()][caliborn.getX()] = caliborn;
        }

        boardPieces[gamzee.getY()][gamzee.getX()] = gamzee;
    }

    //method that draws everything
    @Override
    public void paint(Graphics g) {
        //draws white back image to prevent flickering
        g.drawImage(image, 0, 0, WIDTH * SCALE + 50, HEIGHT * SCALE + 50, Color.white, this);
        if (!needToRevive) { //If no piece needs to be revived.
            if (battle) { //And it is battle.
                if (movesDone) { //Immediately after the players finish moving.
                    drawMainBackground(g); //Draw the main background once to let the player see its final condition.
                    movesDone = false; //Make sure this if statement isn't true again.
                } else {
                    drawBattleBackground(g); //After that first draw, draw the battle background.
                }
            } else {
                drawMainBackground(g); //If battle is false, draw the main screen.
            }
        } else {
            drawRevivalScreen(g); //If a piece needs to be revived, draw the revival screen.
        }
//        if (playerMoving == -1) {
//            drawMainBackground(g);
//        }
    }

    public void drawMainBackground(Graphics g) {
        g.setColor(Color.white);
        //draws mountain background
        g.drawImage(mapBackground, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, this);
        //draws grid for pieces
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 12; k++) {
                //draws a rectangle
                g.drawRect(k * 64, i * 64, 64, 64);
            }
        }
        //Draws coloured squares around pieces that correspond to the side they fight for.
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 12; k++) {
                //checks if a piece is there
                if (boardPieces[i][k] != null) {
                    if (boardPieces[i][k].getPlayer() == 1) {
                        g.drawImage(player1Tile, k * 64, i * 64, this); //Draw a blue tile over all of player 1's pieces.
                    } else if (boardPieces[i][k].getPlayer() == 2) {
                        g.drawImage(player2Tile, k * 64, i * 64, this); //Draw a red tile over all of player 2's pieces.
                    } else {
                        g.drawImage(gamzeeTile, k * 64, i * 64, this); //Draw a dark blue tile over Gamzee.
                    }
                    if (boardPieces[i][k] != null) {
                        //draws the sprite
                        g.drawImage((BufferedImage) (boardPieces[i][k].getSprite()), k * 64, i * 64, this);
                    }
                    //This draws a tile over pieces that have moved but are not in battle.
                    if (boardPieces[i][k] != null && !boardPieces[i][k].isInBattle() && !boardPieces[i][k].isMovable()) {
                        g.drawImage(whiteTile, k * 64, i * 64, this);
                    }
                }
            }
        }

        if (playerMoving != -1) { //Keep Looping normally as long as playerMoving isn't -1, which would mean the game is over.
            //Tell the users turn it is, what player is moving, and either how many actions are left for that player or that they are in battle.
            g.drawString("Turn: " + turn, 800, 30);
            if (selectedPiece != null && selectedPiece.getX() == -1) { //If a piece is selected and its x-coordinate is -1, then inform the player controlling it to bring it into the game.
                g.drawString("Player " + selectedPiece.getPlayer() + ", bring " + selectedPiece.getName(), 800, 48);
                g.drawString("into the game.", 800, 66);
            } else { //Otherwise display the normal information.
                if (playerMoving == 0) { //If player 0 is moving, then it is the bard's (Gamzee's) turn to move. Tell the user to move him.
                    g.drawString("Move the Bard.", 800, 48);
                } else { //Otherwise inform the user whose move it is.
                    if (playerMoving == 1) {
                        g.drawString("It is Player 1/Blue's move.", 800, 48);
                    } else if (playerMoving == 2) {
                        g.drawString("It is Player 2/Red's move.", 800, 48);
                    }
                }
                if (playerMoving == 0) {
                    if (gamzee.isBerserk()) { //If Gamzee is berserk, player 2 moves him. Otherwise player 1 moves him. Inform the user of this.
                        g.drawString("Player 2 must do this.", 800, 66);
                    } else {
                        g.drawString("Player 1 must do this.", 800, 66);
                    }
                } else if (playerMoving == 1) { //If it is player 1's move, display the number of actions they have left. Otherwise display the number of actions player 2 has left.
                    g.drawString("Actions remaining: " + player1Actions, 800, 66);
                } else {
                    g.drawString("Actions remaining: " + player2Actions, 800, 66);
                }
            }
            //Draw the 'end actions' button.
            g.drawRect(800, 80, 85, 20);
            g.drawString("End Actions", 805, 95);
            //Make a spot for the piece information.
            g.drawString("Piece Information:", 800, 130);

            //Draw the 'Save Game' button.
            g.drawRect(800, 310, 85, 20);
            g.drawString("Save Game", 805, 325);

            //Draw the 'Back to Main Menu' button.
            g.drawRect(800, 340, 125, 20);
            g.drawString("Back to Main Menu", 805, 355);

            //draws selected piece info on right of screen
            if (selectedPiece != null) {
                //gets and draws pieces info
                g.drawString("Name:     " + selectedPiece.getName(), 800, 152);
                g.drawString("Type:      " + selectedPiece.getType(), 800, 167);
                g.drawString("Attack:    " + selectedPiece.getAttack(), 800, 182);
                if (selectedPiece != null) { //This is to fix another one of those weird null pointer exceptions.
                    g.drawString("Health:    " + selectedPiece.getHealth(), 800, 197);
                }
                //Indicates the squares the piece can move to.
                for (int x = 0; x < reachable.size(); x++) {
                    if (selectedPiece != null && selectedPiece.isInBattle()) { //The first condition is to prevent a really weird error. Sometimes there is a null pointer exception here even though the piece shouldn't be null.
                        //Draw an orange square on the places a piece can attack from
                        g.drawImage(attackTile, reachable.get(x)[1] * 64, reachable.get(x)[0] * 64, this);
                    } else {
                        //Draw a yellow square on the places the piece can move to.
                        g.drawImage(moveTile, reachable.get(x)[1] * 64, reachable.get(x)[0] * 64, this);
                    }
                }
                //Draw the 'heal' button if the piece is healable.
                if (selectedPiece != null && selectedPiece.isHealable() && playerMoving != 0) { //The first condition is to prevent a really weird error. Sometimes there is a null pointer exception here even though the piece shouldn't be null.
                    g.drawRect(800, 220, 60, 20);
                    g.drawString("Heal", 815, 235);
                } else {
                    //If the selected piece isn't healable, print this message.
                    g.drawString("Cannot Heal", 800, 235);
                }
            } else { //If there is no selected piece, one can display the information for a piece with the mouse hovering over it.
                Point m;
                //gets the mouse position
                m = frame.getMousePosition();
                if (m != null) {
                    //checks if the mouse is on the screen
                    if (m.getX() < 768 && m.getY() < 534 && m.getY() > 22) {
                        //Find the coordinates that corespond to the mouse position.
                        int i = (int) Math.floor((m.getY() - 22) / 64);
                        int k = (int) Math.floor((m.getX()) / 64);
                        if (boardPieces[i][k] != null) { //If that position on the board isn't empty.
                            //Display the information for the piece.
                            g.drawString("Name:     " + boardPieces[i][k].getName(), 800, 152);
                            g.drawString("Type:      " + boardPieces[i][k].getType(), 800, 167);
                            g.drawString("Attack:    " + boardPieces[i][k].getAttack(), 800, 182);
                            g.drawString("Health:    " + boardPieces[i][k].getHealth(), 800, 197);
                        } else { //If there is no piece at this position, communicate that there is not piece to display information about.
                            g.drawString("No piece selected.", 800, 150);
                        }
                    }
                }
            }

            //Show green tiles over all the pieces in battle.
            for (int x = 0; x < 8; x++) { //Go through the board array using two for loops.
                for (int y = 0; y < 12; y++) {
                    if (boardPieces[x][y] != null && boardPieces[x][y].isInBattle()) { //If a square contains a piece and it is in battle, a draw yellow square over it.
                        g.drawImage(attackingTile, boardPieces[x][y].getX() * 64, boardPieces[x][y].getY() * 64, this);
                    }
                }
            }
        } else { //If playerMoving is -1, inform the user that the game is over and declare the winner.
            //Draw the 'Back to Main Menu' button.
            g.drawRect(800, 340, 125, 20);
            g.drawString("Back to Main Menu", 805, 355);
            g.setFont(new Font("Ariel", Font.BOLD, 18));
            g.drawString("Game over.", 800, 30); //Inform the user that the game is over.
            g.drawString("Player " + victor + " won.", 800, 55); //Inform the user who won.
            for (int x = 0; x < 8; x++) { //Draw gray tiles over the board to show that the game is over.
                for (int y = 0; y < 12; y++) {
                    g.drawImage(greyTile, y * 64, x * 64, this);
                }
            }
        }
    }

    protected int battleNumber = 0;
    protected boolean startBattle;
    protected int round = 1;
    protected boolean showReinforcements;
    protected int c1TotalAttack = 0;
    protected int c2TotalAttack = 0;
    protected Piece combatant1;
    protected Piece combatant2;

    public void drawBattleBackground(Graphics g) {

        if (battleNumber < combatants.size()) { //Loop for every battle.
            if (c1TotalAttack == 0) { //When battle first starts, wait half a second before drawing it so the users can see the battle screen before beginning to fight.
                wait(500);
            }
            combatant1 = combatants.get(battleNumber)[0]; //gets the two battling pieces
            combatant2 = combatants.get(battleNumber)[1];
            if (round == 1) { //Only run if it is round 1.
                c1TotalAttack = combatant1.getAttack(); //Calculate the initial total attack of each side.
                c2TotalAttack = combatant2.getAttack();
            }

            g.drawImage(battlebackground, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, this); //draws the battle background
            g.drawImage(combatant1.getReversedSprite(), 250, 250, 128, 128, this); //draws the two battling sprites
            g.drawImage(combatant2.getSprite(), 650, 250, 128, 128, this);
            //prints text about the battling sprites
            g.setFont(new Font("Ariel", Font.BOLD, 18));
            g.setColor(Color.white);

            if (showReinforcements) {
                //checks if there are any player 1 reinforcements
                if (reinforcements[0] != null) {
                    for (int i = 0; i < reinforcements[0].size(); i++) {
                        g.drawImage(reinforcements[0].get(i).getReversedSprite(), 100, 100 + (i * 64), this);
                    }
                }
                //checks if there are any player 2 reinforcements
                if (reinforcements[1] != null) {
                    for (int i = 0; i < reinforcements[1].size(); i++) {
                        g.drawImage(reinforcements[1].get(i).getSprite(), 870, 100 + (i * 64), this);
                    }
                }
            }

            if (!rolled && battleRoll1 != 0) { //If the dice were rolled,
                wait(1000);
                battleRoll1 = 0; //Reset the rolls for the next round.
                battleRoll2 = 0;
            } else {
                rolled = false;
            }

            if (round == 1 && startBattle == true) { //This is battle code for round 1, and it only runs after enter is pressed.
                startBattle = false; //This makes the program wait until enter is pressed again to run round 2.
                battleRoll1 = (int) ((Math.random() * 6) + 1); //Generate two random numbers from 1 to 6.
                battleRoll2 = (int) ((Math.random() * 6) + 1);
                rolled = true; //The dice were rolled. Take note of this.
                int c1Damage = (battleRoll1 + combatant1.getAttack()); //Generate the total attacks of both sides by adding the random numbers to the attacks of the actual pieces.
                int c2Damage = (battleRoll2 + combatant2.getAttack());
                dealDamage(combatant1, combatant2, c1Damage, c2Damage); //Do damage calculations.
                round++; //Go to the next round.
                if (combatant1.getHealth() < 1 || combatant2.getHealth() < 1) { //If either piece runs out of health, go to round 4 (which isn't actually a round but does contain code).
                    round = 4;
                }
            } else if (round == 2 && startBattle == true) { //For round 2 of combat.
                showReinforcements = true;
                startBattle = false;
                reinforcements = genReinforcementList(combatant1, combatant2); //Get reinforcements.
                battleRoll1 = (int) ((Math.random() * 6) + 1); //Generate the random numbers.
                battleRoll2 = (int) ((Math.random() * 6) + 1);
                rolled = true; //The dice were rolled. Take note of this.
                //Add the attacks of reinforcements to the appropriate sides. Also, update each side's total attack.
                for (int b = 0; b < reinforcements[0].size(); b++) {
                    c1TotalAttack += reinforcements[0].get(b).getAttack();
                }
                for (int c = 0; c < reinforcements[1].size(); c++) {
                    c2TotalAttack += reinforcements[1].get(c).getAttack();
                }
                int c1Damage = battleRoll1 + c1TotalAttack; //Calculate the attacks.
                int c2Damage = battleRoll2 + c2TotalAttack;
                dealDamage(combatant1, combatant2, c1Damage, c2Damage); //Deal with damage calculations.
                round++; //Go to the next round.
                if (combatant1.getHealth() < 1 || combatant2.getHealth() < 1) { //If either main piece has run out of health, go to round 4.
                    round = 4;
                }
            } else if (round == 3 && startBattle == true) { //For round 3 of combat.
                startBattle = false;
                battleRoll1 = (int) ((Math.random() * 6) + 1); //Calculate the random numbers.
                battleRoll2 = (int) ((Math.random() * 6) + 1);
                rolled = true; //The dice were rolled. Take note of this.
                int c1Damage = (battleRoll1 + c1TotalAttack); //Calculate the attacks.
                int c2Damage = (battleRoll2 + c2TotalAttack);
                dealDamage(combatant1, combatant2, c1Damage, c2Damage); //Deal with damage calculations
                round++; //Go to the next round. There is no need to check if either piece is out of health because it is round 4 anyway.
            } else if (round == 4) {
                if (reinforcements != null) { //This is to stop a weird null pointer exception.
                    for (int x = 0; x < reinforcements[0].size(); x++) { //Make all the pieces that were reinforcements not in battle any more.
                        reinforcements[0].get(x).inBattle = false;
                    }
                    for (int x = 0; x < reinforcements[1].size(); x++) { //Make all the pieces that were reinforcements not in battle any more.
                        reinforcements[1].get(x).inBattle = false;
                    }
                    for (int a = reinforcements[0].size() - 1; a >= 0; a--) { //Reset the array holding the pieces that are reinforcements.
                        reinforcements[0].remove(a);
                    }
                    for (int b = reinforcements[1].size() - 1; b >= 0; b--) {
                        reinforcements[1].remove(b);
                    }
                }
                wait(2000); //Wait so as to draw the final shot of the battle ending before the revival screen is drawn.
                if (combatant1 != null && combatant1.getHealth() < 1) { //If combatant 1 is out of health.
                    if (combatant1.getType().equals("God") && combatant1.maxHealth > 2) { //And if they are a god with maximum health greater than 2.
                        needToRevive = true; //Then a piece needs to be revived and it is this piece.
                        mustRevive = combatant1;
                    } else {
                        JOptionPane.showMessageDialog(null, combatant1.getName() + " was killed.");
                        destroyPiece(combatant1); //If this piece isn't a god or doesn't have a maximum health greater than one, destroy the piece.
                    }
                } else if (combatant1 != null && combatant2.getHealth() < 1) { //If combatant 2 is out of health.
                    if (combatant2.getType().equals("God") && combatant2.maxHealth > 2) { //And if they are a god with maximum health greater than 2.
                        needToRevive = true; //Then a piece needs to be revived and it is this piece.
                        mustRevive = combatant2;
                    } else {
                        destroyPiece(combatant2);//If this piece isn't a god or doesn't have a maximum health greater than one, destroy the piece.
                    }
                }
                battleNumber++; //Go to the next battle.
                round = 1; //Reset the round number for the next battle.
            }

            if (battleRoll1 != 0) { //If the dice have been rolled.
                g.drawString("Total: " + (battleRoll1 + c1TotalAttack), 295, 200); //Draw the total attacks for each side.
                g.drawString("Total: " + (battleRoll2 + c2TotalAttack), 655, 200);
                if ((battleRoll1 + c1TotalAttack) < (battleRoll2 + c2TotalAttack - 1)) { //If player 1 lost the battle, draw the amount of damage they took in red above their primary combatant.
                    g.setColor(Color.red);
                    g.drawString("" + (int) Math.floor(((battleRoll1 + c1TotalAttack) - (battleRoll2 + c2TotalAttack)) / 2), 300, 250);
                } else if ((battleRoll1 + c1TotalAttack - 1) > (battleRoll2 + c2TotalAttack)) { //If player 2 lost the battle, draw the amount of damage they took in red above their primary combatant.
                    g.setColor(Color.red);
                    g.drawString("" + (int) Math.floor(((battleRoll2 + c2TotalAttack) - (battleRoll1 + c1TotalAttack)) / 2), 700, 250);
                } else { //If there was a tie in battle, inform the user about this.
                    g.drawString("Tie!", 460, 250);
                }
            }

            g.setColor(Color.black); //Make the graphics object draw in black again.
            //Draw the name, type, attack, and health of both primary combatants.
            g.drawString(combatant1.getName(), 15, 55);
            g.drawString("Type: " + combatant1.getType(), 15, 345);
            g.drawString("Attack: " + combatant1.getAttack(), 15, 370);
            g.drawString("Health: " + combatant1.getHealth(), 15, 395);
            g.drawString(combatant2.getName(), 890, 55);
            g.drawString("Type: " + combatant2.getType(), 860, 345);
            g.drawString("Attack: " + combatant2.getAttack(), 860, 370);
            g.drawString("Health: " + combatant2.getHealth(), 860, 395);
            g.drawString("Total Attack: " + c1TotalAttack, 185, 465); //Draw the total attacks of both combatants.
            g.drawString("Total Attack: " + c2TotalAttack, 700, 465);

            g.setColor(Color.white); //Make the graphics object draw in white.
            if (round != 4) { //If it's not the fourth round, draw the round number in the top of the center of the screen.
                g.drawString("Round " + round, (WIDTH * SCALE) / 2 - 50, 50);
                g.drawString("Press Enter to stop the dice roll.", (WIDTH * SCALE) / 2 - 135, 80); //Also tell the user how to stop the dice.
            }

            wait(50); //Wait so the dice don't flicker so much.
            if (battleRoll1 == 0) { //If the dice are still at their initial value of 0.
                //Generate two random numbers and draw the two dice using the random numbers to pick which side is drawn.
                int randomNum1 = (int) (Math.random() * 6);
                g.drawImage(die[randomNum1], 300, 100, this);
                int randomNum2 = (int) (Math.random() * 6);
                g.drawImage(die[randomNum2], 660, 100, this);
            } else { //And if the dice are not at their initial value (meaning they've been rolled).
                g.drawImage(die[battleRoll1 - 1], 300, 100, this); //Draw the sides of the dice displaying the numbers rolled.
                g.drawImage(die[battleRoll2 - 1], 660, 100, this);
            }
        } else { //This code runs after the battles.
            c1TotalAttack = 0; //Reset the total attacks.
            c2TotalAttack = 0;
            combatants.clear(); //Reset the list of combatants for the next round of battles.
            battle = false; //Now that every battle has been run, battle is over.
            battleNumber = 0;
            endTurn(); //Now that all battles are over, end the turn.
        }
    }

    //A method for handling damage calculations after battle.
    public void dealDamage(Piece combatant1, Piece combatant2, int c1Damage, int c2Damage) {
        int damageDealt;
        if (c1Damage > c2Damage) { //If the first side has a higher attack, deal damage to the main piece of the second side.
            damageDealt = (c1Damage - c2Damage) / 2;
            combatant2.setHealth(combatant2.getHealth() - damageDealt);
        } else if (c2Damage > c1Damage) { //If the second side has a higher attack, deal damage to the main piece of the first side.
            damageDealt = (c2Damage - c1Damage) / 2;
            combatant1.setHealth(combatant1.getHealth() - damageDealt);
        }
    }

    transient protected BufferedImage clock = null;

    //Draw the screen for reviving gods, which also runs the logic for doing the actual reviving or destroying of the piece.
    public void drawRevivalScreen(Graphics g) {
        if (clock == null) {
            try {
                //If the clock image hasn't been loaded yet, try to load it.
                clock = ImageIO.read(new File("src/resources/Clock.png"));
            } catch (IOException e) {
                System.out.println("Image Load Failed: " + e);
            }
        }
        //Draw the clock and text in white font displaying headings over the dice.
        g.drawImage(clock, 0, 0, 1040, 530, this);
        g.setFont(new Font("Ariel", Font.BOLD, 16));
        g.setColor(Color.white);
        g.drawString(mustRevive.getName() + " must be revived. Click the dice to stop them.", 290, 35); //Display the name of a piece that needs to be revived.
        g.drawString("First Roll", 180, 175); //Draw text in the buttons.
        g.drawString("Second Roll", 700, 175);
        if (revivalRoll1 != 0) { //If the first die has been rolled, display what the roll was.
            g.drawImage(die[revivalRoll1 - 1], 180, 250, this);
        } else { //Otherwise, draw a random side of the dice.
            int randomNum1 = (int) (Math.random() * 6);
            g.drawImage(die[randomNum1], 180, 250, this);
        }
        if (revivalRoll2 != 0) { //If the second die has been rolled, display what the roll was.
            g.drawImage(die[revivalRoll2 - 1], 710, 250, this);
        } else { //Otherwise, draw a random side of the dice.
            int randomNum2 = (int) (Math.random() * 6);
            g.drawImage(die[randomNum2], 710, 250, this);
        }
        if (revivalRoll1 != 0 && revivalRoll2 != 0) { //If both dice have been rolled, run this code.
            if (!rolled) {
                //If the rolls are both not 0 (both dice have been rolled), then wait 1 second to display the results.
                wait(1000);
                if ((revivalRoll1 + revivalRoll2) > 6) { //If the dice rolls add up to more than 6, the god is revived with reduced health.
                    JOptionPane.showMessageDialog(null, mustRevive.getName() + " was revived!\n(" + revivalRoll1 + " + " + revivalRoll2 + " > 6)"); //Inform the player of the result
                    mustRevive.maxHealth -= 2; //Decrease the piece's maximum health.
                    mustRevive.setHealth(mustRevive.maxHealth); //Reset the piece's health.
                    if (mustRevive == jane || mustRevive == jade && mustRevive.getPlayer() == 2) { //If Jane or Jade is killed and revived, make them change sides.
                        mustRevive.player = 1;
                        if (mustRevive == jane) { //Change the sprites to the good sprites.
                            mustRevive.sprite = janeSpriteGood;
                            mustRevive.reversedSprite = janeSpriteGoodReversed;
                        } else {
                            mustRevive.sprite = jadeSpriteGood;
                            mustRevive.reversedSprite = jadeSpriteGoodReversed;
                        }
                    }
                    mustRevive = null; //There is no longer a piece that needs to be revived.
                    needToRevive = false; //The program no longer needs to revive a piece.
                } else {
                    JOptionPane.showMessageDialog(null, mustRevive.getName() + " was not revived.\n(" + revivalRoll1 + " + " + revivalRoll2 + " <= 6)"); //Inform the player that the piece was not revived.
                    destroyPiece(mustRevive); //Destroy the piece by removing it from the board.
                    mustRevive = null; //There is no longer a piece that needs to be revived.
                    needToRevive = false; //The program no longer needs to revive a piece.
                }
                revivalRoll1 = 0; //Reset the dice rolls for the next god revival.
                revivalRoll2 = 0;
            } else {
                rolled = false; //If the dice have just been rolled, do not run the regular code. Instead run this once. I am not entirely sure why this code is here but I don't care.
            }
        }
        //Wait so the dice animation doesn't flicker so much.
        wait(50);
    }

    //***Does NOT take terrain into account***
    public ArrayList<Piece>[] genReinforcementList(Piece attackingPiece1, Piece attackingPiece2) {
        //declare required variables
        boolean useAsReinforcement;
        //hold the 2 pieces in an array; easier to manipulate
        Piece[] attackingPieces = {attackingPiece1, attackingPiece2};

        //declare and initialise array to hold 2 Piece type arrayLists; [<player1Pieces>, <player2Pieces>)]
        ArrayList<Piece>[] reinforcementList = new ArrayList[2];
        //initialise the 2 array lists to hold Piece type objects
        reinforcementList[0] = new ArrayList<Piece>();
        reinforcementList[1] = new ArrayList<Piece>();

        //iterate over each piece
        for (int i = 0; i < attackingPieces.length; i++) {
            //***Remember to declare and initialise all arrays (reference variables) for each piece within
            // the for loop***
            //figure out coordinates of attacking piece
            int[] attackPos = {attackingPieces[i].getY(), attackingPieces[i].getX()}; //(Row, Column)

            //declare and initialise arrays to hold possible positions for reinforcements
            int leftPos[] = new int[2], rightPos[] = new int[2], downPos[] = new int[2], upPos[] = new int[2];
            int possibleReinforcementList[][] = {leftPos, rightPos, downPos, upPos};

            //figure out coordinates of tiles on all 4 sides of attackPos
            leftPos[0] = attackPos[0];
            leftPos[1] = attackPos[1] - 1;

            rightPos[0] = attackPos[0];
            rightPos[1] = attackPos[1] + 1;

            downPos[0] = attackPos[0] + 1;
            downPos[1] = attackPos[1];

            upPos[0] = attackPos[0] - 1;
            upPos[1] = attackPos[1];

            for (int j = 0; j < possibleReinforcementList.length; j++) {
                //check if each position is within bounds of board and is held by a piece other than the
                //attacking pieces
                if (possibleReinforcementList[j][0] >= 0 && possibleReinforcementList[j][0] < boardPieces.length && possibleReinforcementList[j][1] >= 0 && possibleReinforcementList[j][1] < boardPieces[0].length && boardPieces[possibleReinforcementList[j][0]][possibleReinforcementList[j][1]] != null && boardPieces[possibleReinforcementList[j][0]][possibleReinforcementList[j][1]] != attackingPiece1 && boardPieces[possibleReinforcementList[j][0]][possibleReinforcementList[j][1]] != attackingPiece2) {
                    Piece possibleReinforcement = boardPieces[possibleReinforcementList[j][0]][possibleReinforcementList[j][1]];
                    if (possibleReinforcement.inBattle) {        //no need to prompt user again
                        //add piece to correct position in array
                        reinforcementList[possibleReinforcement.getPlayer() - 1].add(possibleReinforcement);

                    } //make sure user has not already been prompted for choosing piece as reinforcement
                    else if (possibleReinforcement.canReinforce && possibleReinforcement.getPlayer() != 0) { //The second condition is to make sure Gamzee doesn't enter as a reinforcement when he isn't berserk.
                        //ask user if he/she wants to deploy piece as reinforcement
                        useAsReinforcement = promptForReinforcement(possibleReinforcement);

                        if (useAsReinforcement) {
                            //add piece to correct position in array
                            reinforcementList[possibleReinforcement.getPlayer() - 1].add(possibleReinforcement);
                            //bring piece into battle
                            possibleReinforcement.inBattle = true;
                        } else {
                            //don't check this piece again
                            possibleReinforcement.canReinforce = false;
                        }
                    }
                }
            }
        }
        return reinforcementList;
    }

    protected static boolean promptForReinforcement(Piece p) {
        int result = JOptionPane.showConfirmDialog(null, "Player " + p.getPlayer() + ", Would you like to use " + p.getName().toUpperCase() + " as Reinforcement?", "Deploy Reinforcement", JOptionPane.YES_NO_OPTION);
        if (result == 0) {
            return true;
        } else {
            return false;
        }

    }

    //runs when player clicks the exit button
    @Override
    public void windowClosing(WindowEvent evt) {
        if (!saved) {
            int confirmation = JOptionPane.showConfirmDialog(null, "Would you like to Save your game progress before you exit?", "Save Game", JOptionPane.YES_NO_OPTION);
            if (confirmation == 0) {

                String fileName = JOptionPane.showInputDialog("Please enter a Name:\n(Enter same name to overwrite existing file)");
                if (fileName != null) {
                    fileName = fileName.replaceAll("[^a-zA-Z0-9\\s]+", ""); //take care of illegal special characters
                    fileName = fileName.toUpperCase();
                    save(fileName);
                }
            }

        }
        new StartScreen().setVisible(true);
        frame.dispose();

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:

                break;

            case KeyEvent.VK_DOWN:

                break;

            case KeyEvent.VK_LEFT:

                break;

            case KeyEvent.VK_RIGHT:

                break;

            case KeyEvent.VK_ENTER:
                if (battle && !needToRevive) { //If pieces are in battle and none need to be revived, begin the next round of battle.
                    startBattle = true;
                }
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:

                break;

            case KeyEvent.VK_DOWN:

                break;

            case KeyEvent.VK_LEFT:

                break;
            case KeyEvent.VK_RIGHT:

                break;

        }

    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void windowOpened(WindowEvent we) {
    }

    @Override
    public void windowClosed(WindowEvent we) {
    }

    @Override
    public void windowIconified(WindowEvent we) {
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
    }

    @Override
    public void windowActivated(WindowEvent we) {
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
    }

    //run when the player clicks the mouse
    @Override
    public void mouseClicked(MouseEvent me) {
        Point m;
        //gets the mouse position
        m = frame.getMousePosition();
        //checks if null
        if (m != null) {
            if (!battle && !needToRevive) {
                clickOnScreenWithBoard(m); //Draw the main screen if there is no battle and no piece needs to be revived.
            } else if (needToRevive) {
                clickOnRevivalScreen(m); //Otherwise if a piece does need to be revived, draw the revival screen.
            }
        }
    }

    public void clickOnScreenWithBoard(Point m) {
        //If the user clicked where the heal button was supposed to be while there is a selected piece that can be healed, then heal the piece, take note that an action was used, and set the selected piece to null. Also, Gamzee can never be healed (so don't run if the playerMoving is 0).
        if (m.getX() <= 860 && m.getX() >= 800 && m.getY() >= 240 && m.getY() <= 265 && selectedPiece != null && selectedPiece.isHealable() && playerMoving != 0) {
            selectedPiece.heal();
            selectedPiece = null;
            actionWasUsed();
        }
        //This code is for if the user clicks the end action button. It cannot be clicked when the game is over.
        if (m.getX() <= 885 && m.getX() >= 800 && m.getY() >= 102 && m.getY() <= 122 && playerMoving != -1) {
            if (selectedPiece != null && selectedPiece.getX() == -1) {
                return;
            }
            if (playerMoving == 1) {
                player1Actions = 0; //Make player 1 run out of actions.
                selectedPiece = null; //The selected piece must be set to null.
            } else if (playerMoving == 2) {
                player2Actions = 0; //Make player 2 run out of actions.
                selectedPiece = null; //The selected piece must be set to null.
            } else {
                if (combatants != null) { //If there are pieces in the array of pieces to fight, make battle start.
                    battle = true;
                }
                movesDone = true; //Take note that the movement phase is now over.
            }
            if (player1Actions == 0 && player2Actions == 0) { //If both players are out of actions, the bard (Gamzee) moves.
                playerMoving = 0;
                theBardMoves();
            } else if (player1Actions == 0) { //If only player 1 is out of actions, player 2 moves.
                playerMoving = 2;
            } else if (player2Actions == 0) { //If only player 2 is out of actions, player 1 moves.
                playerMoving = 1;
            }
        } //Save button hit detection. The save button cannot be clicked if the game is over.
        else if (m.getX() <= 885 && m.getX() >= 800 && m.getY() >= 332 && m.getY() <= 352 && playerMoving != -1) {
            String fileName = JOptionPane.showInputDialog("Please enter a Name:\n(Enter same name to overwrite existing file)");
            if (fileName != null) {
                fileName = fileName.replaceAll("[^a-zA-Z0-9\\s]+", ""); //take care of illegal special characters
                fileName = fileName.toUpperCase();
                save(fileName);
            }
        } //back to main menu button
        else if (m.getX() <= 918 && m.getX() >= 800 && m.getY() >= 352 && m.getY() <= 382) {
            if (!saved && playerMoving != -1) { //If the game hasn't been saved and isn't over, ask the user if they want to save it.
                int confirmation = JOptionPane.showConfirmDialog(null, "Would you like to Save your game progress before you exit?", "Save Game", JOptionPane.YES_NO_OPTION);
                if (confirmation == 0) {

                    String fileName = JOptionPane.showInputDialog("Please enter a Name:\n(Enter same name to overwrite existing file)");
                    if (fileName != null) {
                        fileName = fileName.replaceAll("[^a-zA-Z0-9\\s]+", ""); //take care of illegal special characters
                        fileName = fileName.toUpperCase();
                        save(fileName);
                    }
                }

            }
            new StartScreen().setVisible(true);
            frame.dispose();
            System.out.println("Saving");

        }
        //checks if its on the screen
        if (m.getX() < 768 && m.getY() < 532) {
            //loops to check what square was clciked
            for (int i = 0; i < 8; i++) {
                for (int k = 0; k < 12; k++) {
                    if (m.getX() >= k * 64 && m.getX() <= (k + 1) * 64 && m.getY() - 32 >= i * 64 && m.getY() - 32 <= (i + 1) * 64) {
                        clickOnBoard(i, k);
                    }
                }
            }
        }
    }

    //When the user clicks on the board, run this code.
    public void clickOnBoard(int i, int k) {
        //If the selected piece is Gamzee, go to the code meant specifically for him.
        if (selectedPiece == gamzee) {
            moveGamzee(i, k);
            return;
        }
        //If a piece is being brought in, run the code specific for a piece being brought in.
        if (selectedPiece != null && selectedPiece.getX() == -1) {
            bringPieceIn(i, k);
            return;
        }
        if (selectedPiece != null && !canGetTo(i, k)) { //If there is a piece selected but the spot clicked cannot be reached.
            if (k != selectedPiece.getX() || i != selectedPiece.getY()) { //And if the spot clicked isn't the spot the selected piece is on.
                //Then set the selected piece to null and undo any battle it was set to participate in.
                if (selectedPiece.isInBattle() && selectedPiece.isMovable()) {
                    undoBattleStart(selectedPiece);
                }
                selectedPiece = null;
            }
        }
        if (selectedPiece == null) { //If there is no selected piece.
            if (boardPieces[i][k] != null) { //And if the clicked on the board isn't empty.
                //And if the piece selected hasn't been moved, healed, or been engaged in battle this turn.
                if (!boardPieces[i][k].isMovable() || boardPieces[i][k].isInBattle()) {
                    return;
                }
                //And if the piece selected belongs to the player currently moving.
                if (boardPieces[i][k].getPlayer() != playerMoving) {
                    return;
                }
                //Then make the piece clicked on the selected piece.
                selectedPiece = boardPieces[i][k];
                //Highlight all positions this piece can move to.
                showAllReachable();
            }
        } else if (selectedPiece == boardPieces[i][k]) { //If there is a selected piece and the user clicked on it a second time.
            //Then set the selected piece to null and undo any battle it was set to participate in.
            if (selectedPiece.isInBattle() && selectedPiece.isMovable() && !canGetTo(i, k)) { //The last condition in here makes sure that if the piece is attacking from where it stands, it will not be unselected.
                undoBattleStart(selectedPiece);
            } else if (canGetTo(i, k)) {
                selectedPiece = null;
                actionWasUsed();
            }
        } else if (boardPieces[i][k] == null) { //If there is a selected piece and then the user clicks on an empty square.
            //Then transfer the selected piece to the position in the board array that corresponds to the spot clicked.
            boardPieces[i][k] = selectedPiece;
            boardPieces[selectedPiece.getY()][selectedPiece.getX()] = null;
            selectedPiece.move(k, i);
            //Set the selected piece to null.
            selectedPiece = null;
            //Take note that an action was used.
            actionWasUsed();
        } else if (selectedPiece.getPlayer() == boardPieces[i][k].getPlayer()) { //If there is already a selected piece but the player clicks on another piece they control.
            //Then change the selected piece to the one being clicked.
            selectedPiece = boardPieces[i][k];
            //Highlight all positions this piece can move to.
            showAllReachable();
        } else if (selectedPiece.getPlayer() != boardPieces[i][k].getPlayer()) { //If there is already a selected piece but the player clicks on a piec the other player controls.
            //Then the pieces are in battle together. Take not of this.
            selectedPiece.inBattle = true;
            boardPieces[i][k].inBattle = true;
            //Show all the approaches from which the attacking piece could assault the defender.
            showBattleApproaches(boardPieces[i][k]);
            Piece[] pairOfCombatants = new Piece[2];
            if (selectedPiece.getPlayer() == 1) { //
                pairOfCombatants[0] = selectedPiece;
                pairOfCombatants[1] = boardPieces[i][k];
            } else {
                pairOfCombatants[1] = selectedPiece;
                pairOfCombatants[0] = boardPieces[i][k];
            }
            combatants.add(pairOfCombatants); //Add the selected piece and the piece clicked on to the array of fighting pieces.
        }
    }

    //This code runs when the user clicks on the screen for reviving gods.
    public void clickOnRevivalScreen(Point m) {
        if (m.getX() >= 100 && m.getX() <= 300 && m.getY() >= 100 && m.getY() <= 400 && revivalRoll1 == 0) { //If the user clicks on the first dice roll area and the dice hasn't been rolled yet, generate a dice roll value.
            revivalRoll1 = (int) (Math.random() * 6 + 1);
            rolled = true;
        } else if (m.getX() >= 700 && m.getX() <= 900 && m.getY() >= 100 && m.getY() <= 400 && revivalRoll2 == 0) { //If the user clicks on the second dice roll area and the dice hasn't been rolled yet, generate a dice roll value.
            revivalRoll2 = (int) (Math.random() * 6 + 1);
            rolled = true;
        }
    }

    //The code specific to moving Gamzee around (he has his own phase in which he must move).
    public void moveGamzee(int i, int k) {
        if (!canGetTo(i, k)) { //If the spot clicked cannot be reached.
            if (k != selectedPiece.getX() || i != selectedPiece.getY()) { //And if the spot clicked isn't the spot Gamzee is on.
                //Then don't freaking do anything because Gamzee can't move there. Don't let him get unselected because everything goes to heck.
                return;
            }
        }
        if (boardPieces[i][k] == null) { //If the user clicks on an empty square.
            //Then transfer the selected piece to the position in the board array that corresponds to the spot clicked.
            boardPieces[i][k] = selectedPiece;
            boardPieces[selectedPiece.getY()][selectedPiece.getX()] = null;
            selectedPiece.move(k, i);
            //Set the selected piece to null.
            selectedPiece = null;
            if (combatants != null) { //After the bard (Gamzee) moves, the part of the turn involving movement is over. If there are pieces in the array of pieces to fight, make battle start.
                battle = true;
            }
            movesDone = true; //Take note that the movement phase is now over.
        } else if (boardPieces[i][k].getPlayer() == 1) { //If the player clicks on one of player 1's pieces, they can attack them with Gamzee.
            //Then the pieces are in battle together. Take not of this.
            selectedPiece.inBattle = true;
            boardPieces[i][k].inBattle = true;
            //Show all the approaches from which the attacking piece could assault the defender.
            showBattleApproaches(boardPieces[i][k]);
            Piece[] pairOfCombatants = new Piece[2];
            if (selectedPiece.getPlayer() == 1) {
                pairOfCombatants[0] = selectedPiece;
                pairOfCombatants[1] = boardPieces[i][k];
            } else {
                pairOfCombatants[1] = selectedPiece;
                pairOfCombatants[0] = boardPieces[i][k];
            }
            combatants.add(pairOfCombatants); //Add the selected piece and the piece clicked on to the array of fighting pieces.
        } else if (boardPieces[i][k] == gamzee && gamzee.isInBattle() && canGetTo(i, k)) { //If the player clicks on Gamzee and he is in battle and the square he is standing on is a valid one to attack from, then make the selected piece null and end the turn since he can attack from here and doesn't need to move.
            selectedPiece = null;
            if (combatants != null) { //If there are pieces in the array of pieces to fight, make battle start.
                battle = true;
            }
        }
    }

    //The code for bringing a piece onto the board.
    public void bringPieceIn(int i, int k) {
        if (canGetTo(i, k)) { //If the piece can get to the square the player clicked on.
            selectedPiece.setY(i); //Then set its x and y coordinates to those of that square.
            selectedPiece.setX(k);
            boardPieces[i][k] = selectedPiece; //Make the square on the board hold the piece.
            selectedPiece = null; //Make the selected piece null.
        }
    }

    //This code executes when a piece is healed or moved.
    public void actionWasUsed() {
        //Decrement the number of actions of the player who performed an action.
        if (playerMoving == 1) {
            player1Actions--;
        } else if (playerMoving == 2) {
            player2Actions--;
        }
        //If player 1 is out of actions, make it player 2's turn.
        if (player1Actions == 0 && player2Actions == 0) { //If both players have finished their actions, then it is time for the bard (Gamzee) to move.
            playerMoving = 0;
            theBardMoves();
        } else if (player1Actions == 0) { //If only player 1 has finished their actions, it is player 2's move.
            playerMoving = 2;
        } else if (player2Actions == 0) { //If only player 2 has finished their actions, it is player 1's move.
            playerMoving = 1;
        }

    }

    //Undo a battle involving the selected piece when the attacking piece is unselected.
    public void undoBattleStart(Piece selectedPiece) {
        for (int x = 0; x < combatants.size(); x++) { //Loop once for every spot in the array.
            if (combatants.get(x)[0] == selectedPiece || combatants.get(x)[1] == selectedPiece) { //When the spot containing the selected piece is found,
                combatants.get(x)[0].inBattle = false; //take note that the piece are no longer in battle
                combatants.get(x)[1].inBattle = false;
                combatants.remove(x); //And remove their spot in the array of attacking pieces.
            }
        }
    }

    //Show the squares from which one piece can attack another.
    public void showBattleApproaches(Piece attacked) {
        int[] attackingPosition = {selectedPiece.getY(), selectedPiece.getX()};
        int[] attackedPosition = {attacked.getY(), attacked.getX()};
        reachable = genAttackSideList(attackedPosition, attackingPosition, selectedPiece.getMoves(), boardPieces);
    }

    //This method is for marking all squares that a selected piece can move to.
    public void showAllReachable() {
        int[] coordinates = {selectedPiece.getY(), selectedPiece.getX()};
        reachable = genDestinationList(coordinates, selectedPiece.getMoves(), selectedPiece.getPlayer(), boardPieces);
    }

    public boolean canGetTo(int y, int x) {
        for (int a = 0; a < reachable.size(); a++) {
            if (reachable.get(a)[0] == y && reachable.get(a)[1] == x) {
                return true;
            }
        }
        return false;
    }

    //returns an ArrayList (of int[] arrays) containing all possible positions that can be reached from the initial position, 
    //given the number of moves the piece if capable of moving in a turn  
    //***To maintain consistenty with the indexing format of 2D arrays (row, column), 
    //      COORDINATES FOR POSITION OF A PIECE ON THE BOARD SHOULD BE GIVEN IN THE FORM (Y, X)***  
    public ArrayList<int[]> genDestinationList(int initialPos[], int moves, int player, Piece board[][]) {
        //declare and initialise required variables
        int diff;
        ArrayList<int[]> possibleDestinations = new ArrayList<int[]>();

        //iterate over all tiles in board--> all possible positions
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int[] finalPos = {i, j};    //make sure to declare & initialise a new array every time (reference variable)
                diff = compDiff(initialPos, finalPos);  //compute distance (number of tiles) between finalPos & initialPos

                //make sure destination tile is either empty or contains an enemy piece and is accessible
                //***EACH PIECE OBJECT SHOULD HAVE AN ATTRIBUTE "int player" (1 OR 2) AND AN ACCESSOR ".getplayer()"***
                //***NOT CHECKING FOR TERRAIN***\
                if ((board[finalPos[0]][finalPos[1]] == null || board[finalPos[0]][finalPos[1]].getPlayer() != player && board[finalPos[0]][finalPos[1]].inBattle == false) && diff <= moves && (finalPos[0] != initialPos[0] || finalPos[1] != initialPos[1])) {
                    if (determineAccessibility(initialPos, finalPos, moves, board) == true) {
                        //add position to destination list
                        possibleDestinations.add(finalPos);
                    }
                }
            }
        }
        return possibleDestinations;
    }

    //private helper method--> returns true if finalPosition is reachable from initialPosition, false otherwise
    //***To maintain consistenty with the indexing format of 2D arrays (row, column), 
    //      COORDINATES FOR POSITION OF A PIECE ON THE BOARD SHOULD BE GIVEN IN THE FORM (Y, X)*** 
    private boolean determineAccessibility(int initialPos[], int finalPos[], int moves, Piece board[][]) {
        //Make some stupid initial conditions for Gamzee because he is special.
        if (selectedPiece == gamzee && board[finalPos[0]][finalPos[1]] != null && !gamzee.isBerserk()) { //If the selected piece is Gamzee and the place he's trying to get to is occupied and he isn't berserk, he cannot get to that place. Therefore return false.
            return false;
        }
        if (board[finalPos[0]][finalPos[1]] == gamzee && selectedPiece != gamzee) { //Gamzee can't be attacked. He just can't. The second condition is added so that Gamzee can attack from the square he is in.
            return false;
        }

        if (initialPos[0] == finalPos[0] && initialPos[1] == finalPos[1]) { //base case--> piece has reached destination
            return true;
        } else {      //inductive case
            //declare and initialise required variables
            //for each path taken (approaching from the left/right/up/down), have a new copy of moves--> trying out one path 
            //should not affect the number of available moves left for the piece for another path
            int leftMoves = moves, rightMoves = moves, upMoves = moves, downMoves = moves;

            //figure out minimum number of moves required to reach destination
            int diff = compDiff(initialPos, finalPos);

            //try moving to each adjacent tile if it leads to the destination tile (and is unoccupied)
            int upPos[] = {(initialPos[0] - 1), initialPos[1]};
            int downPos[] = {(initialPos[0] + 1), initialPos[1]};
            int rightPos[] = {(initialPos[0]), initialPos[1] + 1};
            int leftPos[] = {(initialPos[0]), initialPos[1] - 1};

            //LEFT
            if (leftPos[0] >= 0 && leftPos[0] < board.length && leftPos[1] >= 0 && leftPos[1] < board[0].length && compDiff(leftPos, finalPos) < moves && (board[leftPos[0]][leftPos[1]] == null || (leftPos[0] == finalPos[0] && leftPos[1] == finalPos[1]))) {
                leftMoves--;    //decrement number of moves left
                if (determineAccessibility(leftPos, finalPos, leftMoves, board)) { //recursive call
                    return true;
                }
            }

            //RIGHT
            if (rightPos[0] >= 0 && rightPos[0] < board.length && rightPos[1] >= 0 && rightPos[1] < board[0].length && compDiff(rightPos, finalPos) < moves && (board[rightPos[0]][rightPos[1]] == null || (rightPos[0] == finalPos[0] && rightPos[1] == finalPos[1]))) {
                rightMoves--;    //decrement number of moves left
                if (determineAccessibility(rightPos, finalPos, rightMoves, board)) { //recursive call
                    return true;
                }
            }

            //UP
            if (upPos[0] >= 0 && upPos[0] < board.length && upPos[1] >= 0 && upPos[1] < board[0].length && compDiff(upPos, finalPos) < moves && (board[upPos[0]][upPos[1]] == null || (upPos[0] == finalPos[0] && upPos[1] == finalPos[1]))) {
                upMoves--;    //decrement number of moves left
                if (determineAccessibility(upPos, finalPos, upMoves, board)) { //recursive call
                    return true;
                }
            }

            //DOWN
            if (downPos[0] >= 0 && downPos[0] < board.length && downPos[1] >= 0 && downPos[1] < board[0].length && compDiff(downPos, finalPos) < moves && (board[downPos[0]][downPos[1]] == null || (downPos[0] == finalPos[0] && downPos[1] == finalPos[1]))) {
                downMoves--;    //decrement number of moves left
                if (determineAccessibility(downPos, finalPos, downMoves, board)) { //recursive call
                    return true;
                }
            }

            return false;   //no possible path to destination tile
        }
    }

    //private helper method; returns the distance (in terms of horizontal/vertical paths; NOT diagonal/straight line distance)
    //between a piece's current position on the board and a given destination.
    private static int compDiff(int[] currentPos, int[] finalPos) {
        return Math.abs(finalPos[0] - currentPos[0]) + Math.abs(finalPos[1] - currentPos[1]);
    }

    //returns an ArrayList (of int[] arrays) containing all possible sides (tiles adjacent to finalPos) from which a piece
    //can attack another piece (maximum 4) 
    //***To maintain consistenty with the indexing format of 2D arrays (row, column), 
    //      COORDINATES FOR POSITION OF A PIECE ON THE BOARD SHOULD BE GIVEN IN THE FORM (Y, X)*** 
    public ArrayList<int[]> genAttackSideList(int finalPos[], int initialPos[], int moves, Piece[][] board) {
        //declare and initialse arrayList
        ArrayList<int[]> finalAttackSideList = new ArrayList<int[]>();
        int leftPos[] = new int[2], rightPos[] = new int[2], downPos[] = new int[2], upPos[] = new int[2];
        int possibleAttackSideList[][] = {leftPos, rightPos, downPos, upPos};
        leftPos[0] = finalPos[0] - 1;
        leftPos[1] = finalPos[1];

        rightPos[0] = finalPos[0] + 1;
        rightPos[1] = finalPos[1];

        downPos[0] = finalPos[0];
        downPos[1] = finalPos[1] + 1;

        upPos[0] = finalPos[0];
        upPos[1] = finalPos[1] - 1;

        for (int i = 0; i < possibleAttackSideList.length; i++) {
            //check if each destination tile is within bounds of board and is empty
            if (possibleAttackSideList[i][0] >= 0 && possibleAttackSideList[i][0] < board.length && possibleAttackSideList[i][1] >= 0 && possibleAttackSideList[i][1] < board[0].length && (board[possibleAttackSideList[i][0]][possibleAttackSideList[i][1]] == null || (possibleAttackSideList[i][0] == initialPos[0] && possibleAttackSideList[i][1] == initialPos[1]))) {
                //determine if a path exists to the destination tile
                if (determineAccessibility(initialPos, possibleAttackSideList[i], moves - 1, board)) {
                    //add position to list
                    finalAttackSideList.add(possibleAttackSideList[i]);
                }
            }
        }

        return finalAttackSideList;
    }

    //This is called as Gamzee begins to move. It determines whether he is berserk or not.
    public void theBardMoves() {
        if (turn % 3 == 0) {
            gamzee.goBerserk(); //Make Gamzee berserk on every third turn.
        } else {
            gamzee.beSubdued(); //Otherwise subdue him.
        }
        selectedPiece = gamzee; //Make Gamzee the selected piece.
        showAllReachable(); //Show all the places Gamzee can reach.
    }

    //This method is to be run at the end of a turn, after battle.
    public void endTurn() {
        if (playerMoving != -1) {
            //Reset the number of actions both players have.
            player1Actions = 4;
            player2Actions = 3;
            //Set the selected piece to null.
            selectedPiece = null;
            //Go through the board to find the pieces and reset them for a new turn.
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 12; y++) {
                    if (boardPieces[x][y] != null) {
                        boardPieces[x][y].turnBegins();
                    }
                }
            }
            //If Gamzee is berserk, subdue him.
            if (gamzee.isBerserk()) {
                gamzee.beSubdued();
            }
            //Increment the turn number.
            turn++;
            //On even numbered turns, player 2 goes first. On odd numbered turns, player 1 goes first.
            if (turn % 2 == 0) {
                playerMoving = 2;
            } else {
                playerMoving = 1;
            }
            if (turn == 4) { //If it is turn 4, Dirk enters the game.
                JOptionPane.showMessageDialog(null, "It is turn 4.\nPlayer 1 must now bring Dirk Strider into the game.");
                selectedPiece = dirk; //Make Dirk the selected piece.
                setSelectableForBoardEntry(); //Make all unoccupied spots along the board's perimeter possible entry points for this piece.
            } else if (turn == 5) { //If it is turn 5, Dirk enters the game.
                JOptionPane.showMessageDialog(null, "It is turn 5.\nPlayer 2 must now bring Jack Noir into the game.");
                selectedPiece = jackNoir; //Make Jack Noir the selected piece.
                setSelectableForBoardEntry(); //Make all unoccupied spots along the board's perimeter possible entry points for this piece.
            } else if (turn == 6) { //If it is turn 6, Dirk enters the game.
                JOptionPane.showMessageDialog(null, "It is turn 6.\nPlayer 2 must now bring the Felt into the game.");
                selectedPiece = theFelt; //Make the Felt the selected piece.
                setSelectableForBoardEntry(); //Make all unoccupied spots along the board's perimeter possible entry points for this piece.
            } else if (turn == 8) { //If it is turn 8, Dirk enters the game.
                JOptionPane.showMessageDialog(null, "It is turn 8."); //THIS IS WHERE I.
                JOptionPane.showMessageDialog(null, "AND THAT MEANS."); //CALIBORN.
                JOptionPane.showMessageDialog(null, "YOUR LORD."); //INTIMIDATE THE STUPID HUMAN PLAYERS.
                JOptionPane.showMessageDialog(null, "IS ALREADY HERE."); //WITH JAVA INTERFACES.
                selectedPiece = caliborn; //Make Caliborn the selected piece.
                setSelectableForBoardEntry(); //Make all unoccupied spots along the board's perimeter possible entry points for this piece.
            }
        }
    }

    //Set the reachable positions to those around the perimeter of the board.
    public void setSelectableForBoardEntry() {
        //Make an array list.
        ArrayList<int[]> coordinates = new ArrayList();
        //Go through all squares on the left and right sides of the board.
        for (int x = 0; x < 8; x++) {
            //All empty spaces on the left hand side should be reachable.
            if (boardPieces[x][0] == null) {
                int[] position = {x, 0};
                coordinates.add(position);
            }
            //All empty spaces on the right hand side should be reachable.
            if (boardPieces[x][11] == null) {
                int[] position = {x, 11};
                coordinates.add(position);
            }
        }
        //Go through all squares on the top and bottom of the board.
        for (int y = 0; y < 12; y++) {
            //All empty spaces on the top should be reachable.
            if (boardPieces[0][y] == null) {
                int[] position = {0, y};
                coordinates.add(position);
            }
            //All empty spaces on the bottom should be reachable.
            if (boardPieces[7][y] == null) {
                int[] position = {7, y};
                coordinates.add(position);
            }
        }
        //Make the reachable squares those found above.
        reachable = coordinates;
    }

    //Check to see if a player has won. Return either a 0 if no one has won, a 1 if player 1 has won, and a 2 if player 2 has won.
    public int checkVictory(Piece destroyed) {
        if (destroyed == caliborn) {
            if (!searchForPiece(condesce)) { //If Caliborn was destroyed and the Condesce is not on the board, player 1 wins.
                JOptionPane.showMessageDialog(null, "Player 1 has won!");
                endGame();
                return 1;
            }
        } else if (destroyed == condesce) {
            if (!searchForPiece(caliborn)) { //If the Condesce was destroyed and Caliborn is not on the board, player 1 wins.
                JOptionPane.showMessageDialog(null, "Player 1 has won!");
                endGame();
                return 1;
            }
        } else if (destroyed == john) {
            if (!searchForPiece(karkat) && jane.getPlayer() == 2 || !searchForPiece(jane)) { //If John was destroyed, Karkat is not on the board, and Jane is either controlled by player 2 or not on the board, player 2 wins.
                JOptionPane.showMessageDialog(null, "Player 2 has won!");
                endGame();
                return 2;
            }
        } else if (destroyed == karkat) {
            if (!searchForPiece(john) && jane.getPlayer() == 2 || !searchForPiece(jane)) { //If Karkat was destroyed, John is not on the board, and Jane is either controlled by player 2 or not on the board, player 2 wins.
                JOptionPane.showMessageDialog(null, "Player 2 has won!");
                endGame();
                return 2;
            }
        } else if (destroyed == jane && jane.getPlayer() == 1) {
            if (!searchForPiece(john) && !searchForPiece(karkat)) { //If Jane was destroyed while controlled by player 1 and John and Karkat are both not on the board, player 2 wins.
                JOptionPane.showMessageDialog(null, "Player 1 has won!");
                endGame();
                return 2;
            }
        }
        return 0;
    }

    //Return true if a given piece is on the board, and false if it isn't.
    public boolean searchForPiece(Piece p) {
        for (int x = 0; x < 8; x++) { //Loop through the whole board until the piece passed as a parameter is found.
            for (int y = 0; y < 12; y++) {
                if (boardPieces[x][y] == p) { //If the piece is found, return true.
                    return true;
                }
            }
        }
        return false; //If no such piece is found, return false.
    }

    //Destroy a certain piece.
    public void destroyPiece(Piece p) {
        for (int x = 0; x < 8; x++) { //Loop through the whole board until the piece passed as a parameter is found.
            for (int y = 0; y < 12; y++) {
                if (boardPieces[x][y] == p) { //When this piece is found, destroy it by making its place on the board null.
                    boardPieces[x][y] = null;
                    victor = checkVictory(p);
                    x = 8; //End the loops searching through the board by setting the counters for the loops to values that will make them end.
                    y = 12;
                }
            }
        }
    }

    //Construct a savefile of the game.
    public void save(String fileName) {
        try {
            saved = true;
            Name = fileName;
            fileName = "src//user_data//" + fileName;
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            frame.setTitle(Name);
        } catch (Exception ex) {
            System.out.println("Exception thrown during test: " + ex.toString());
        }

    }

    //End the game.
    public void endGame() {
        playerMoving = -1; //When the playerMoving is -1, the game is over. 
        battle = false; //Any remaining battles must end.
        needToRevive = false; //Do not revive any more pieces.
        combatants.clear(); //This list no longer needs to hold combatants.
    }

    //Wait for a specific number of milliseconds.
    public void wait(int ms) {
        long initTime = System.currentTimeMillis(); //Get the initial time.
        while (initTime > System.currentTimeMillis() - ms) {
            //Loop until a certain number of milliseconds have passed.
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
}
