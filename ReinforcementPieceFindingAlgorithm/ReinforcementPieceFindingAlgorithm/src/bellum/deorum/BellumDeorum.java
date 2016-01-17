package bellum.deorum;

import java.awt.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BellumDeorum extends JPanel implements Runnable, MouseListener, KeyListener, WindowListener {
    
    private static final long serialVersionUID = 1L;
    public static final String NAME = "Bellum Deorum";
    public static final int HEIGHT = 256;
    public static final int WIDTH = HEIGHT * 2;
    public static final int SCALE = 2;
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TRANSLUCENT);
    private boolean running = false;
    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy bufferStrategy;
    //This array list holds all coordinates reachable by the selected piece, if a piece is selected.
    private ArrayList<int[]> reachable;
    //An array holding whether or not some squares can be reached. The array is intentionally too big to stop an error that we might deal with later.
    private boolean[][] canGetTo = new boolean[16][16];
    
    int tickCount;
    
    int playerMoveX = 0;
    
    //delcare all sprites and pieces / images
    Piece selectedPiece;
    Piece boardPieces[][] = new Piece[8][12]; //Sprites are 32 by 32
    
    //Player 1's pieces;
    God john;
    God dave;
    God rose;
    God roxy;
    God jake;
    Mortal karkat;
    Mortal terezi;
    Mortal kanaya;
    God dirk;
    //An array that will hold player 1's pieces.
    Piece[] player1Pieces;
    
    //Player 2's pieces
    Mortal condesce;
    Mortal aranea;
    God jade;
    God jane;
    Piece gamzee;
    Mortal jackNoir;
    Mortal theFelt;
    God caliborn;
    //An array that will hold player 2's pieces.
    Piece[] player2Pieces;
    
    //An empty piece;
    Piece empty;
    
    BufferedImage mapBackground;
    BufferedImage baseSprite;

    //The sprites for player 1's pieces.
    BufferedImage johnSprite;
    BufferedImage daveSprite;
    BufferedImage roseSprite;
    BufferedImage roxySprite;
    BufferedImage jakeSprite;
    BufferedImage karkatSprite;
    BufferedImage tereziSprite;
    BufferedImage kanayaSprite;
    BufferedImage dirkSprite;

    //The sprites for player 2's pieces.
    BufferedImage condesceSprite;
    BufferedImage araneaSprite;
    BufferedImage jadeSprite;
    BufferedImage janeSprite;
    BufferedImage gamzeeSprite;
    BufferedImage jackNoirSprite;
    BufferedImage theFeltSprite;
    BufferedImage calibornSprite;
    
    ArrayList <Piece> attackingPieces = new ArrayList();

    public void start() {
        runBattles();
        BellumDeorum game = new BellumDeorum();
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);
        frame = new JFrame(NAME);
        //   canvas.setBounds(0, 0, WIDTH, HEIGHT);
        //canvas.setIgnoreRepaint(true);
        canvas.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        canvas.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        canvas.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        canvas.addMouseListener(this);
        canvas.addKeyListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(canvas);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addKeyListener(game);
        frame.addWindowListener(game);

        running = true;
        sprites();
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    public void resume() {
        running = true;
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

    public void tick() {
        tickCount++;
    }

    public void render() {
        if (bufferStrategy == null) {
            canvas.createBufferStrategy(3);
        }
        bufferStrategy = canvas.getBufferStrategy();
        Graphics g = bufferStrategy.getDrawGraphics();
        paint(g);
        g.dispose();
        bufferStrategy.show();
        gameUpdate();
    }

    public void sprites() {
        BufferedImageLoader loader = new BufferedImageLoader();
        BufferedImage Sprites = null, baseSprites = null;
        try {
            Sprites = loader.loadImage("spriteSheet.png");
            baseSprites = loader.loadImage("Base Standing.png");
        } catch (IOException ex) {
            Logger.getLogger(BellumDeorum.class.getName()).log(Level.SEVERE, null, ex);
        }
        SpriteSheet SpriteSheet = new SpriteSheet(Sprites);
        SpriteSheet baseSheet = new SpriteSheet(baseSprites);
        baseSprite = baseSheet.grabSprite(0, 0, 64, 64);

        daveSprite = SpriteSheet.grabSprite(0, 0, 64, 64);
        dirkSprite = SpriteSheet.grabSprite(0, 64, 64, 64);

        araneaSprite = SpriteSheet.grabSprite(0, 128, 64, 64);
        calibornSprite = SpriteSheet.grabSprite(0, 192, 64, 64);
//       try {
//           mapBackground = ImageIO.read(getClass().getResource("/resources/tempMapBackground.png"));
//           System.out.println(mapBackground);
//       } catch (IOException e) {
//           System.out.println("Image Load Failed: " + e);
//       }
        loadPieces();
    }

    @SuppressWarnings("empty-statement")
    public void loadPieces() {
        //creates all of the piece of objects

        dave = new God(daveSprite, "Dave", 3, 5, 3, 0, 3, 1);
        john = new God(baseSprite, "John", 4, 5, 3, 10, 7, 1);
        rose = new God(baseSprite, "Rose", 3, 5, 2, 11, 4, 1);
        roxy = new God(baseSprite, "Roxy", 3, 5, 2, 11, 3, 1);
        karkat = new Mortal(baseSprite, "Karkat", 2, 4, 1, 2, 7, 1);
        kanaya = new Mortal(baseSprite, "Kanaya", 2, 4, 2, 1, 6, 1);
        terezi = new Mortal(baseSprite, "Terezi", 2, 4, 1, 4, 7, 1);
        dirk = new God(dirkSprite, "Dirk", 3, 5, 3, -1, -1, 1);

        condesce = new Mortal(baseSprite, "Her Imperious Condescension", 3, 12, 5, 3, 0, 2);
        aranea = new Mortal(araneaSprite, "Aranea", 2, 4, 2, 10, 1, 2);
        jade = new God(baseSprite, "Jade", 3, 5, 3, 5, 3, 2);
        jane = new God(baseSprite, "Jane", 3, 6, 2, 6, 3, 2);
        
        Piece [] p1Pieces = {john, dave, rose, roxy, jake, karkat, terezi, kanaya};
        player1Pieces=p1Pieces;
        Piece [] p2Pieces = {condesce, aranea, jade, jane, jackNoir, theFelt, caliborn};
        player2Pieces=p2Pieces;

        //stores the pieces in the initial board
        boardPieces[7][10] = john;
        boardPieces[3][0] = dave;
        boardPieces[4][11] = rose;
        boardPieces[3][11] = roxy;
        boardPieces[7][2] = karkat;
        boardPieces[6][1] = kanaya;
        boardPieces[7][4] = terezi;

        boardPieces[0][3] = condesce;
        boardPieces[1][10] = aranea;
        boardPieces[3][5] = jade;
        boardPieces[3][6] = jane;
    }

    //method that draws everything
    @Override
    public void paint(Graphics g) {
        //draws white back image to prevent flickering
        g.drawImage(image, 0, 0, WIDTH * SCALE + 50, HEIGHT * SCALE + 50, Color.white, this);
        //draws mountain background
        //g.drawImage(mapBackground, 0, 0, WIDTH * SCALE + 50, HEIGHT * SCALE + 50, this);
        //draws grid for pieces
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 12; k++) {
                //checks if a piece is there
                if (boardPieces[i][k] != null) {
                    //draws the sprite
                    g.drawImage((BufferedImage) (boardPieces[i][k].getSprite()), k * 64, i * 64, this);
                } else {
                    //draws a rectangle
                    g.drawRect(k * 64, i * 64, 64, 64);
                }
            }
        }

        //draws selected piece info on right of screen
        if (selectedPiece != null) {
            //gets and draws pieces info
            g.drawString("Name: " + selectedPiece.getName(), 800, 100);
            //g.drawString("Type: " + selectedPiece.getType(), 800, 115);
            g.drawString("Attack: " + selectedPiece.getAttack(), 800, 130);
            g.drawString("Health: " + selectedPiece.getHealth(), 800, 145);
            //Indicates the squares the piece can move to.
            for (int x = 0; x < reachable.size(); x++) {
                if (selectedPiece.isInBattle()) {
                    g.drawString("!", reachable.get(x)[1] * 64 + 32, reachable.get(x)[0] * 64 + 32);
                } else {
                    g.drawString("#", reachable.get(x)[1] * 64 + 32, reachable.get(x)[0] * 64 + 32);
                }
            }
        }
        
        //Show question marks over all the pieces in battle. These should be changed to something like red squares later.
        int counter = 0;
        while (player1Pieces[counter] != null && counter < player1Pieces.length) {
            if (player1Pieces[counter].isInBattle()) {
                g.drawString("???", player1Pieces[counter].getX() * 64 + 32, player1Pieces[counter].getY() * 64 + 32);
            }
            counter++;
        }
        counter = 0;
        while (player2Pieces[counter] != null && counter < player2Pieces.length) {
            if (player2Pieces[counter].inBattle) {
                g.drawString("???", player2Pieces[counter].getX() * 64 + 32, player2Pieces[counter].getY() * 64 + 32);
            }
            counter++;
        }
    }

    //game code
    public void gameUpdate() {
    }

    //runs when player clicks the exit button
    @Override
    public void windowClosing(WindowEvent evt) {
        System.out.println("Saving");
        MainMenu menu = new MainMenu();
        menu.setVisible(true);
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

            case KeyEvent.VK_ESCAPE:
                if (running) {
                    this.stop();
                } else {
                    this.resume();
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
            //checks if its on the screen
            if (m.getX() < 768 && m.getY() < 512) {
                //loops to check what square was clciked
                for (int i = 0; i < 8; i++) {
                    for (int k = 0; k < 12; k++) {
                        if (m.getX() >= k * 64 && m.getX() <= (k + 1) * 64 && m.getY() - 32 >= i * 64 && m.getY() - 32 <= (i + 1) * 64) {
                            System.out.println(m.getX() + " " + m.getY());
                            System.out.println("In " + k + ", " + i);
                            if (selectedPiece != null && !canGetTo[k][i]) {
                                if (k != selectedPiece.getX() || i != selectedPiece.getY()) {
                                    return;
                                }
                            }
                            //makes that piece the selected piece
                            if (selectedPiece == null) {
                                //Do not run this code if the piece has already been moved or healed this turn.
                                if (boardPieces[i][k] != null) {
                                    if (!boardPieces[i][k].isMovable() || !boardPieces[i][k].isHealable()) {
                                        return;
                                    }
                                }
                                //If no piece is selected, select the one clicked on.
                                selectedPiece = boardPieces[i][k];
                                //Highlight all positions this piece can move to.
                                if (selectedPiece != null) {
                                    showAllReachable();
                                }
                            } else if (selectedPiece == boardPieces[i][k]) {
                                //If the clicked piece is already selected, unselect it.
                                selectedPiece = null;
                            } else if (boardPieces[i][k] == null) {
                                //If this position on the board is empty, move the selected piece here. We already know the selected piece is not null.
                                boardPieces[i][k] = selectedPiece;
                                boardPieces[selectedPiece.getY()][selectedPiece.getX()] = null;
                                selectedPiece.move(k, i);
                                selectedPiece = null;
                            } else if (selectedPiece.getPlayer() == boardPieces[i][k].getPlayer()) {
                                //If there is already a selected piece but the user is clicking on another piece, change the selected piece to the one being clicked, IF the two pieces belong to the same player.
                                selectedPiece = boardPieces[i][k];
                                //Highlight all positions this piece can move to.
                                showAllReachable();
                            } else { //This last condition is if a piece is selected and the user clicks on a piece controlled by the other player.
                                selectedPiece.inBattle = true;
                                boardPieces[i][k].inBattle = true;
                                showBattleApproaches(boardPieces[i][k]);
                                //Add the piece attacking to an array list of attacking pieces.
                                attackingPieces.add(selectedPiece);
                            }
                        }
                    }
                }
            }
        }
    }

    public void showBattleApproaches(Piece attacked) {
        int[] attackingPosition = {selectedPiece.getY(), selectedPiece.getX()};
        int[] attackedPosition = {attacked.getY(), attacked.getX()};
        reachable = genAttackSideList(attackedPosition, attackingPosition, selectedPiece.getMoves(), boardPieces);
        //This bit of code marks out, on a 2 dimensional array, where a piece can go.
        //This first bit initializes the array as all false, meaning it's assumed the piece cannot get there.
        for (int a = 0; a < 8; a++) {
            for (int b = 0; b < 12; b++) {
                canGetTo[b][a] = false;
            }
        }
        //Now, the places in the array with the coordinates held in the list of reachable places will be set to true.
        for (int c = 0; c < reachable.size(); c++) {
            canGetTo[reachable.get(c)[1]][reachable.get(c)[0]] = true;
        }
    }

    //This method is for marking all squares that a selected piece can move to.
    public void showAllReachable() {
        int[] coordinates = {selectedPiece.getY(), selectedPiece.getX()};
        reachable = genDestinationList(coordinates, selectedPiece.getMoves(), selectedPiece.getPlayer(), boardPieces);
        //This bit of code marks out, on a 2 dimensional array, where a piece can go.
        //This first bit initializes the array as all false, meaning it's assumed the piece cannot get there.
        for (int a = 0; a < 8; a++) {
            for (int b = 0; b < 12; b++) {
                canGetTo[b][a] = false;
            }
        }
        //Now, the places in the array with the coordinates held in the list of reachable places will be set to true.
        for (int c = 0; c < reachable.size(); c++) {
            canGetTo[reachable.get(c)[1]][reachable.get(c)[0]] = true;
        }
    }

    //returns an ArrayList (of int[] arrays) containing all possible positions that can be reached from the initial position, 
    //given the number of moves the piece if capable of moving in a turn  
    //***To maintain consistenty with the indexing format of 2D arrays (row, column), 
    //      COORDINATES FOR POSITION OF A PIECE ON THE BOARD SHOULD BE GIVEN IN THE FORM (Y, X)***  
    public static ArrayList<int[]> genDestinationList(int initialPos[], int moves, int player, Piece board[][]) {
        //declare and initialise required variables
        int diff;
        ArrayList<int[]> possibleDestinations = new ArrayList<int[]>();

        //iterate over all tiles in board--> all possible positions
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int[] finalPos = {i, j};    //make sure to declare & initialise a new array every time (reference variable)
                diff = compDiff(initialPos, finalPos);  //compute distance (number of tiles) between finalPos & initialPos

                //make sure destination tile is either empty or contains an enemy piece (that is not in battle) and is accessible
                //***EACH PIECE OBJECT SHOULD HAVE AN ATTRIBUTE "int player" (1 OR 2) AND AN ACCESSOR ".getplayer()"***
                //***NOT CHECKING FOR TERRAIN***
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
    private static boolean determineAccessibility(int initialPos[], int finalPos[], int moves, Piece board[][]) {
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
    public static ArrayList<int[]> genAttackSideList(int finalPos[], int initialPos[], int moves, Piece[][] board) {
        //declare and initialse arrayList
        ArrayList<int[]> finalAttackSideList = new ArrayList<int[]>();
        int leftPos[] = new int[2], rightPos[] = new int[2], downPos[] = new int[2], upPos[] = new int[2];
        int possibleAttackSideList[][] = {leftPos, rightPos, downPos, upPos};
        //System.out.println("Initial Pos: " + Arrays.toString(initialPos) + "  finalPos: " + Arrays.toString(finalPos) + "  Moves: " + moves);
        //figure out coordinates of tiles on all 4 sides of finalPos
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
                //System.out.println("AttackSide: [" + possibleAttackSideList[i][0] + "," + possibleAttackSideList[i][1] + "]" );
                //determine if a path exists to the destination tile
                if (determineAccessibility(initialPos, possibleAttackSideList[i], moves - 1, board)) {
                    //add position to list
                    finalAttackSideList.add(possibleAttackSideList[i]);
                }
            }
        }

        return finalAttackSideList;
    }
    
    public void runBattles(){
        //for (int x=0; x<attackingPieces.size(); x++){
            //Start the battle.
        int start[] = {1,2};
        int end[]= {2,3};
           //ArrayList<Piece>[] reinforcementList = genReinforcementList(start, end, boardPieces);
           //System.out.println(reinforcementList.toString());
           //JOptionPane.showMessageDialog(null, (reinforcementList[0].get(0)).getPlayer());
        //}
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
    
    //***Does NOT take terrain into account***
    public static ArrayList<Piece>[] genReinforcementList(Piece attackingPiece1, Piece attackingPiece2, Piece[][] board){
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
        for (int i = 0; i < attackingPieces.length; i++){
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
                if (possibleReinforcementList[j][0] >= 0 && possibleReinforcementList[j][0] < board.length && possibleReinforcementList[j][1] >= 0 && possibleReinforcementList[j][1] < board[0].length && board[possibleReinforcementList[j][0]][possibleReinforcementList[j][1]] != null && board[possibleReinforcementList[j][0]][possibleReinforcementList[j][1]] != attackingPiece1 && board[possibleReinforcementList[j][0]][possibleReinforcementList[j][1]] != attackingPiece2) {
                   Piece possibleReinforcement =  board[possibleReinforcementList[j][0]][possibleReinforcementList[j][1]];
                    //make sure user has not already been prompted for choosing piece as reinforcement
                    if (possibleReinforcement.canReinforce){
                        //ask user if he/she wants to deploy piece as reinforcement
                        useAsReinforcement = promptForReinforcement(possibleReinforcement);
                        
                        if (useAsReinforcement){
                            //add piece to correct position in array
                            reinforcementList[possibleReinforcement.getPlayer() - 1].add(possibleReinforcement);
                            //bring piece into battle
                            possibleReinforcement.inBattle = true;
                            
                        }else{
                            //don't check this piece again
                            possibleReinforcement.canReinforce = false;
                        }
                    }
                }
            }
        }
        return reinforcementList;         
    }
    
    private static boolean promptForReinforcement(Piece p){
        int result = JOptionPane.showConfirmDialog(null, "Would you like to use" + p.getName() + " as Reinforcement?", "Deploy Reinforcement", JOptionPane.YES_NO_OPTION);
        if (result == 0){
            return true;
        }else{
            return false;
        }
    
    }
    
    
}
