/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bellum.deorum;

import java.awt.image.BufferedImage;

public class God extends Piece {

    public God(BufferedImage s, String n, int ml, int h, int a, int xPos, int yPos, int p) {
        super(s, n, ml, h, a, xPos, yPos, p);
    }
    
    public void takeDamage(int damage){
        if (health>damage){
            health-=damage;
        } else {
            health=0;
            if (maxHealth>1){
                revive();
            } else {
                //Destroy the piece. Make some kind of animation for this.
            }
        }
    }
    
    public void revive(){
        int rolled;
        //Need to roll 2 dice. Somewhere, there will be a method for this, probably in the GUI. The roll will need to be displayed.
        rolled=7; //It will equal whatever was just rolled.
        if (rolled>7){
            maxHealth-=1;
            health=maxHealth;
        } else {
            //Destroy this piece.
        }
    }
    
    public String getType(){
        return "God";
    }
}
