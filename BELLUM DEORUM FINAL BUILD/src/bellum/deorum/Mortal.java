/*
 * Bellum Deorum
 * Completed June 11, 2014
 * The blueprint for a mortal.
 */
package bellum.deorum;

import java.awt.image.BufferedImage;

public class Mortal extends Piece{

    public Mortal(BufferedImage s1, BufferedImage s2, String n, int ml, int h, int a, int xPos, int yPos, int p) {
        super(s1, s2, n, ml, h, a, xPos, yPos, p);
    }
    
    public void takeDamage(int damage){
        if (health>damage){
            health-=damage;
        } else {
            //Destroy the piece. Make some kind of animation for this.
        }
    }
    
    public String getType(){
        return "Mortal";
    }
}
