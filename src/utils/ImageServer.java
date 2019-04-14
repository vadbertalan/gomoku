// Vad Bertalan | Computer Science | UBB | 2nd year | vbim1780

package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageServer {
    static public BufferedImage X_IMAGE;
    static public BufferedImage O_IMAGE;

    static {
        try {
//            X_IMAGE = ImageIO.read(new File("x.png"));
            X_IMAGE = ImageIO.read(ImageServer.class.getResourceAsStream("/x.png"));
//            O_IMAGE = ImageIO.read(new File("o.png"));
            O_IMAGE = ImageIO.read(ImageServer.class.getResourceAsStream("/o.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public BufferedImage getImage(char move) {
        return (move == 'x') ? X_IMAGE : O_IMAGE;
    }
}
