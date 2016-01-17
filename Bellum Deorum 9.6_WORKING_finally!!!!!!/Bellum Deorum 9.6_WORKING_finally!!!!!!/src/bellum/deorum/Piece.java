/*
 * Max Reed
 * May 21, 2014
 * Creating the outline for a piece.
 */
package bellum.deorum;

import java.awt.image.BufferedImage;

public abstract class Piece implements java.io.Serializable{

    protected String name;
    protected int movementLimit;
    protected int movesLeft;
    protected int x;
    protected int y;
    protected int maxHealth;
    protected int health;
    protected int attack;
    protected boolean movable;
    protected boolean healable;
    protected boolean inBattle;
    protected boolean selected;
    protected boolean canReinforce;
    protected int player;
    transient BufferedImage sprite = null;
    transient BufferedImage reversedSprite = null;

    public Piece(BufferedImage s1, BufferedImage s2, String n, int ml, int h, int a, int xPos, int yPos, int p) {
        name = n;
        movementLimit = ml;
        maxHealth = h;
        health = h;
        attack = a;
        movable = true;
        inBattle = false;
        healable=false;
        x = xPos;
        y = yPos;
        sprite = s1;
        reversedSprite = s2;
        player = p;
        canReinforce = true;
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
        return healable;
    }

    //Checking if this piece is in battle. This is useful because a piece in battle cannot move, heal, attack again, or be attacked.
    public boolean isInBattle() {
        return inBattle;
    }

    //Check if the user can move this piece.
    public boolean isMovable() {
        return movable;
    }

    //This method runs when the user selects to heal this piece. This will only run if healable is true.
    public void heal() {
        if (health >= maxHealth - 3) {
            health = maxHealth;
        } else {
            health += 3;
        }
        healable=false;
        //This should also trigger some sort of animation to show that the piece has healed.
    }

    //This method runs when the user tries to move this piece. This will only run if movable is true.
    public void move(int xPos, int yPos) {
        movable = false;
        x = xPos;
        y = yPos;
    }

    //This will run when a turn begins, and will reset the boolean variable of this object.
    public void turnBegins() {
        inBattle = false;
        movable = true;
        healable=true;
    }

    //Some getters.
    //Get the name.
    public String getName() {
        return name;
    }

    //Get the x position.
    public int getX() {
        return x;
    }

    //Get the y position.
    public int getY() {
        return y;
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

    public int getMoves() {
        return movementLimit;
    }

    //Some setters.
    public void setX(int xPos) {
        x = xPos;
    }

    public void setY(int yPos) {
        y = yPos;
    }

    public boolean canReinforce() {
        return canReinforce;
    }

    public void setHealth(int hlth) {
        health = hlth;
        if (health<0){ //Make sure the piece's health cannot be less than 0.
            health=0;
        }
    }

    public BufferedImage getReversedSprite(){
        return reversedSprite;
    }
    
     public void setBufferedImage(BufferedImage img, BufferedImage imgReversed){
        sprite = img;
        reversedSprite = imgReversed;
    }
    abstract public String getType();
}
