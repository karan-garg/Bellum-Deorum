/*
 * Bellum Deorum
 * Completed June 11, 2014
 * The blueprint for Gamzee.
 */

package bellum.deorum;

import java.awt.image.BufferedImage;

public class Gamzee extends Piece{
    
    private boolean berserk;
    
    public Gamzee(BufferedImage s1, BufferedImage s2, String n, int ml, int h, int a, int xPos, int yPos, int p) {
        super(s1, s2, n, ml, h, a, xPos, yPos, p);
    }
    
    public void goBerserk(){
        berserk=true;
        movementLimit=5;
        attack=9;
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
