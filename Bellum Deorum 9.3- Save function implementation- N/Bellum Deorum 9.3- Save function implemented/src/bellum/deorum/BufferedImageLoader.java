/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bellum.deorum;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author Mike
 */
public class BufferedImageLoader {
 
    public BufferedImage loadImage(String pathRelativeToThis) throws IOException{
        URL url = this.getClass().getResource(pathRelativeToThis);
        BufferedImage img = ImageIO.read(url);
        return img;
    }
    
}
