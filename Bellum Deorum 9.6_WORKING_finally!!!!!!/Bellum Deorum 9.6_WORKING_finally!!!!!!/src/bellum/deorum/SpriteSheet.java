/*
 * 
 */
package bellum.deorum;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    
    public transient BufferedImage spriteSheet;
    
    public SpriteSheet(BufferedImage ss){
        this.spriteSheet = ss;
        
    }
    
    public BufferedImage grabSprite(int x, int y, int width, int height){
        BufferedImage sprite = spriteSheet.getSubimage(x, y, width, height);
        return sprite;
    }
}
