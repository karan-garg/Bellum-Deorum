/*
 * Bellum Deorum
 * June 2, 2014
 * A class for the most special piece.
 */

package bellum.deorum;

import java.awt.image.BufferedImage;

public class Gamzee extends Piece{
    
    private boolean berserk;
    
    public Gamzee(BufferedImage s, String n, int ml, int h, int a, int xPos, int yPos, int p) {
        super(s, n, ml, h, a, xPos, yPos, p);
    }
    
    public void goBerserk(){
        berserk=true;
        movementLimit=4;
        attack=6;
        player=2;
    }
    
    public void beSubdued(){
        berserk=false;
        movementLimit=1;
        attack=0;
        player=0;
    }
    
    public boolean isBerserk(){
        return berserk;
    }
    
    public String getType(){
        return "Evil Clown";
    }
}
