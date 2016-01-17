/*
 * Karan Garg
 * May 29, 2014
 * Defines a tile on the board. Each tile holds a piece
 */
package bellum.deorum;

import java.awt.image.BufferedImage;


public class Tile {
    //define attributes
    private Piece p;
    private transient BufferedImage background;     //provides the option to change the background of a tile and highlight it
                                          //Background should be partially transparent
    
    //primary constructor
    public Tile(Piece heldPiece){
        p = heldPiece;
        background = null;
}
    //secondary constructor
    public Tile(Piece heldPiece, BufferedImage b){
        this(heldPiece);
        background = b;
    }
    
    //Accessors
    public Piece getPiece(){
        return p;
    }
    
    public BufferedImage getBackground(){
        return background;
    }
  
    @Override
    public String toString(){
        return p.getName() + "(Row, Column): (" + p.getY() + "," + p.getX() + ")";
    }
    
    //Mutators
    public void setPiece(Piece newPiece){
        p = newPiece;
    }
    
    public void setBackground(BufferedImage newBackground){
        background = newBackground;
    }
    
    
    
}
