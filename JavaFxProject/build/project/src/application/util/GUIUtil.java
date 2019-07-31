package application.util;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class GUIUtil {

    public static BufferedImage getImage(InputStream is) {
        try {
            BufferedImage image = ImageIO.read(is);
            is.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
