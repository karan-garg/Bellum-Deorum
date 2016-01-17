/*
 * Bellum Deorum
 * Completed June 11, 2014
 * Loads the spritesheet
 */
package bellum.deorum;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author Mike
 */
public class BufferedImageLoader {
 
    public BufferedImage loadImage(String pathRelativeToThis) throws IOException{
        File file = new File("src/" + pathRelativeToThis);  
        URL url = this.getClass().getResource(pathRelativeToThis);
        BufferedImage img = ImageIO.read(file);
        return img;
    }
    
}
