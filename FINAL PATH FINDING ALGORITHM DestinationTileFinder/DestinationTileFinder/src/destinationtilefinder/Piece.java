/*
 * Max Reed
 * May 21, 2014
 * Creating the outline for a piece.
 */
package destinationtilefinder;

import java.awt.image.BufferedImage;

public class Piece {
    
    protected int row, column;
    protected int moves;
    protected String name;
    protected int movementLimit;
    protected int movesLeft;
    protected int player;
    protected int x;
    protected int y;
    protected int maxHealth;
    protected int health;
    protected int attack;
    protected boolean healable;
    protected boolean movable;
    protected boolean inBattle;
    protected boolean selected;
    protected BufferedImage sprite;

    public Piece(int p, int r, int c) {
        name = "";
        movementLimit = 0;
        maxHealth = 0;
        health = 0;
        attack = 0;
        movable = true;
        healable = false;
        inBattle = false;
        sprite = null;
        player = p;
        moves = (int) (Math.random() * 6) + 1;
        row = r;
        column = c;
    }

    public Piece(BufferedImage s, String n, int ml, int h, int a, int p, int m, int r, int c) {
        name = n;
        movementLimit = ml;
        maxHealth = h;
        health = h;
        attack = a;
        movable = true;
        healable = false;
        inBattle = false;
        sprite = s;
        player = p;
        moves = m;
        row = r;
        column = c;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    //This is essentially an accessor for when the piece is clicked.
    public void clicked() {
        selected = true;
        //Make selected false for all other pieces.
    }

    //Check if the user can heal this piece.
    public boolean isHealable() {
        if (healable) {
            return true;
        } else {
            return false;
        }
    }

    //Checking if this piece is in battle. This is useful because a piece in battle cannot move, heal, attack again, or be attacked.
    public boolean isInBattle() {
        if (inBattle) {
            return true;
        } else {
            return false;
        }
    }

    //Check if the user can move this piece.
    public boolean isMovable() {
        if (movable) {
            return true;
        } else {
            return false;
        }
    }

    //This method runs when the user selects to heal this piece. This will only run if healable is true.
    public void heal() {
        if (health == maxHealth - 1) {
            health = maxHealth;
        } else {
            health += 2;
        }
        healable = false;
        movable = false;
        //This should also trigger some sort of animation to show that the piece has healed.
    }

    //This method runs when the user tries to move this piece. This will only run if movable is true.
    public void move(int xPos, int yPos) {
        movable = false;
        healable = false;
        if (true) { //This condition will later be to check for if the piece is moving into a square occupied by an enemy.
            //Hence this is the regular movement code.
            x = xPos;
            y = yPos;
            //This will be changed later to allow for animation.
        } else {
            //This will move the piece to a square adjacent to the one being attacked.
            inBattle = true;
        }
    }

    //This will run when a turn begins, and will reset the boolean variable of this object.
    public void turnBegins() {
        movable = true;
        if (health < maxHealth) {
            healable = true;
        }
    }

    //Some getters.
    //Get the name.
    public String getName() {
        return name;
    }
    
     public int getMoves() {
        return moves;
    }

    //Get the x position.
    public int getColumn() {
        return column;
    }

    //Get the y position.
    public int getRow() {
        return row;
    }

    //Get the attack.
    public int getAttack() {
        return attack;
    }

    public int getHealth() {
        return health;
    }

    public int getPlayer() {
        return player;
    }
    
    public void setPlayer(int p){
        player = p;
    }
    
    @Override
    public String toString() {
        if (player == 1) {
            return "1";
        } else if (player == 0) {
            return "*";
        } else if (player == 3){
            return "I";
        }else if (player == 4){
            return "F";
        }
        
        
        else {
            return "2";
        }
    }

   
}
